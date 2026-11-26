# Como abrir e executar no IntelliJ IDEA — passo a passo

## Pré-requisitos

1. IntelliJ IDEA (Community ou Ultimate).
2. JDK 17 ou superior instalado; Java 17, 21 ou 25 são opções compatíveis.
3. Conexão com a internet na primeira sincronização Maven, para baixar o driver SQLite JDBC.

## Abrir o projeto

1. Extraia o ZIP inteiro para uma pasta do computador; não abra apenas o `App.java` dentro do ZIP.
2. No IntelliJ IDEA, clique **File > Open**.
3. Selecione a pasta `EducaData_Fase02_IntelliJ` (onde está o arquivo `pom.xml`).
4. Confirme **Trust Project**, se solicitado.
5. Configure o **Project SDK** para JDK 17 ou superior em **File > Project Structure > Project**.
6. Clique em **Load Maven Changes** ou no ícone de recarregar Maven, se necessário. Aguarde a conclusão da resolução das dependências.
7. No painel lateral, abra `src/main/java/br/edu/cesa/educadata/App.java`.
8. Clique no triângulo verde ao lado de `main` e escolha **Run 'App.main()'**.
9. Na janela **Run**, digite o número da opção do menu. Nas consultas de ano, pressione Enter para usar 2025.

## Se o IntelliJ não encontrar o arquivo SQL

Em **Run > Edit Configurations... > Working directory**, informe a pasta do projeto que contém `pom.xml`. Os scripts são carregados de `src/main/resources`, então devem ser reconhecidos como recursos Maven.

## O que acontece quando executa?

- O programa cria `dados/educadata.sqlite` automaticamente.
- Se o banco estiver vazio, ele insere um conjunto de **dados FICTÍCIOS** para exemplificar os recursos.
- Menu 1: lista escolas de um ano.
- Menu 2: mostra apenas escolas que responderam que **não** têm internet.
- Menu 3: compara porcentagens em áreas urbana e rural.
- Menu 4: compara internet para alunos por rede administrativa.
- Menu 5: compara municípios.
- Menu 6: cadastra uma escola fictícia e sua infraestrutura, numa transação SQL.
- Menu 7: lista municípios e IDs.
- Menu 8: verifica se as chaves estrangeiras estão corretas.

## Como começar com um banco vazio?

- Feche o programa.
- Renomeie ou apague **somente o banco de execução** `dados/educadata.sqlite`.
- Execute `App.main()` com o argumento de programa `--sem-exemplos` em **Run > Edit Configurations > Program arguments**.
- Assim, o sistema cria apenas as tabelas. Os arquivos SQL do projeto continuam intactos.

## Rodar via terminal Maven (alternativa)

Abra um terminal na pasta do projeto:

```bash
mvn compile exec:java
```

Para criar apenas as tabelas:

```bash
mvn compile exec:java -Dexec.args="--sem-exemplos"
```

Para verificar o banco:

```bash
mvn compile exec:java -Dexec.args="--verificar"
```

## Se aparecer "package org.sqlite não existe" ou "No suitable driver"

1. Confira se abriu o projeto pelo `pom.xml`, não como projeto Java simples.
2. Recarregue o projeto Maven.
3. Verifique a conexão com a internet e se Maven não está em modo Offline.
4. Confirme que o JDK está configurado.

## Arquivos importantes

- `pom.xml`: dependências e configuração Maven.
- `App.java`: menu principal.
- `BancoDados.java`: conexão e criação do banco.
- `Consultas.java`: SQL e cadastro de demonstração.
- `sql/01_estrutura.sql`: esquema do banco.
- `sql/02_dados_FICTICIOS.sql`: dados exclusivamente para testes.
- `sql/03_consultas.sql`: consultas SQL para copiar no editor SQL.
- `dados/educadata_exemplo.sqlite`: banco de demonstração já preenchido, que pode ser aberto em um visualizador SQLite.
- `docs/DER_diagrama.png`: desenho das cinco tabelas.

## Muito importante

Os resultados exibidos pelo programa vêm de registros inventados para verificar se o SQL funciona. **NÃO são resultados do Censo Escolar 2025.** Para usar dados do INEP, adapte o programa à base real após baixar e validar o dicionário de variáveis.
