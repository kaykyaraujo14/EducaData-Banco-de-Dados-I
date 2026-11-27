// EducaData - AESA/CESA - Banco de Dados I
// Apoio de IA (ChatGPT) em partes do desenvolvimento/revisao; revisao humana obrigatoria.
// Link publico da conversa: PENDENTE - inserir no README/DECLARACAO_USO_IA.md.
package br.edu.cesa.educadata;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * IMPORTADOR DE ESCOLAS (CENSO ESCOLAR 2025, CSV Tabela_Escola_2025_V2).
 * IMPORTANTE: o CSV deve ser baixado do portal oficial do INEP e extraído do ZIP.
 * O importador NÃO faz o download, NÃO verifica a autenticidade do arquivo e NÃO
 * altera o banco local de demonstração: usa dados/educadata_inep2025.sqlite.
 *
 * Regras: escolas públicas e em atividade; 0 != NULL; códigos extremos não viram
 * quantidades reais; importação transacional, rejeição de colunas obrigatórias ausentes.
 * Autores do projeto: Kayky Pereira de Araujo; Caique Henrique Da Silva Mendonça.
 * Código acadêmico elaborado com auxílio de IA; revisar antes da entrega.
 */
public final class ImportarInep2025 {
    private static final int ANO = 2025;
    private static final Path CAMINHO_PADRAO = Path.of("dados", "inep", "Tabela_Escola_2025_V2.csv");
    private static final Path BANCO = Path.of("dados", "educadata_inep2025.sqlite");

    private static final List<String> OBRIGATORIAS = List.of(
            "CO_ENTIDADE", "NO_ENTIDADE", "CO_MUNICIPIO", "NO_MUNICIPIO", "SG_UF",
            "TP_SITUACAO_FUNCIONAMENTO", "TP_LOCALIZACAO", "TP_DEPENDENCIA", "IN_INTERNET");
    private static final List<String> OPCIONAIS = List.of(
            "IN_INTERNET_ALUNOS", "QT_DESKTOP_ALUNO", "QT_COMP_PORTATIL_ALUNO",
            "IN_EQUIP_LOUSA_DIGITAL", "IN_LABORATORIO_INFORMATICA");

    private ImportarInep2025() { }

