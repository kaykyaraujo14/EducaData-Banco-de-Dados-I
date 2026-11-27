# Conferência do dicionário de dados — Censo Escolar 2025

Conferência realizada em 27/11/2026, comparando o arquivo `dicionário_dados_educação_básica.xlsx` fornecido pela equipe com o importador `ImportarInep2025.java`.

- Aba: `Tabela_de_Escola`; cabeçalhos nas linhas 7–8; coluna Y = coleta em 2025. Todas as 14 variáveis usadas pelo importador estão marcadas com “s” nessa coluna.
- SHA-256 do XLSX fornecido: `140e0f3d0df0241330f1c26fd45e516e63bd2ad60fb7ae3c9ae5cb47c6290166`.
- Fonte institucional: https://www.gov.br/inep/pt-br/acesso-a-informacao/dados-abertos/microdados/censo-escolar

| Variável | Linha | Definição/categoria | Tratamento no importador |
|---|---:|---|---|
| SG_UF | 14 | Sigla da UF, Char(2) | Texto |
| NO_MUNICIPIO | 16 | Nome do município, Char(150) | Texto |
| CO_MUNICIPIO | 17 | Código do município, Num(7) | Identificador textual |
| NO_ENTIDADE | 32 | Nome da escola, Char(100) | Texto |
| CO_ENTIDADE | 33 | Código da escola, Num(8) | Identificador textual |
| TP_DEPENDENCIA | 35 | 1 federal; 2 estadual; 3 municipal; 4 privada | Inclui 1–3 |
| TP_LOCALIZACAO | 37 | 1 urbana; 2 rural | Mapeia ambos |
| TP_SITUACAO_FUNCIONAMENTO | 48 | 1 em atividade; 2 paralisada; 3 extinta no ano; 4 extinta anteriormente | Inclui apenas 1 |
| IN_LABORATORIO_INFORMATICA | 157 | Laboratório de informática existente e utilizado; 0 não, 1 sim | Preserva 0/1 |
| IN_EQUIP_LOUSA_DIGITAL | 214 | Lousa digital existente para ensino e aprendizagem; 0 não, 1 sim | Preserva 0/1 |
| QT_DESKTOP_ALUNO | 232 | Quantidade de computadores de mesa em uso pelos alunos; 88888 = valor extremo | Exclui marcador e soma quando ambos os campos são válidos |
| QT_COMP_PORTATIL_ALUNO | 234 | Quantidade de computadores portáteis em uso pelos alunos; 88888 = valor extremo | Mesmo tratamento |
| IN_INTERNET | 239 | Acesso à internet; 0 não, 1 sim | Preserva 0/1 |
| IN_INTERNET_ALUNOS | 240 | Internet para uso dos alunos; 0 não, 1 sim | Preserva 0/1 |

## Decisões e limitações

- O dicionário confirma os filtros, os nomes e as categorias acima. Não estabelece nessas linhas que 9 ou branco equivalem a “não”; o projeto trata valores fora de 0/1 como NULL, por precaução. A lista de colunas obrigatórias/opcionais é uma decisão do importador, não uma declaração de obrigatoriedade do INEP.
- O marcador 88888 é explicitamente documentado nas duas quantidades. O limite de valores menores que 88888 é uma regra conservadora do código, não um intervalo válido completo declarado pelo INEP. O indicador soma desktops e portáteis; não inclui tablets (linha 236).
- A linha 216 identifica IN_EQUIP_MULTIMIDIA como Projetor Multimídia (Datashow), 0 não, 1 sim, coletado em 2025. A base existente não importou esse campo e mantém projetor como NULL. Essa limitação é mantida para preservar a base já produzida; o campo não integra as análises principais de conectividade. Uma eventual inclusão requer nova validação dos dados e reexportação.
- A conferência documental foi concluída para as variáveis utilizadas. Posteriormente, o CSV organizado e a execução local das opções 3, 8, 9 e 10 foram conferidos (ver `../../../evidencias/VALIDACAO_FINAL.md`). Não foram auditados diretamente o banco SQLite, o CSV original do INEP nem a identidade byte a byte do XLSX com o pacote V2 publicado. O arquivo XLSX foi fornecido pela equipe; a origem institucional não foi certificada independentemente.

