# Dicionário inicial — campos de referência

**Proposta preliminar; consultar o dicionário oficial do CSV do INEP 2025 para confirmar codificação e versão.**

| Campo no CSV | Interpretação usada no projeto | Destino / uso |
|---|---|---|
| `CO_ENTIDADE` | Código da escola | `escola.codigo_inep` |
| `NO_ENTIDADE` | Nome da escola | `escola.nome` |
| `CO_MUNICIPIO` | Código do município | `municipio.codigo_ibge` |
| `NO_MUNICIPIO` | Nome do município | `municipio.nome` |
| `SG_UF` | Sigla da unidade federativa | `municipio.uf` |
| `TP_LOCALIZACAO` | Categoria urbana/rural | `registro_escolar.localizacao` |
| `TP_DEPENDENCIA` | Dependência administrativa | `registro_escolar.dependencia` |
| `TP_SITUACAO_FUNCIONAMENTO` | Situação da escola | Filtro de importação |
| `IN_INTERNET` | Internet existente na escola | `infraestrutura.internet` |
| `IN_INTERNET_ALUNOS` | Internet para estudantes | `infraestrutura.internet_alunos` |
| `QT_DESKTOP_ALUNO` | Desktops para alunos | Parte do indicador de computadores |
| `QT_COMP_PORTATIL_ALUNO` | Portáteis para alunos | Parte do indicador de computadores |

**Regra aplicada nas consultas:** valor 1 = sim, 0 = não, `NULL` = desconhecido/não informado; verificar na documentação oficial se a semântica coincide com a transformação do importador. Não inferir causas ou tendências de apenas um ano.
