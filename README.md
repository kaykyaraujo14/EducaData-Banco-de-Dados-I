# EducaData — Desigualdade na Infraestrutura Tecnológica das Escolas Brasileiras

**AESA / CESA** • Análise e Desenvolvimento de Sistemas • Banco de Dados I • 2ª avaliação  
**Professor:** Esp. Pedro Paz  
**Integrantes:** Kayky Pereira de Araujo e Caique Henrique Da Silva Mendonça  
**Entrega informada no checklist:** 30/11/2026, até 23h59.

## Tema e questão central

Investigar diferenças de infraestrutura tecnológica entre escolas públicas brasileiras, com foco na conectividade urbana e rural. Questão: *Como a localização e a dependência administrativa se relacionam ao acesso à internet nas escolas?*

### Três subtemas apresentados
1. Acesso à internet em escolas urbanas versus rurais **(selecionado)**.
2. Computadores e laboratórios de informática nas escolas.
3. Internet para alunos por rede municipal, estadual e federal.

**Justificativa da escolha:** conectividade é uma dimensão mensurável e permite comparações territoriais não triviais, usando indicadores do Censo Escolar 2025.

## Fonte, obtenção e recorte

- INEP/MEC — Microdados do Censo Escolar 2025: https://www.gov.br/inep/pt-br/acesso-a-informacao/dados-abertos/microdados/censo-escolar
- Download manual do pacote de 2025, descompactação, leitura do CSV de escolas e importação com `ImportarInep2025.java`.
- Nome de CSV informado na execução: `Tabela_Escola_2025_V2.csv`.
- Recorte do importador: redes públicas municipal, estadual ou federal, em funcionamento.
- **214.192 linhas processadas; 138.085 escolas incluídas**, conforme o console do projeto. O banco local original não foi disponibilizado aqui para nova auditoria.

## Resultados apresentados pela equipe (consultas no IntelliJ)

| Indicador | Resultado |
|---|---:|
| Internet em escolas rurais | 41.456 / 50.092 = 82,8% |
| Internet em escolas urbanas | 87.157 / 87.993 = 99,0% |
| Internet para alunos — municipal | 35.019 / 107.371 = 32,6% |
| Internet para alunos — estadual | 23.007 / 29.998 = 76,7% |
| Internet para alunos — federal | 699 / 716 = 97,6% |
| Integridade referencial no aplicativo | OK |

**Não confundir:** internet da escola versus internet para alunos; `NULL` (desconhecido) versus `0` (não). Os percentuais são cálculos do aplicativo sobre dados informados como importados; é necessária conferência com o dicionário oficial da versão baixada.

## Funcionalidade adicional — solicitação do professor (24/11/2026)

O menu do `AppOficial.java` agora inclui **9 — Ranking por estado (UF)** e **10 — Ranking dos municípios de uma UF**, calculando os maiores percentuais de escolas sem internet em 2025. Na opção 10, a equipe pode escolher `PE`, definir um mínimo de respostas válidas (padrão de 10) e detalhar município por área urbana/rural. O código lê as cinco tabelas existentes e **não exige nova importação**. Veja `codigo_java/docs/NOVAS_CONSULTAS_ESTADOS_MUNICIPIOS.md` e `codigo_java/sql/06_RANKING_ESTADO_MUNICIPIO.sql`.

**Resultados executados em 24/11/2026:** o ranking de estados identificou AC (48,7%), AM (35,8%), AP (33,7%), RR (33,4%) e PA (23,2%) como os cinco maiores percentuais. Pernambuco registrou 238 escolas sem internet em 5.884 (4,0%). No ranking municipal de PE (mínimo de 10 respostas válidas), Manari liderou com 11 de 20 escolas sem internet (55,0%). Nos 27 registros de UF, o total é 9.472 sem internet entre 138.085 escolas, cerca de 6,9%. Os números foram conferidos quanto às somas, mas não auditados diretamente no CSV/SQLite desta etapa. "Mais carente" refere-se **exclusivamente à proporção de escolas sem internet**, não à pobreza da população.

Os arquivos `dados_resultados/ranking_estados_2025.csv` e `dados_resultados/ranking_municipios_pe_top5_2025.csv` registram os resultados informados no console. O slide 10 e o Apêndice A do relatório foram atualizados.

## Estrutura desta entrega

- `apresentacao/` — pitch final (PowerPoint e PDF), gráficos e roteiro.
- `relatorio/` — documentação de projeto conceitual e lógico.
- `modelagem/` — DER e dicionário/mapeamento inicial.
- `codigo_java/` — Java/Maven, SQLite, SQL e importador.
- `dados_resultados/` — CSVs agregados dos resultados informados pelo aplicativo.
- `dados_originais/` — instruções para acrescentar/fornecer o pacote oficial de origem.
- `documentacao/` — fluxo de atividades, responsabilidades, declaração de IA e checklist.
- `evidencias/` — espaço para capturas reais de execução (não foram inventadas).

## Trabalho em equipe

Kayky Pereira de Araujo e Caique Henrique Da Silva Mendonça realizaram o projeto **em conjunto**, com responsabilidades compartilhadas. Não há registros de reuniões formais apresentados; não foram inventadas atas. Ver `documentacao/FLUXO_E_EQUIPE.md`.

## Uso de IA

Parte do código, das explicações e dos materiais de apresentação recebeu apoio do ChatGPT. O professor exige informar o uso de IA no código e inserir um **link público da conversa**. Veja `documentacao/DECLARACAO_USO_IA.md`. **Link da conversa informado pela equipe:** https://chatgpt.com/share/6b085b4e-b480-83e8-980f-a24107d7086a. Antes da entrega, conferir abertura sem login e privacidade do conteúdo.

## Como executar

1. Abra a pasta `codigo_java` no IntelliJ como projeto Maven (`pom.xml`).
2. Use JDK 17 ou superior e carregue Maven (`sqlite-jdbc`).
3. Para dados de demonstração, execute `App.java`.
4. Para dados oficiais, baixe o pacote do INEP, coloque o CSV esperado em `codigo_java/dados/inep/` e execute `ImportarInep2025.java` **uma vez**.
5. Execute `AppOficial.java`. Se já possui um SQLite oficial importado, faça backup antes de copiá-lo para `codigo_java/dados/` e não execute a importação novamente.

## Pendências para entrega final

- [ ] Conferir variáveis e códigos com o dicionário oficial 2025 baixado.
- [x] Registrar o link da conversa no README e na declaração de uso de IA (link fornecido pela equipe; acessibilidade sem login ainda precisa ser conferida).
- [ ] Anexar dados originais e organizados conforme política de tamanho da entrega (ver `dados_originais/COMO_ENTREGAR_DADOS.md`).
- [ ] Publicar materiais em repositório da equipe, revisar acesso, fazer commit final.
- [ ] Um integrante responder no Classroom **após** o commit final.

**Atenção:** as pendências marcadas não devem ser anunciadas como concluídas até comprovação.
