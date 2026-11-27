# Como publicar o EducaData no GitHub — Windows / IntelliJ

**Antes de tudo:** confirme que **não** selecionou o ZIP enorme do INEP, nem `dados/educadata_inep2025.sqlite`, nem dados pessoais desnecessários. O `.gitignore` deste projeto impede incluir os CSVs locais e o SQLite importado, desde que seus arquivos estejam nos caminhos indicados.

**Situação atual (27/11/2026):** o projeto já está publicado em https://github.com/kaykyaraujo14/EducaData-Banco-de-Dados-I. Não crie outro repositório. Este guia preserva o procedimento histórico; a sincronização deve ser feita no repositório existente.

## Pelo site do GitHub (procedimento de primeira publicação)

1. Entre em https://github.com e faça login.
2. Clique em **New repository** (Novo repositório).
3. Nome sugerido: `EducaData-Banco-de-Dados-I`.
4. Escolha **Public** somente se o professor exigir um link público; confirme com a equipe antes de divulgar.
5. Crie o repositório.
6. Na pasta local `codigo_java`, selecione `src`, `sql`, `docs`, `.gitignore`, `README.md` e `pom.xml`; carregue os arquivos preservando as pastas. A interface web pode variar; se preferir manter a estrutura completa, use o Git integrado ao IntelliJ.
7. Adicione ao repositório o PDF da apresentação, o PDF do relatório e os CSVs **agregados de resultados**, que não são microdados originais.
8. Confira no GitHub que os arquivos Java e `pom.xml` estão visíveis e que o SQLite local não foi publicado. O CSV original está na Release v1.0.0; o CSV organizado está em `codigo_java/dados_organizados/`.
9. Copie a URL do repositório para enviar ao professor.

## Pelo IntelliJ (opção alternativa)

No IntelliJ, abra `Projeto_Java_IntelliJ`; use o menu **VCS > Enable Version Control Integration** (ou equivalente), selecione Git, confira o `.gitignore`, faça commit das alterações e publique em GitHub por **Git > GitHub > Share Project on GitHub** (os nomes variam por versão). Confira sempre o preview antes do primeiro commit.

## O que comprovar na entrega

- Fonte oficial do INEP e versão do CSV.
- Regras de filtragem e tratamento de `NULL`.
- DER + cinco tabelas, PK, FK, UNIQUE e CHECK.
- Scripts SQL e código Java.
- Resultados das consultas e suas limitações.
- Apresentação PDF e relatório.
- Nomes da equipe, divisão de tarefas real e declaração de auxílio de IA.

**Nota:** não publique o arquivo de banco local apenas para provar que o projeto funciona. O professor poderá executar o código sobre o CSV baixado do INEP, se necessário.
