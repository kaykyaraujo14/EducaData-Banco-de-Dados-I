// EducaData - AESA/CESA - Banco de Dados I
// Apoio de IA (ChatGPT) em partes do desenvolvimento/revisao; revisao humana obrigatoria.
// Link publico da conversa: https://chatgpt.com/share/6b085b4e-b480-83e8-980f-a24107d7086a
package br.edu.cesa.educadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Scanner;

/** Relatórios e cadastro demonstrativo em SQL. */
public final class Consultas {
    private Consultas() { }

    public static void listarMunicipios(Connection conexao) throws SQLException {
        System.out.println("\n--- MUNICÍPIOS ---");
        String sql = "SELECT id_municipio, nome, uf FROM municipio ORDER BY nome LIMIT 50";
        try (Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID %d | %s - %s%n", rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        }
    }

    public static void listarEscolas(Connection conexao, int ano) throws SQLException {
        System.out.println("\n--- ESCOLAS / " + ano + " ---");
        String sql = """
                SELECT e.nome, m.nome, m.uf, r.localizacao, r.dependencia,
                       CASE i.internet WHEN 1 THEN 'Sim' WHEN 0 THEN 'Não'
                         ELSE 'Não informado' END AS internet
                FROM registro_escolar r
                JOIN escola e ON e.id_escola = r.id_escola
                JOIN municipio m ON m.id_municipio = e.id_municipio
                LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
                WHERE r.ano_referencia = ?
                ORDER BY m.nome, e.nome
                LIMIT 50
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                int total = 0;
                while (rs.next()) {
                    System.out.printf("%s | %s/%s | %s | %s | Internet: %s%n",
                            rs.getString(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6));
                    total++;
                }
                System.out.println("Exibidos: " + total + " (limite de 50; consulte o total na opção 8).");
            }
        }
    }

    public static void listarSemInternet(Connection conexao, int ano) throws SQLException {
        System.out.println("\n--- ESCOLAS SEM INTERNET / " + ano + " ---");
        String sql = """
                SELECT e.nome, m.nome, r.localizacao
                FROM registro_escolar r
                JOIN escola e ON e.id_escola = r.id_escola
                JOIN municipio m ON m.id_municipio = e.id_municipio
                JOIN infraestrutura i ON i.id_registro = r.id_registro
                WHERE r.ano_referencia = ? AND i.internet = 0
                ORDER BY m.nome, e.nome
                LIMIT 50
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                int total = 0;
                while (rs.next()) {
                    System.out.printf("%s | %s | %s%n", rs.getString(1), rs.getString(2), rs.getString(3));
                    total++;
                }
                System.out.println("Escolas com resposta 'Não': " + total);
            }
        }
        System.out.println("Dados desconhecidos (NULL) NÃO são tratados como ausência de internet.");
    }

    public static void percentualPorLocalizacao(Connection conexao, int ano) throws SQLException {
        System.out.println("\n--- INTERNET POR LOCALIZAÇÃO / " + ano + " ---");
        String sql = """
                SELECT r.localizacao, COUNT(*) AS registros,
                       COUNT(i.internet) AS respondidas,
                       SUM(CASE WHEN i.internet = 1 THEN 1 ELSE 0 END) AS com_internet,
                       ROUND(100.0 * SUM(CASE WHEN i.internet = 1 THEN 1 ELSE 0 END)
                             / NULLIF(COUNT(i.internet), 0), 1) AS percentual
                FROM registro_escolar r
                LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
                WHERE r.ano_referencia = ?
                GROUP BY r.localizacao
                ORDER BY r.localizacao
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | registros: %d | respostas válidas: %d | com internet: %d | %%: %s%n",
                            rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4),
                            percentual(rs, 5));
                }
            }
        }
        System.out.println("Percentual calculado apenas entre respostas conhecidas (0 ou 1).");
    }

    public static void percentualPorDependencia(Connection conexao, int ano) throws SQLException {
        System.out.println("\n--- INTERNET PARA ALUNOS POR REDE / " + ano + " ---");
        String sql = """
                SELECT r.dependencia, COUNT(i.internet_alunos) AS respondidas,
                       SUM(CASE WHEN i.internet_alunos = 1 THEN 1 ELSE 0 END) AS sim,
                       ROUND(100.0 * SUM(CASE WHEN i.internet_alunos = 1 THEN 1 ELSE 0 END)
                             / NULLIF(COUNT(i.internet_alunos), 0), 1) AS percentual
                FROM registro_escolar r
                LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
                WHERE r.ano_referencia = ?
                GROUP BY r.dependencia
                ORDER BY r.dependencia
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s | respostas: %d | sim: %d | percentual: %s%%%n",
                            rs.getString(1), rs.getInt(2), rs.getInt(3), percentual(rs, 4));
                }
            }
        }
    }

    public static void percentualSemInternetPorMunicipio(Connection conexao, int ano) throws SQLException {
        System.out.println("\n--- AUSÊNCIA DE INTERNET POR MUNICÍPIO / " + ano + " ---");
        String sql = """
                SELECT m.nome, m.uf, COUNT(i.internet) AS respondidas,
                       SUM(CASE WHEN i.internet = 0 THEN 1 ELSE 0 END) AS sem_internet,
                       ROUND(100.0 * SUM(CASE WHEN i.internet = 0 THEN 1 ELSE 0 END)
                             / NULLIF(COUNT(i.internet), 0), 1) AS percentual
                FROM registro_escolar r
                JOIN escola e ON e.id_escola = r.id_escola
                JOIN municipio m ON m.id_municipio = e.id_municipio
                LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
                WHERE r.ano_referencia = ?
                GROUP BY m.id_municipio
                ORDER BY percentual DESC, m.nome
                LIMIT 50
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%s/%s | respostas: %d | sem internet: %d | percentual: %s%%%n",
                            rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
                            percentual(rs, 5));
                }
            }
        }
    }


    /**
     * Mostra um panorama por UF. "Carência" aqui é APENAS o indicador de
     * ausência de internet na escola (0), não um índice socioeconômico amplo.
     */
    public static void rankingEstadosSemInternet(Connection conexao, int ano) throws SQLException {
        String sql = """
                SELECT m.uf, COUNT(*) AS total,
                       COUNT(i.internet) AS validas,
                       SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
                       COUNT(*)-COUNT(i.internet) AS desconhecidas,
                       ROUND(100.0 * SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
                           / NULLIF(COUNT(i.internet), 0), 1) AS percentual
                FROM registro_escolar r
                JOIN escola e ON e.id_escola=r.id_escola
                JOIN municipio m ON m.id_municipio=e.id_municipio
                LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
                WHERE r.ano_referencia=?
                GROUP BY m.uf
                ORDER BY (1.0 * SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
                             / NULLIF(COUNT(i.internet),0)) DESC, m.uf
                """;
        System.out.println("\n--- ESTADOS: ESCOLAS SEM INTERNET / " + ano + " ---");
        System.out.println("UF | escolas | válidas | sem internet | desconhecidas | % sem internet");
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                int exibidas = 0;
                while (rs.next()) {
                    System.out.printf(Locale.forLanguageTag("pt-BR"),
                            "%s | %d | %d | %d | %d | %s%%%n",
                            rs.getString("uf"), rs.getInt("total"), rs.getInt("validas"),
                            rs.getInt("sem_internet"), rs.getInt("desconhecidas"),
                            percentual(rs, 6));
                    exibidas++;
                }
                if (exibidas == 0) System.out.println("Nenhum registro encontrado para o ano escolhido.");
            }
        }
        System.out.println("A porcentagem considera apenas respostas 0/1. NULL não é falta de internet.");
    }

    /**
     * Permite escolher uma UF e identificar municípios com MAIOR PROPORÇÃO
     * de escolas sem internet, evitando ranking baseado só no número absoluto.
     * O mínimo de respostas válidas é configurável (padrão 10).
     */
    public static void rankingMunicipiosDeUmEstado(Connection conexao, Scanner entrada, int ano)
            throws SQLException {
        System.out.println("\n--- MUNICÍPIOS MAIS CARENTES DE INTERNET, POR ESTADO / " + ano + " ---");
        System.out.println("Estados disponíveis na base (siglas):");
        String listarUF = """
                SELECT DISTINCT m.uf FROM municipio m
                JOIN escola e ON e.id_municipio=m.id_municipio
                JOIN registro_escolar r ON r.id_escola=e.id_escola
                WHERE r.ano_referencia=? ORDER BY m.uf
                """;
        try (PreparedStatement ps = conexao.prepareStatement(listarUF)) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                int n = 0;
                while (rs.next()) {
                    System.out.print(rs.getString(1) + "  ");
                    if (++n % 12 == 0) System.out.println();
                }
                if (n == 0) {
                    System.out.println("Nenhum estado encontrado para este ano.");
                    return;
                }
                System.out.println();
            }
        }
        System.out.print("Digite a sigla do estado (ex.: PE): ");
        String uf = entrada.nextLine().strip().toUpperCase(Locale.ROOT);
        if (!uf.matches("[A-Z]{2}")) {
            System.out.println("Sigla inválida: digite duas letras, como PE, AC ou AM.");
            return;
        }
        System.out.print("Mínimo de escolas com resposta válida por município [Enter = 10]: ");
        String minimoTexto = entrada.nextLine().trim();
        int minimo = minimoTexto.isBlank() ? 10 : Integer.parseInt(minimoTexto);
        if (minimo < 1) {
            System.out.println("O mínimo deve ser 1 ou maior.");
            return;
        }
        // Panorama estadual para diferenciar o índice da UF dos municípios individuais.
        String totalUF = """
                SELECT COUNT(*) AS total, COUNT(i.internet) AS validas,
                       SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
                       COUNT(*) - COUNT(i.internet) AS desconhecidas,
                       ROUND(100.0 * SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
                             / NULLIF(COUNT(i.internet),0),1) AS pct
                FROM registro_escolar r
                JOIN escola e ON e.id_escola=r.id_escola
                JOIN municipio m ON m.id_municipio=e.id_municipio
                LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
                WHERE r.ano_referencia=? AND m.uf=?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(totalUF)) {
            ps.setInt(1, ano);
            ps.setString(2, uf);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getInt("total") == 0) {
                    System.out.println("Estado sem registros para o ano escolhido: " + uf);
                    return;
                }
                System.out.printf("%s: escolas %d | respostas válidas %d | sem internet %d | desconhecidas %d | %% sem internet: %s%%%n",
                        uf, rs.getInt("total"), rs.getInt("validas"), rs.getInt("sem_internet"),
                        rs.getInt("desconhecidas"), percentual(rs, 5));
            }
        }

        String porMunicipio = """
                WITH indicadores AS (
                    SELECT m.id_municipio, m.nome, m.uf, COUNT(*) AS total,
                           COUNT(i.internet) AS validas,
                           SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
                           COUNT(*)-COUNT(i.internet) AS desconhecidas
                    FROM registro_escolar r
                    JOIN escola e ON e.id_escola=r.id_escola
                    JOIN municipio m ON m.id_municipio=e.id_municipio
                    LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
                    WHERE r.ano_referencia=? AND m.uf=?
                    GROUP BY m.id_municipio, m.nome, m.uf
                )
                SELECT id_municipio, nome, total, validas, sem_internet, desconhecidas,
                       ROUND(100.0*sem_internet/NULLIF(validas,0),1) AS percentual
                FROM indicadores
                WHERE validas>=?
                ORDER BY (1.0*sem_internet/validas) DESC,
                         sem_internet DESC, validas DESC, nome COLLATE NOCASE
                LIMIT 20
                """;
        System.out.printf("\n--- TOP 20 MUNICÍPIOS DE %s (mínimo %d respostas válidas) ---%n", uf, minimo);
        System.out.println("ID | Município | escolas | válidas | sem internet | desconhecidas | % sem internet");
        int exibidas = 0;
        try (PreparedStatement ps = conexao.prepareStatement(porMunicipio)) {
            ps.setInt(1, ano);
            ps.setString(2, uf);
            ps.setInt(3, minimo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%d | %s/%s | %d | %d | %d | %d | %s%%%n",
                            rs.getInt("id_municipio"), rs.getString("nome"), uf,
                            rs.getInt("total"), rs.getInt("validas"),
                            rs.getInt("sem_internet"), rs.getInt("desconhecidas"),
                            percentual(rs, 7));
                    exibidas++;
                }
            }
        }
        if (exibidas == 0) {
            System.out.println("Nenhum município alcançou o mínimo escolhido. Tente um mínimo menor.");
            return;
        }
        System.out.println("O primeiro município tem a maior proporção de escolas SEM internet,");
        System.out.println("neste estado, entre os municípios com respostas suficientes.");
        System.out.println("Isso NÃO é uma medida geral de carência social ou qualidade da conexão.");
        System.out.print("Quer detalhar um município? Informe o ID acima [Enter = voltar]: ");
        String idTexto = entrada.nextLine().trim();
        if (idTexto.isBlank()) return;
        int idMunicipio = Integer.parseInt(idTexto);
        detalharMunicipio(conexao, ano, uf, idMunicipio);
    }

    /** Detalhes da UF escolhida, sem deixar selecionar município de outra UF. */
    private static void detalharMunicipio(Connection conexao, int ano, String uf, int municipio)
            throws SQLException {
        String sql = """
                SELECT m.nome, m.uf, r.localizacao, COUNT(*) AS total,
                       COUNT(i.internet) AS validas,
                       SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
                       COUNT(*)-COUNT(i.internet) AS desconhecidas,
                       ROUND(100.0*SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
                           /NULLIF(COUNT(i.internet),0),1) AS percentual
                FROM registro_escolar r
                JOIN escola e ON e.id_escola=r.id_escola
                JOIN municipio m ON m.id_municipio=e.id_municipio
                LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
                WHERE r.ano_referencia=? AND m.uf=? AND m.id_municipio=?
                GROUP BY m.id_municipio, r.localizacao ORDER BY r.localizacao
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, ano);
            ps.setString(2, uf);
            ps.setInt(3, municipio);
            try (ResultSet rs = ps.executeQuery()) {
                int n = 0;
                while (rs.next()) {
                    if (n++ == 0) System.out.println("\n--- DETALHE: " + rs.getString("nome") + "/" + uf + " ---");
                    System.out.printf("%s | escolas: %d | válidas: %d | sem internet: %d | não informadas: %d | percentual: %s%%%n",
                            rs.getString("localizacao"), rs.getInt("total"), rs.getInt("validas"),
                            rs.getInt("sem_internet"), rs.getInt("desconhecidas"), percentual(rs, 8));
                }
                if (n == 0) System.out.println("ID não encontrado nessa UF e nesse ano.");
            }
        }
    }

    private static String percentual(ResultSet rs, int coluna) throws SQLException {
        Object valor = rs.getObject(coluna);
        return valor == null ? "N/D" : String.valueOf(valor);
    }

    public static void cadastrarEscolaDemonstrativa(Connection conexao, Scanner entrada)
            throws SQLException {
        System.out.println("\n--- CADASTRAR ESCOLA FICTÍCIA ---");
        System.out.println("Este cadastro é para testes e NÃO representa dados oficiais do INEP.");
        listarMunicipios(conexao);
        int municipio = numero(entrada, "ID do município: ");
        System.out.print("Nome da escola (exemplo): ");
        String nome = entrada.nextLine().trim();
        int ano = numero(entrada, "Ano de referência (ex.: 2025): ");
        String localizacao = escolher(entrada, "Localização [1 Urbana, 2 Rural]: ",
                new String[]{"Urbana", "Rural"});
        String dependencia = escolher(entrada,
                "Rede [1 Municipal, 2 Estadual, 3 Federal, 4 Privada]: ",
                new String[]{"Municipal", "Estadual", "Federal", "Privada"});
        Integer internet = simNaoDesconhecido(entrada, "Possui internet [s/n/?]: ");
        Integer internetAlunos = simNaoDesconhecido(entrada, "Internet para alunos [s/n/?]: ");
        Integer computadores = numeroOpcional(entrada, "Número de computadores para alunos [vazio=desconhecido]: ");
        Integer lousa = simNaoDesconhecido(entrada, "Possui lousa digital [s/n/?]: ");
        Integer projetor = simNaoDesconhecido(entrada, "Possui projetor [s/n/?]: ");
        Integer laboratorio = simNaoDesconhecido(entrada, "Possui laboratório de informática [s/n/?]: ");
        if (nome.isBlank() || ano < 1990 || ano > 2100 || computadores != null && computadores < 0) {
            throw new IllegalArgumentException("Nome, ano ou número de computadores inválido.");
        }
        boolean autoCommitAnterior = conexao.getAutoCommit();
        try {
            conexao.setAutoCommit(false);
            // Não misturar registros fictícios com edições identificadas como OFICIAIS.
            try (PreparedStatement ps = conexao.prepareStatement(
                    "SELECT origem_dados FROM censo WHERE ano_referencia = ?")) {
                ps.setInt(1, ano);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && "OFICIAIS".equals(rs.getString(1))) {
                        throw new IllegalArgumentException("Ano com dados oficiais: cadastro fictício bloqueado.");
                    }
                }
            }
            try (PreparedStatement ps = conexao.prepareStatement(
                    "INSERT OR IGNORE INTO censo (ano_referencia, fonte, origem_dados) VALUES (?, ?, 'FICTICIOS')")) {
                ps.setInt(1, ano);
                ps.setString(2, "Cadastro acadêmico de exemplo, não INEP");
                ps.executeUpdate();
            }
            long idEscola;
            try (PreparedStatement ps = conexao.prepareStatement(
                    "INSERT INTO escola(nome, id_municipio) VALUES (?, ?)")) {
                ps.setString(1, nome);
                ps.setInt(2, municipio);
                ps.executeUpdate();
                idEscola = ultimoId(conexao);
            }
            long idRegistro;
            try (PreparedStatement ps = conexao.prepareStatement("""
                    INSERT INTO registro_escolar(id_escola, ano_referencia, localizacao, dependencia)
                    VALUES (?, ?, ?, ?)
                    """)) {
                ps.setLong(1, idEscola);
                ps.setInt(2, ano);
                ps.setString(3, localizacao);
                ps.setString(4, dependencia);
                ps.executeUpdate();
                idRegistro = ultimoId(conexao);
            }
            try (PreparedStatement ps = conexao.prepareStatement("""
                    INSERT INTO infraestrutura(id_registro, internet, internet_alunos,
                       computadores_alunos, lousa_digital, projetor, laboratorio_info)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """)) {
                ps.setLong(1, idRegistro);
                setOpcional(ps, 2, internet);
                setOpcional(ps, 3, internetAlunos);
                setOpcional(ps, 4, computadores);
                setOpcional(ps, 5, lousa);
                setOpcional(ps, 6, projetor);
                setOpcional(ps, 7, laboratorio);
                ps.executeUpdate();
            }
            conexao.commit();
            System.out.println("Escola fictícia cadastrada com sucesso (ID " + idEscola + ").");
        } catch (SQLException | RuntimeException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(autoCommitAnterior);
        }
    }

    private static long ultimoId(Connection conexao) throws SQLException {
        try (Statement st = conexao.createStatement();
             ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private static void setOpcional(PreparedStatement ps, int coluna, Integer valor) throws SQLException {
        if (valor == null) {
            ps.setNull(coluna, java.sql.Types.INTEGER);
        } else {
            ps.setInt(coluna, valor);
        }
    }

    private static int numero(Scanner entrada, String pergunta) {
        System.out.print(pergunta);
        return Integer.parseInt(entrada.nextLine().trim());
    }

    private static Integer numeroOpcional(Scanner entrada, String pergunta) {
        System.out.print(pergunta);
        String valor = entrada.nextLine().trim();
        return valor.isEmpty() ? null : Integer.parseInt(valor);
    }

    private static Integer simNaoDesconhecido(Scanner entrada, String pergunta) {
        System.out.print(pergunta);
        return switch (entrada.nextLine().trim().toLowerCase()) {
            case "s", "sim", "1" -> 1;
            case "n", "não", "nao", "0" -> 0;
            case "?", "" -> null;
            default -> throw new IllegalArgumentException("Responda s, n ou ?.");
        };
    }

    private static String escolher(Scanner entrada, String pergunta, String[] opcoes) {
        int posicao = numero(entrada, pergunta);
        if (posicao < 1 || posicao > opcoes.length) {
            throw new IllegalArgumentException("Opção fora da lista.");
        }
        return opcoes[posicao - 1];
    }
}
