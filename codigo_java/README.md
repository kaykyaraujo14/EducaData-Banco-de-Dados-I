# EducaData — Fase 02 (revisão para entrega)

**Projeto:** Desigualdade na Infraestrutura Tecnológica das Escolas Brasileiras  
**Disciplina:** Banco de Dados I — 2ª avaliação (AESA/CESA)  
**Professor:** Esp. Pedro Paz  
**Autores:** Kayky Pereira de Araujo; Caique Henrique Da Silva Mendonça  
**Tecnologia:** Java 17+, Maven, SQLite/JDBC, SQL.  
**Base de referência:** Microdados do Censo Escolar 2025 — INEP/MEC (página atualizada em julho de 2026).

## O que foi executado pela equipe

Segundo os **resultados do console fornecidos pela equipe em novembro de 2026**, a importação de `Tabela_Escola_2025_V2.csv` processou **214.192 linhas**, incluindo **138.085 escolas públicas em funcionamento** no banco `dados/educadata_inep2025.sqlite`. As opções 3, 4, 5 e 8 de `AppOficial.java` foram executadas; a opção 8 informou **“OK: integridade referencial verificada”**.

**Limite da comprovação:** o arquivo CSV original e o banco SQLite do computador não foram entregues para auditoria independente neste pacote. Os totais reproduzem os resultados de execução; não equivalem a estatísticas oficiais do INEP verificadas de forma independente. Os códigos e significados das variáveis devem ser confrontados com o dicionário oficial da edição usada.


## Atualização sugerida pelo professor: análise por estado e município

No menu do `AppOficial.java`, execute:

- **9 — Ranking de estados por escolas sem internet:** escolhe o ano (Enter = 2025) e mostra, para cada UF, o total de escolas, respostas válidas, quantidade sem internet, desconhecidas e percentual de ausência.
- **10 — Ranking de municípios por estado:** escolhe ano, digita uma UF (por exemplo, `PE`), define o mínimo de respostas válidas por município (Enter = 10) e recebe o ranking dos 20 municípios com **maior proporção de escolas sem internet** naquela UF. Pode digitar o ID de um município para detalhar a situação urbana/rural.

A expressão **"município mais carente"** é usada no sistema **apenas no indicador de ausência de internet escolar** (resposta `IN_INTERNET = 0`). Não se trata de índice de pobreza, de qualidade de internet, nem de falta de outros recursos. O ranking usa respostas `0` e `1`, descartando `NULL` do denominador e exibindo quantas informações estão ausentes. Um mínimo de 10 respostas evita ranquear municípios muito pequenos com uma ou duas respostas, mas esse corte pode ser alterado.

Essas opções consultam as **tabelas já existentes**, sem alterar nem reinicializar o arquivo `dados/educadata_inep2025.sqlite`. O relatório antigo continua a reproduzir as saídas de 2025 fornecidas pela equipe; os novos rankings deverão ser executados no IntelliJ antes de serem adicionados à apresentação como resultados comprovados.

## Abrir no IntelliJ

1. Abra esta pasta `Projeto_Java_IntelliJ` no IntelliJ (ela contém o `pom.xml`).
2. Configure Java 17 ou superior e carregue Maven. A dependência do SQLite JDBC é descrita em `pom.xml`.
3. **Para dados importados:** mantenha seu arquivo local `dados/educadata_inep2025.sqlite` na pasta `dados` e execute `AppOficial.java`.
4. **Para importar do zero, somente em uma cópia/novo banco:** copie `Tabela_Escola_2025_V2.csv` para `dados/inep/` e execute `ImportarInep2025.java`. Não rode o importador novamente sobre o banco já carregado.
5. **Para exemplos fictícios:** execute `App.java` sem `--oficial`; ele usa outro arquivo `dados/educadata.sqlite`.

**Importante:** por segurança, este ZIP **não contém** `educadata_inep2025.sqlite`, `educadata.sqlite` nem o CSV de microdados. Não exclua nem mova o banco funcional que está no seu computador. Caso queira usar a versão revisada com seu banco, faça uma cópia de segurança e depois copie apenas o `.sqlite` para `dados/` da nova pasta.

## Pastas importantes

- `src/main/java/br/edu/cesa/educadata/` — classes Java do aplicativo e importador.
- `sql/01_estrutura.sql` — tabelas, PK/FK, regras SQL.
- `sql/04_consultas_INEP_2025.sql` — consultas analíticas.
- `sql/05_VERIFICACAO_BANCO_2025.sql` — consultas de conferência, sem alterar dados.
- `docs/FASE02_MODELAGEM.md` — modelo conceitual e lógico atualizado.
- `docs/MAPEAMENTO_CENSO_2025.md` — mapeamento proposto de variáveis, com validações pendentes.
- `docs/CONFERIR_DICIONARIO_OFICIAL.md` — o que conferir no pacote do INEP.
- `docs/GITHUB_PASSO_A_PASSO.md` — instruções de publicação.
- `docs/RESPONSABILIDADES_EQUIPE.md` — campo a preencher com contribuições reais.

## Resultados principais informados no console

| Indicador | Resultado | Observação |
|---|---:|---|
| Escolas públicas em funcionamento importadas | 138.085 | Conforme saída do importador |
| Internet: escolas rurais | 82,8% | 41.456 / 50.092 respostas |
| Internet: escolas urbanas | 99,0% | 87.157 / 87.993 respostas |
| Internet para estudantes: rede municipal | 32,6% | 35.019 / 107.371 respostas |
| Internet para estudantes: rede estadual | 76,7% | 23.007 / 29.998 respostas |
| Internet para estudantes: rede federal | 97,6% | 699 / 716 respostas |
| Integridade referencial | OK | Conforme opção 8 da aplicação |

Não confundir “internet na escola” com “internet para alunos”. Os percentuais não demonstram a causa das diferenças.

## Autoria e declaração de IA

Os dois integrantes estão identificados. A equipe informou **responsabilidades compartilhadas, sem divisão fixa de tarefas**. O fluxo conjunto está registrado em `docs/RESPONSABILIDADES_EQUIPE.md`. Não foram fornecidas atas de reuniões, portanto nenhuma foi criada. O projeto, os códigos e os documentos foram preparados com assistência de inteligência artificial e devem ser revisados pela equipe antes da entrega, conforme as regras da disciplina.

**Fonte:** https://www.gov.br/inep/pt-br/acesso-a-informacao/dados-abertos/microdados/censo-escolar
