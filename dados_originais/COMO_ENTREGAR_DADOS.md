# Dados originais e organizados — pendência local

O professor pede cópias dos dados **originais e organizados**. O arquivo `Tabela_Escola_2025_V2.csv`, o pacote completo do INEP e o banco SQLite oficial foram baixados/gerados **no computador da equipe**, e não foram enviados ao assistente. Este pacote inclui **apenas os CSVs agregados** em `dados_resultados/`.

## Para completar

1. Localize no Windows a pasta dos microdados 2025 do INEP, e salve uma cópia do arquivo original `Tabela_Escola_2025_V2.csv` sem modificar.
2. Guarde o link oficial de download, o ano, a versão e a data de obtenção.
3. Exporte e anexe o conjunto organizado/filtrado a partir de `educadata_inep2025.sqlite` usando o script `exportar_dados_organizados.py` (na pasta `codigo_java/tools`).
4. Compare os totais com as saídas (138.085 escolas) e confirme que o arquivo está correto.
5. Verifique o tamanho de cada arquivo. O GitHub Web aceita arquivos de até 25 MiB; para maiores, use Git LFS ou link externo **se o professor permitir**. Não compartilhar links privados sem autorização.
6. Acrescente um `MANIFESTO_DADOS.md` com onde cada arquivo ficou acessível ao professor (caminho no repositório ou link compartilhável).

**Não preencha com arquivos fictícios, nem anuncie o CSV como entregue enquanto ele não estiver acessível.**
