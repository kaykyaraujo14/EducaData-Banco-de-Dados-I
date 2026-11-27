// EducaData - AESA/CESA - Banco de Dados I
// Apoio de IA (ChatGPT) em partes do desenvolvimento/revisao; revisao humana obrigatoria.
// Link publico da conversa: https://chatgpt.com/share/6b085b4e-b480-83e8-980f-a24107d7086a
package br.edu.cesa.educadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

/** Ponto de entrada: execute esta classe no IntelliJ IDEA. */
public final class App {
    private App() { }

    public static void main(String[] args) {
        boolean semExemplos = Arrays.asList(args).contains("--sem-exemplos");
        boolean verificar = Arrays.asList(args).contains("--verificar");
        boolean oficial = Arrays.asList(args).contains("--oficial");
        try (Connection conexao = oficial ? BancoDados.conectarOficial() : BancoDados.conectar()) {
            if (oficial && !jaImportou(conexao)) {
                System.out.println("Ainda não há importação oficial. Execute ImportarInep2025.java primeiro.");
                return;
            }
            BancoDados.inicializar(conexao, !oficial && !semExemplos);
            System.out.println("===============================================");
            System.out.println(" EducaData  |  Banco de Dados I  - CESA ");
            System.out.println("===============================================");
            System.out.println(oficial
                    ? "Modo INEP 2025: banco separado, registros de arquivo CSV importado."
                    : "Atenção: registros de demonstração são FICTÍCIOS.");
            if (verificar) {
                BancoDados.verificar(conexao);
                return;
            }
            try (Scanner entrada = new Scanner(System.in)) {
                executarMenu(conexao, entrada, oficial);
            }
        } catch (Exception e) {
            System.err.println("Falha ao abrir ou iniciar o banco: " + e.getMessage());
            System.err.println("Confira o JDK 17+, a sincronização Maven e o diretório do projeto.");
        }
    }

    private static boolean jaImportou(Connection conexao) throws SQLException {
        try (java.sql.Statement st = conexao.createStatement()) {
            try (java.sql.ResultSet rs = st.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='censo'")) {
                if (!rs.next()) return false;
            }
            try (java.sql.ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) FROM censo WHERE ano_referencia=2025 AND origem_dados='OFICIAIS'")) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private static void executarMenu(Connection conexao, Scanner entrada, boolean oficial) {
        while (true) {
            System.out.println("\n================ MENU ================");
            System.out.println("1 - Listar escolas de um ano");
            System.out.println("2 - Escolas sem internet");
            System.out.println("3 - Percentual de internet: urbana x rural");
            System.out.println("4 - Internet para alunos por rede administrativa");
            System.out.println("5 - Ausência de internet por município");
            System.out.println(oficial ? "6 - Cadastro bloqueado no modo oficial" :
                    "6 - Cadastrar escola FICTÍCIA para testes");
            System.out.println("7 - Listar municípios");
            System.out.println("8 - Verificar integridade do banco");
            System.out.println("9 - Ranking de estados por escolas sem internet");
            System.out.println("10 - Ranking de municípios por estado (mais carentes)");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            String escolha = entrada.nextLine().trim();
            if (escolha.equals("0")) {
                System.out.println("Programa encerrado.");
                return;
            }
            try {
                switch (escolha) {
                    case "1" -> Consultas.listarEscolas(conexao, pedirAno(entrada));
                    case "2" -> Consultas.listarSemInternet(conexao, pedirAno(entrada));
                    case "3" -> Consultas.percentualPorLocalizacao(conexao, pedirAno(entrada));
                    case "4" -> Consultas.percentualPorDependencia(conexao, pedirAno(entrada));
                    case "5" -> Consultas.percentualSemInternetPorMunicipio(conexao, pedirAno(entrada));
                    case "6" -> {
                        if (oficial) System.out.println("Cadastro fictício desabilitado na base oficial.");
                        else Consultas.cadastrarEscolaDemonstrativa(conexao, entrada);
                    }
                    case "7" -> Consultas.listarMunicipios(conexao);
                    case "8" -> BancoDados.verificar(conexao);
                    case "9" -> Consultas.rankingEstadosSemInternet(conexao, pedirAno(entrada));
                    case "10" -> Consultas.rankingMunicipiosDeUmEstado(conexao, entrada, pedirAno(entrada));
                    default -> System.out.println("Opção inválida. Digite 0 a 10.");
                }
            } catch (SQLException e) {
                System.out.println("Erro de SQL: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Entrada inválida: " + e.getMessage());
            }
        }
    }

    private static int pedirAno(Scanner entrada) {
        System.out.print("Informe o ano [Enter = 2025]: ");
        String valor = entrada.nextLine().trim();
        return valor.isBlank() ? 2025 : Integer.parseInt(valor);
    }
}
