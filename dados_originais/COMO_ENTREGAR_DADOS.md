# Dados originais e organizados — situação da entrega

O professor pede cópias dos dados **originais e organizados**. O **CSV original de escolas foi publicado na Release v1.0.0** e o conjunto completo organizado/filtrado foi publicado na branch `main` no commit `b151e59`. O repositório também mantém os CSVs agregados em `dados_resultados/`. O banco SQLite oficial permanece no computador da equipe.

## Download do CSV original

- **Arquivo:** [Tabela_Escola_2025_V2.csv](https://github.com/kaykyaraujo14/EducaData-Banco-de-Dados-I/releases/download/v1.0.0/Tabela_Escola_2025_V2.csv)
- **Versão publicada:** [EducaData v1.0.0](https://github.com/kaykyaraujo14/EducaData-Banco-de-Dados-I/releases/tag/v1.0.0)
- **Tamanho no GitHub:** 141.425.160 bytes (aprox. 135 MB)
- **Fonte:** INEP/MEC — Censo Escolar 2025.
- **Importante:** o CSV foi disponibilizado como ativo da Release por exceder o limite comum de arquivos do repositório Git. Não o adicione ao histórico Git.

## Conjunto organizado publicado

- Arquivo: [completo_escolas_publicas_2025.csv](../codigo_java/dados_organizados/completo_escolas_publicas_2025.csv)
- Tamanho confirmado no GitHub: 12.493.274 bytes (aprox. 12 MB).
- Quantidade informada pela equipe: 138.085 registros.
- A presença do arquivo foi confirmada; o conteúdo e a contagem não foram auditados independentemente.

## Conferência final dos dados

1. Conferir que o CSV original na Release corresponde ao baixado do INEP 2025; registrar a data original de obtenção e o dicionário oficial.
2. O conjunto organizado/filtrado foi exportado a partir de `educadata_inep2025.sqlite`; o script `exportar_dados_organizados.py` está na pasta `codigo_java/tools`.
3. Compare a quantidade exportada com os 138.085 registros indicados pelo importador.
4. O conjunto exportado já está publicado no histórico Git e tem cerca de 12 MB. O CSV original maior que 100 MB permanece na Release.
5. Atualize o manifesto de dados com os links do conjunto organizado e do dicionário usado na validação.

**Situação confirmada em 27/11/2026:** ambos os arquivos estão publicados; faltam a conferência de conteúdo, a validação do dicionário oficial e a entrega no Classroom.
