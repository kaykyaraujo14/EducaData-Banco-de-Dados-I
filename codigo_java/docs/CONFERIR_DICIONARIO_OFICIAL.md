# Conferência das variáveis do Censo Escolar 2025 — checklist

**Situação atual:** documentação preparada, **conferência final do XLSX oficial pendente**. Não declarar validada sem comparar o dicionário que veio no ZIP do INEP.

## Como conferir (sem mudar o código agora)

1. No ZIP original do INEP 2025 V2, encontre o arquivo de **dicionário de dados** (`.xlsx`). Abra-o no Excel ou LibreOffice Calc.
2. Na tabela de **escolas**, procure as colunas listadas abaixo, uma por vez.
3. Para cada coluna, registre no quadro: nome exato, definição textual oficial, códigos válidos (1/0/9 etc.), se a coluna é obrigatória, tratamento de branco / não aplicável, e página/aba/linha do dicionário.
4. Confira se os filtros do importador (`TP_SITUACAO_FUNCIONAMENTO=1` e `TP_DEPENDENCIA` entre 1 e 3) correspondem à população descrita como “escolas públicas em funcionamento”.
5. Confira se `IN_INTERNET` significa presença de acesso na escola e `IN_INTERNET_ALUNOS` significa acesso **para alunos**, e se há restrições no preenchimento destes indicadores.
6. Confira se `QT_DESKTOP_ALUNO` e `QT_COMP_PORTATIL_ALUNO` representam contagens somáveis e se `88888` é de fato código especial nesse campo e nessa edição. O projeto **não soma tablets**.
7. Confira `IN_EQUIP_LOUSA_DIGITAL` e `IN_LABORATORIO_INFORMATICA`. O indicador `projetor` foi deixado `NULL` deliberadamente, porque ainda não há correspondência inequívoca.
8. Registre a fonte do arquivo original (URL, nome e data de download), seu tamanho/hash se possível. Salve capturas das saídas de consultas.

| Variável original | Campo usado | Definição e códigos conferidos no XLSX? |
|---|---|---|
| `CO_MUNICIPIO`, `NO_MUNICIPIO`, `SG_UF` | município | Pendente |
| `CO_ENTIDADE`, `NO_ENTIDADE` | escola | Pendente |
| `TP_SITUACAO_FUNCIONAMENTO` | filtro: ativa | Pendente |
| `TP_DEPENDENCIA` | filtro / rede | Pendente |
| `TP_LOCALIZACAO` | área urbana/rural | Pendente |
| `IN_INTERNET` | internet na escola | Pendente |
| `IN_INTERNET_ALUNOS` | internet para alunos | Pendente |
| `QT_DESKTOP_ALUNO` | desktops para alunos | Pendente |
| `QT_COMP_PORTATIL_ALUNO` | portáteis para alunos | Pendente |
| `IN_EQUIP_LOUSA_DIGITAL` | lousa digital | Pendente |
| `IN_LABORATORIO_INFORMATICA` | laboratório | Pendente |

**Critério de conclusão:** só marque “conferido” depois de localizar a definição e os códigos na planilha oficial. Em caso de divergência, ajuste a transformação, recrie a base em uma **cópia** e execute as consultas novamente. Nunca sobrescreva o banco original sem backup.

Fonte institucional: https://www.gov.br/inep/pt-br/acesso-a-informacao/dados-abertos/microdados/censo-escolar