    public static void main(String[] args) {
        try {
            Path arquivo = escolherArquivo(args);
            if (!Files.isRegularFile(arquivo)) {
                System.err.println("CSV não localizado: " + arquivo.toAbsolutePath());
                System.err.println("Baixe e extraia o Censo 2025 na pasta dados/inep. Veja docs/IMPORTAR_DADOS_REAIS.md.");
                return;
            }
            System.out.println("IMPORTAÇÃO INEP 2025 - fonte do CSV deve ser conferida pelo usuário");
            System.out.println("Arquivo de entrada: " + arquivo.toAbsolutePath());
            System.out.println("Banco oficial SEPARADO: " + BANCO.toAbsolutePath());
            importar(arquivo);
        } catch (Exception e) {
            System.err.println("IMPORTAÇÃO NÃO CONCLUÍDA: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static Path escolherArquivo(String[] args) {
        if (args.length > 0 && !args[0].isBlank()) return Path.of(args[0].trim().replace("\"", ""));
        if (Files.isRegularFile(CAMINHO_PADRAO)) return CAMINHO_PADRAO;
        System.out.print("Cole o caminho completo do CSV Tabela_Escola_2025_V2.csv: ");
        Scanner teclado = new Scanner(System.in);
        String caminho = teclado.nextLine().trim();
        return caminho.isBlank() ? CAMINHO_PADRAO : Path.of(caminho.replace("\"", ""));
    }

    public static void importar(Path arquivo) throws Exception {
        // A distribuição pública de 2025 utiliza ISO-8859-1 e ';'. Aceita BOM UTF-8
        // caso um arquivo tenha sido convertido manualmente.
        try (BufferedInputStream bytes = new BufferedInputStream(Files.newInputStream(arquivo))) {
            bytes.mark(4);
            byte[] bom = bytes.readNBytes(3);
            boolean utf8 = bom.length == 3 && (bom[0] & 255) == 239
                    && (bom[1] & 255) == 187 && (bom[2] & 255) == 191;
            if (!utf8) bytes.reset();
            Charset charset = utf8 ? StandardCharsets.UTF_8 : StandardCharsets.ISO_8859_1;
            try (CsvLeitor csv = new CsvLeitor(new BufferedReader(
                    new InputStreamReader(bytes, charset), 1 << 16), ';')) {
                List<String> colunas = csv.proximaLinha();
                if (colunas == null) throw new IllegalArgumentException("CSV vazio.");
                Map<String, Integer> indices = indices(colunas);
                conferirCabecalho(indices);
                importarLinhas(csv, indices, colunas.size(), arquivo);
            }
        }
    }

    private static Map<String, Integer> indices(List<String> colunas) {
        Map<String, Integer> resultado = new HashMap<>();
        for (int i = 0; i < colunas.size(); i++) {
            String coluna = colunas.get(i).replace("\uFEFF", "").trim().toUpperCase(Locale.ROOT);
            if (!coluna.isEmpty()) resultado.put(coluna, i);
        }
        return resultado;
    }

    private static void conferirCabecalho(Map<String, Integer> indices) {
        List<String> ausentes = new ArrayList<>();
        for (String coluna : OBRIGATORIAS) if (!indices.containsKey(coluna)) ausentes.add(coluna);
        if (!ausentes.isEmpty()) throw new IllegalArgumentException(
                "Colunas obrigatórias ausentes: " + ausentes + ". Confira a versão/dicionário do INEP.");
        for (String coluna : OPCIONAIS) {
            if (!indices.containsKey(coluna))
                System.out.println("[AVISO] Coluna opcional ausente: " + coluna + " (o campo ficará NULL).");
        }
        System.out.println("Cabeçalho identificado: " + indices.size() + " colunas.");
    }

    private static void importarLinhas(CsvLeitor csv, Map<String, Integer> colunas,
                                      int esperado, Path arquivo) throws Exception {
        // A transação evita deixar uma importação parcial após qualquer falha.
        try (Connection db = BancoDados.conectarOficial()) {
            BancoDados.inicializar(db, false); // jamais adiciona os exemplos fictícios
            try (PreparedStatement check = db.prepareStatement(
                    "SELECT COUNT(*) FROM censo WHERE ano_referencia=?")) {
                check.setInt(1, ANO);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt(1) != 0)
                        throw new IllegalStateException("O banco 2025 já foi importado. Para reiniciar, "
                                + "faça backup e apague apenas dados/educadata_inep2025.sqlite.");
                }
            }
            db.setAutoCommit(false);
            long lidas = 0, importadas = 0, privadas = 0, inativas = 0, invalidas = 0;
            Map<String, Long> municipios = new HashMap<>();
            Set<String> codigos = new HashSet<>();
            try (PreparedStatement censos = db.prepareStatement(
                        "INSERT INTO censo(ano_referencia, fonte, origem_dados) VALUES (?, ?, 'OFICIAIS')");
                 PreparedStatement mun = db.prepareStatement(
                        "INSERT INTO municipio(nome, uf, codigo_ibge) VALUES (?, ?, ?)");
                 PreparedStatement escola = db.prepareStatement(
                        "INSERT INTO escola(codigo_inep, nome, id_municipio) VALUES (?, ?, ?)");
                 PreparedStatement registro = db.prepareStatement(
                        "INSERT INTO registro_escolar(id_escola, ano_referencia, localizacao, dependencia) "
                                + "VALUES (?, ?, ?, ?)");
                 PreparedStatement infra = db.prepareStatement(
                        "INSERT INTO infraestrutura(id_registro, internet, internet_alunos, "
                                + "computadores_alunos, lousa_digital, projetor, laboratorio_info) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?)");
                 Statement last = db.createStatement()) {
                censos.setInt(1, ANO);
                censos.setString(2, "INEP/MEC - CSV fornecido pelo usuário: " + arquivo.getFileName());
                censos.executeUpdate();
                List<String> linha;
                while ((linha = csv.proximaLinha()) != null) {
                    lidas++;
                    if (linha.size() != esperado) {
                        invalidas++;
                        continue;
                    }
                    if (!"1".equals(valor(linha, colunas, "TP_SITUACAO_FUNCIONAMENTO"))) {
                        inativas++;
                        continue;
                    }
                    String dep = valor(linha, colunas, "TP_DEPENDENCIA");
                    String dependencia = switch (dep) {
                        case "1" -> "Federal";
                        case "2" -> "Estadual";
                        case "3" -> "Municipal";
                        default -> null;
                    };
                    if (dependencia == null) { privadas++; continue; }
                    String loc = valor(linha, colunas, "TP_LOCALIZACAO");
                    String localizacao = switch (loc) {
                        case "1" -> "Urbana";
                        case "2" -> "Rural";
                        default -> null;
                    };
                    String codigoEscola = valor(linha, colunas, "CO_ENTIDADE");
                    String nomeEscola = valor(linha, colunas, "NO_ENTIDADE");
                    String codigoMunicipio = valor(linha, colunas, "CO_MUNICIPIO");
                    String nomeMunicipio = valor(linha, colunas, "NO_MUNICIPIO");
                    String uf = valor(linha, colunas, "SG_UF").toUpperCase(Locale.ROOT);
                    if (localizacao == null || codigoEscola.isEmpty() || nomeEscola.isEmpty()
                            || codigoMunicipio.isEmpty() || nomeMunicipio.isEmpty() || uf.length() != 2
                            || !codigoEscola.matches("[0-9]+") || !codigoMunicipio.matches("[0-9]+")) {
                        invalidas++;
                        continue;
                    }
                    if (!codigos.add(codigoEscola)) { invalidas++; continue; }
                    Long idMunicipio = municipios.get(codigoMunicipio);
                    if (idMunicipio == null) {
                        mun.setString(1, nomeMunicipio);
                        mun.setString(2, uf);
                        mun.setString(3, codigoMunicipio);
                        mun.executeUpdate();
                        idMunicipio = ultimoId(last);
                        municipios.put(codigoMunicipio, idMunicipio);
                    }
                    escola.setString(1, codigoEscola);
                    escola.setString(2, nomeEscola);
                    escola.setLong(3, idMunicipio);
                    escola.executeUpdate();
                    long idEscola = ultimoId(last);
                    registro.setLong(1, idEscola);
                    registro.setInt(2, ANO);
                    registro.setString(3, localizacao);
                    registro.setString(4, dependencia);
                    registro.executeUpdate();
                    long idRegistro = ultimoId(last);
                    infra.setLong(1, idRegistro);
                    inteiroNulo(infra, 2, booleano(linha, colunas, "IN_INTERNET"));
                    inteiroNulo(infra, 3, booleano(linha, colunas, "IN_INTERNET_ALUNOS"));
                    inteiroNulo(infra, 4, computadores(linha, colunas));
                    inteiroNulo(infra, 5, booleano(linha, colunas, "IN_EQUIP_LOUSA_DIGITAL"));
                    // O dicionário de 2025 identifica IN_EQUIP_MULTIMIDIA como projetor Datashow.
                    // A versão atual mantém o campo NULL para preservar o recorte existente.
                    infra.setNull(6, Types.INTEGER);
                    inteiroNulo(infra, 7, booleano(linha, colunas, "IN_LABORATORIO_INFORMATICA"));
                    infra.executeUpdate();
                    importadas++;
                    if (importadas % 10000 == 0) System.out.println("Importadas: " + importadas + " escolas...");
                }
                if (importadas == 0) throw new IllegalStateException(
                        "Nenhuma escola pública ativa importada. Verifique o arquivo e os códigos oficiais.");
                db.commit();
                System.out.printf("%nCONCLUÍDO. Linhas: %d | Escolas públicas ativas: %d | "
                                + "Outras redes: %d | Inativas: %d | Inválidas/duplicadas: %d%n",
                        lidas, importadas, privadas, inativas, invalidas);
                System.out.println("Banco salvo em: " + BANCO.toAbsolutePath());
                BancoDados.verificar(db);
                if (invalidas > 0) System.out.println("[AVISO] Revise as linhas rejeitadas e o CSV original.");
                System.out.println("Para consultar: execute App.java com o argumento --oficial.");
            } catch (Exception ex) {
                db.rollback();
                System.err.println("Operação revertida. Não houve importação parcial.");
                throw ex;
            } finally {
                db.setAutoCommit(true);
            }
        }
    }

