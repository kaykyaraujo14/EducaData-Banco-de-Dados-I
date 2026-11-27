# Validação final — EducaData

Data: 27/11/2026. Fontes: capturas reais e saídas de console fornecidas pela equipe; leitura integral do CSV organizado fornecido na conversa; metadados da branch main.

## Execução local

- AppOficial iniciou em modo INEP 2025. Avisos de acesso nativo do Java não impediram a execução.
- Opção 8: 138.085 escolas cadastradas e mensagem de integridade referencial OK.
- Opção 3: rural, 41.456 com internet de 50.092 (82,8%); urbana, 87.157 de 87.993 (99,0%).
- Opção 9: 27 UFs; AC 48,7%, AM 35,8%, AP 33,7%, RR 33,4%, PA 23,2%; PE 238 de 5.884 sem internet (4,0%).
- Opção 10: PE, mínimo de 10 respostas válidas; Manari 11/20 (55,0%), São Benedito do Sul 10/19 (52,6%), Iati 7/15 (46,7%), Rio Formoso 11/24 (45,8%), Angelim 5/12 (41,7%).
- Detalhe de Manari: rural 11/16 sem internet (68,8%); urbana 0/4 (0,0%).

## Auditoria do CSV organizado

- Arquivo: completo_escolas_publicas_2025.csv; separador ponto e vírgula; 13 colunas; 138.085 registros de dados, 138.085 códigos INEP distintos, zero linhas com campos excedentes ou faltantes.
- Ano: 2025 em todos os registros. Dependência: 107.371 municipais, 29.998 estaduais, 716 federais.
- Internet: 128.613 sim, 9.472 não, zero valores desconhecidos.
- Internet para alunos: 58.725 sim, 79.360 não; municipal 35.019/107.371, estadual 23.007/29.998, federal 699/716.
- Recalculados os totais por área, as 27 UFs, os 20 primeiros municípios de PE com mínimo de 10 respostas e o detalhamento de Manari: coincidem com as saídas enviadas.
- Campo projetor vazio em todos os registros, conforme limitação documentada.
- Arquivo recebido: 12.631.360 bytes; SHA-256 a5c72712c97af99ac1fd86babb4af86c97d4cc3e16a46f01d73ff1a0826dcb08.
- Após normalização CRLF para LF: 12.493.274 bytes; blob Git e3335f1148eb58691d721aa3f66c21dff3b0adb5, idêntico ao blob publicado na branch main.

## Limitações

Os resultados do console foram fornecidos pela equipe, não executados diretamente pelo assistente. A base SQLite não foi inspecionada diretamente e o CSV original do INEP não foi comparado linha a linha. A auditoria confirma a consistência do CSV organizado com os resultados do projeto, não a autenticidade de origem. Não foram publicadas capturas contendo caminhos locais. A equipe confirmou o funcionamento do link de IA após a orientação de teste em janela anônima. A entrega no Classroom ainda depende da equipe.
