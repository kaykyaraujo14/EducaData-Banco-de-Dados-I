# Dados originais e organizados — situação da entrega

O professor pede cópias dos dados **originais e organizados**. O arquivo `Tabela_Escola_2025_V2.csv`, o pacote completo do INEP e o banco SQLite oficial foram baixados/gerados **no computador da equipe**, e não foram enviados ao assistente. O **CSV original de escolas foi publicado na Release v1.0.0**, enquanto o repositório mantém os CSVs agregados em `dados_resultados/`. O banco SQLite oficial e o conjunto completo organizado/filtrado não foram publicados.

## Download do CSV original

- **Arquivo:** [Tabela_Escola_2025_V2.csv](https://github.com/kaykyaraujo14/EducaData-Banco-de-Dados-I/releases/download/v1.0.0/Tabela_Escola_2025_V2.csv)
- **Versão publicada:** [EducaData v1.0.0](https://github.com/kaykyaraujo14/EducaData-Banco-de-Dados-I/releases/tag/v1.0.0)
- **Tamanho no GitHub:** 141.425.160 bytes (aprox. 135 MB)
- **Fonte:** INEP/MEC — Censo Escolar 2025.
- **Importante:** o CSV foi disponibilizado como ativo da Release por exceder o limite comum de arquivos do repositório Git. Não o adicione ao histórico Git.

## Para completar o conjunto organizado

1. Conferir que o CSV original na Release corresponde ao baixado do INEP 2025; registrar a data original de obtenção e o dicionário oficial.
2. Exporte o conjunto organizado/filtrado a partir de `educadata_inep2025.sqlite` usando `exportar_dados_organizados.py` (na pasta `codigo_java/tools`).
3. Compare a quantidade exportada com os 138.085 registros indicados pelo importador.
4. Verifique o tamanho do conjunto exportado e escolha um meio de entrega adequado; para arquivos maiores que 100 MB, um ativo de Release pode ser usado. Não adicione arquivos grandes diretamente ao histórico Git.
5. Atualize o manifesto de dados com os links do conjunto organizado e do dicionário usado na validação.

**Situação confirmada:** o CSV original está publicado e acessível via ativo da Release. A exportação e a publicação do conjunto organizado/filtrado ainda exigem conferência.
