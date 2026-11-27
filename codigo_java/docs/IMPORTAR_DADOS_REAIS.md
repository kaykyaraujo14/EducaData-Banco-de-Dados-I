# EducaData — Como importar o Censo Escolar 2025 no IntelliJ IDEA

**Projeto:** Banco de Dados I — CESA — Prof. Esp. Pedro Paz  
**Objetivo:** continuar a Fase 02 com uma base de escolas públicas *em funcionamento*, sem misturar os registros inventados dos testes.

## A. Baixar o arquivo verdadeiro

1. Abra https://www.gov.br/inep/pt-br/acesso-a-informacao/dados-abertos/microdados/censo-escolar
2. Clique em **2025 — Microdados do Censo Escolar da Educação Básica 2025** (versão publicada/atualizada em julho de 2026).
3. Baixe o arquivo ZIP no seu computador e clique com o botão direito nele, **Extrair Tudo**.
4. Entre nas pastas extraídas e localize **`Tabela_Escola_2025_V2.csv`** (pode estar em `dados/`). Use a **tabela de escolas**, não a de alunos/matrículas.
5. Copie esse CSV para dentro do seu projeto, na pasta **`dados/inep/`**. O caminho final será:

   `EducaData_Fase02_IntelliJ/dados/inep/Tabela_Escola_2025_V2.csv`

**Atenção:** este projeto **não** contém o microdado real. O arquivo CSV deverá ser baixado do site do INEP pela equipe. Não renomeie o CSV desnecessariamente.

## B. Abrir e importar no IntelliJ

1. Abra a pasta `EducaData_Fase02_IntelliJ`, a que contém o `pom.xml`, no IntelliJ.
2. Abra `src/main/java/br/edu/cesa/educadata/ImportarInep2025.java`.
3. Clique no triângulo verde ▶ ao lado de `main()` e selecione **Run 'ImportarInep2025.main()'**.
4. Se você já copiou o CSV para `dados/inep/`, o programa usa esse arquivo automaticamente. Senão, ele pede o caminho completo: copie o caminho do arquivo (no Windows use **Shift + botão direito > Copiar como caminho**) e cole no console do IntelliJ.
5. Ao final, confira `CONCLUÍDO` e `OK: integridade referencial verificada`. Não feche o IntelliJ durante a importação.

O programa criará o arquivo **`dados/educadata_inep2025.sqlite`**, separado do banco fictício `dados/educadata.sqlite`.

## C. Abrir o menu com dados oficiais

1. Abra `src/main/java/br/edu/cesa/educadata/AppOficial.java`.
2. Clique no triângulo verde ▶ ao lado de `main()` e escolha **Run 'AppOficial.main()'**.
3. Agora você verá o menu normal e poderá escolher **3** para comparar a internet urbana/rural, **4** para rede administrativa, **5** para município e **8** para conferir a integridade.

**Você NÃO precisa editar as VM options ou os argumentos do programa**: `AppOficial.java` já direciona para a base importada.

Para continuar utilizando seus registros fictícios, execute o `App.java` como fez anteriormente. O banco com as suas **oito escolas fictícias permanece na pasta antiga**: ele não é substituído nem incluído neste pacote novo.

## D. O que será importado

- Escolas **em funcionamento** (`TP_SITUACAO_FUNCIONAMENTO = 1`).
- Somente redes **federal, estadual e municipal** (`TP_DEPENDENCIA = 1, 2, 3`), de acordo com o recorte de escolas públicas definido na Fase 01.
- Estados e municípios, códigos das escolas e recursos tecnológicos.
- Colunas opcionais faltantes geram aviso e são armazenadas como `NULL`.
- Respostas `0` e `1` são preservadas, enquanto vazios e códigos especiais ficam como **desconhecidos (`NULL`)**.
- O total de computadores para alunos, se ambos os campos estiverem válidos, é a soma de desktops + portáteis; **não inclui tablets**. Valores `88888` (marcação de valor extremo) não entram na soma.
- **Projetor** fica `NULL` na base existente. O dicionário fornecido confirma que `IN_EQUIP_MULTIMIDIA` representa Projetor Multimídia (Datashow); a variável não foi importada e fica fora da análise principal de conectividade.

O programa lê o CSV por partes, evitando colocar o arquivo inteiro em memória. Em caso de erro SQL, a transação é revertida para evitar dados importados pela metade. Se o arquivo tiver colunas essenciais ausentes, o programa interrompe e avisa quais são.

## E. Perguntas que podem ser respondidas

1. Qual o percentual de escolas públicas em funcionamento com internet nas áreas urbana e rural?
2. Em quais municípios o percentual de escolas sem internet é maior entre respostas válidas?
3. Qual o percentual com internet para uso dos alunos em cada rede administrativa?
4. Quantas escolas públicas existem por município ou por estado?

Os relatórios **não são conclusões prontas**: é preciso executar o sistema sobre o CSV verdadeiro, conferir os totais e registrar a versão, a data e os filtros da análise.

## F. Se ocorrer algum problema

- **CSV não localizado:** confirme que a pasta é `dados/inep/` e o nome é `Tabela_Escola_2025_V2.csv`.
- **Coluna obrigatória ausente:** verifique se você baixou o CSV de 2025 na versão correta. Consulte o dicionário oficial.
- **Banco 2025 já importado:** não é permitido sobrescrever uma carga anterior. Faça uma cópia de segurança e, se necessário, remova somente `dados/educadata_inep2025.sqlite` antes de uma nova importação.
- **`No suitable driver` ou falha do JDBC:** no IntelliJ, atualize o Maven pelo `pom.xml` e confirme o acesso à dependência `org.xerial:sqlite-jdbc`.
- **Aparecem apenas escolas fictícias:** você executou `App.java`. Para os dados importados, execute `AppOficial.java`.
- **Avisos com o JDK 26:** em Run > Edit Configurations > VM options, use `--enable-native-access=ALL-UNNAMED` se necessário. Avisos não significam obrigatoriamente falha.

## G. Para documentação e entrega

Registre em seu relatório:
- Link da página oficial e identificação da edição do pacote (2025, v2/julho de 2026).
- Nome, tamanho e, quando disponível, hash/checksum do CSV original.
- Quantidade de linhas lidas, escolas incluídas, excluídas e registros inválidos retornados pelo importador.
- Dicionário de variáveis e critérios de tratamento de `NULL`.
- Evidências de execução (capturas de console), comparação urbano/rural e histórico de trabalho dos integrantes.
- Uso de IA no desenvolvimento do código, conforme exigido na disciplina.

**Fonte:** https://www.gov.br/inep/pt-br/acesso-a-informacao/dados-abertos/microdados/censo-escolar


## H. Estado observado em novembro de 2026

A equipe informou que o CSV já foi importado com **138.085 escolas públicas ativas** e que o `AppOficial.java` executou as consultas e a opção 8 (integridade OK). Portanto, **não é necessário repetir a importação no computador que já contém `dados/educadata_inep2025.sqlite`**. Este guia continua válido para abrir uma cópia em outro computador. O arquivo real de banco permanece no computador original, não neste ZIP.
