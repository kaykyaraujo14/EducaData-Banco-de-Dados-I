package br.edu.cesa.educadata;

import java.io.StringReader;
import java.util.List;

/** Teste local sem dependências: execute pela classe TesteLeitorCsv. */
public class TesteLeitorCsv {
    public static void main(String[] args) throws Exception {
        String teste = "COD;NOME;TEXTO;ULTIMO\r\n"
                + "1;\"Escola; Central\";\"Palavra \"\"citada\"\"\";\r\n"
                + "2;\"Escola\nRural\";ok;\r\n";
        try (CsvLeitor csv = new CsvLeitor(new StringReader(teste), ';')) {
            assertEquals(List.of("COD", "NOME", "TEXTO", "ULTIMO"), csv.proximaLinha());
            assertEquals(List.of("1", "Escola; Central", "Palavra \"citada\"", ""), csv.proximaLinha());
            assertEquals(List.of("2", "Escola\nRural", "ok", ""), csv.proximaLinha());
            if (csv.proximaLinha() != null) throw new AssertionError("Esperado fim de arquivo.");
        }
        System.out.println("OK: CSV com ;, aspas escapadas, campo vazio e múltiplas linhas.");
    }
    private static void assertEquals(List<String> esperado, List<String> atual) {
        if (!esperado.equals(atual))
            throw new AssertionError("Esperado " + esperado + ", obtido " + atual);
    }
}