    private static long ultimoId(Statement st) throws SQLException {
        try (ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
            if (!rs.next()) throw new SQLException("Não foi possível obter o identificador criado.");
            return rs.getLong(1);
        }
    }

    private static String valor(List<String> linha, Map<String, Integer> colunas, String nome) {
        Integer posicao = colunas.get(nome);
        return posicao == null ? "" : linha.get(posicao).trim();
    }

    private static Integer booleano(List<String> linha, Map<String, Integer> colunas, String nome) {
        return switch (valor(linha, colunas, nome)) {
            case "1" -> 1;
            case "0" -> 0;
            default -> null; // 9, vazio, não aplicável e outros são desconhecidos
        };
    }

    private static Integer qtd(List<String> linha, Map<String, Integer> colunas, String nome) {
        String v = valor(linha, colunas, nome);
        if (v.isEmpty()) return null;
        try {
            int quantidade = Integer.parseInt(v);
            // 88888 é marcador de valor extremo no dicionário, não quantidade.
            return quantidade >= 0 && quantidade < 88888 ? quantidade : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer computadores(List<String> linha, Map<String, Integer> colunas) {
        Integer desktop = qtd(linha, colunas, "QT_DESKTOP_ALUNO");
        Integer portatil = qtd(linha, colunas, "QT_COMP_PORTATIL_ALUNO");
        if (desktop == null || portatil == null) return null;
        return desktop + portatil;
    }

    private static void inteiroNulo(PreparedStatement stmt, int indice, Integer v) throws SQLException {
        if (v == null) stmt.setNull(indice, Types.INTEGER);
        else stmt.setInt(indice, v);
    }
}
