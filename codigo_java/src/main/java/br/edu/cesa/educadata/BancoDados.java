// EducaData - AESA/CESA - Banco de Dados I
// Apoio de IA (ChatGPT) em partes do desenvolvimento/revisao; revisao humana obrigatoria.
// Link publico da conversa: https://chatgpt.com/share/6b085b4e-b480-83e8-980f-a24107d7086a
package br.edu.cesa.educadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Gerencia a conexão e a criação do banco SQLite.
 * Projeto acadêmico com assistência de IA; revisar e adaptar antes da entrega.
 */
public final class BancoDados {
    private static final Path CAMINHO = Path.of("dados", "educadata.sqlite");

    private BancoDados() { }

    public static Connection conectar() throws SQLException, IOException {
        return conectar(CAMINHO);
    }

    /** Base oficial separada: a base local de demonstração NÃO é alterada. */
    public static Connection conectarOficial() throws SQLException, IOException {
        return conectar(Path.of("dados", "educadata_inep2025.sqlite"));
    }

    public static Connection conectar(Path caminho) throws SQLException, IOException {
        Files.createDirectories(caminho.toAbsolutePath().getParent());
        Connection conexao = DriverManager.getConnection("jdbc:sqlite:" + caminho.toAbsolutePath());
        // O SQLite exige ativar as FKs em cada conexão.
        try (Statement st = conexao.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conexao;
    }

    public static void inicializar(Connection conexao, boolean incluirExemplos)
            throws SQLException, IOException {
        executarScript(conexao, "/sql/01_estrutura.sql");
        if (incluirExemplos && bancoEstaVazio(conexao)) {
            executarScript(conexao, "/sql/02_dados_FICTICIOS.sql");
            System.out.println("[AVISO] Foram adicionadas escolas e indicadores FICTÍCIOS para testes.");
        }
    }

    private static boolean bancoEstaVazio(Connection conexao) throws SQLException {
        String sql = "SELECT (SELECT COUNT(*) FROM municipio) "
                + "+ (SELECT COUNT(*) FROM escola) "
                + "+ (SELECT COUNT(*) FROM censo) "
                + "+ (SELECT COUNT(*) FROM registro_escolar) AS total";
        try (Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() && rs.getInt("total") == 0;
        }
    }

    /** Executa instruções SQL de recursos do projeto, sem dependência de arquivo externo. */
    private static void executarScript(Connection conexao, String recurso)
            throws IOException, SQLException {
        InputStream stream = BancoDados.class.getResourceAsStream(recurso);
        if (stream == null) {
            throw new IOException("Arquivo SQL não encontrado: " + recurso);
        }
        String conteudo;
        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            conteudo = leitor.lines()
                    .filter(linha -> !linha.stripLeading().startsWith("--"))
                    .collect(Collectors.joining("\n"));
        }
        try (Statement st = conexao.createStatement()) {
            for (String comando : conteudo.split(";")) {
                if (!comando.isBlank()) {
                    st.execute(comando.trim());
                }
            }
        }
    }

    public static void verificar(Connection conexao) throws SQLException {
        try (Statement st = conexao.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM escola")) {
            rs.next();
            System.out.println("Escolas cadastradas: " + rs.getInt(1));
        }
        try (Statement st = conexao.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA foreign_key_check")) {
            System.out.println(rs.next()
                    ? "ERRO: há registros que violam chaves estrangeiras."
                    : "OK: integridade referencial verificada.");
        }
        try (Statement st = conexao.createStatement();
             ResultSet rs = st.executeQuery("PRAGMA database_list")) {
            if (rs.next()) System.out.println("Banco em: " + rs.getString("file"));
        }
    }
}
