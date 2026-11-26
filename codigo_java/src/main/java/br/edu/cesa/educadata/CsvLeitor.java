// EducaData - AESA/CESA - Banco de Dados I
// Apoio de IA (ChatGPT) em partes do desenvolvimento/revisao; revisao humana obrigatoria.
// Link publico da conversa: PENDENTE - inserir no README/DECLARACAO_USO_IA.md.
package br.edu.cesa.educadata;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Leitor CSV em fluxo: suporta ';', aspas duplas, aspas escapadas e quebras de linha
 * dentro de campos. Não carrega todo o Censo Escolar na memória.
 * Código acadêmico elaborado com auxílio de IA; revisar antes da entrega.
 */
public final class CsvLeitor implements AutoCloseable {
    private final PushbackReader leitor;
    private final char separador;

    public CsvLeitor(Reader reader, char separador) {
        this.leitor = new PushbackReader(reader, 2);
        this.separador = separador;
    }

    public List<String> proximaLinha() throws IOException {
        List<String> campos = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreAspas = false;
        boolean encontrouCaractere = false;
        int c;

        while ((c = leitor.read()) != -1) {
            encontrouCaractere = true;
            char ch = (char) c;
            if (entreAspas) {
                if (ch == '"') {
                    int seguinte = leitor.read();
                    if (seguinte == '"') {
                        campo.append('"');
                    } else {
                        entreAspas = false;
                        if (seguinte != -1) leitor.unread(seguinte);
                    }
                } else {
                    campo.append(ch);
                }
            } else if (ch == '"' && campo.length() == 0) {
                entreAspas = true;
            } else if (ch == separador) {
                campos.add(campo.toString());
                campo.setLength(0);
            } else if (ch == '\n' || ch == '\r') {
                if (ch == '\r') {
                    int seguinte = leitor.read();
                    if (seguinte != '\n' && seguinte != -1) leitor.unread(seguinte);
                }
                campos.add(campo.toString());
                return campos;
            } else {
                campo.append(ch);
            }
        }
        if (entreAspas) throw new IOException("CSV inválido: campo com aspas sem fechamento.");
        if (!encontrouCaractere) return null;
        campos.add(campo.toString());
        return campos;
    }

    @Override
    public void close() throws IOException {
        leitor.close();
    }
}
