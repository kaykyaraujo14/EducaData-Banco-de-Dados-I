# Dicionário inicial — campos de referência

**Registro do dicionário inicial da Fase 01.** A conferência posterior das 14 variáveis está documentada em [CONFERIR_DICIONARIO_OFICIAL.md](../codigo_java/docs/CONFERIR_DICIONARIO_OFICIAL.md).

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

**Regra aplicada nas consultas:** valor 1 = sim, 0 = não, `NULL` = desconhecido/não informado; as categorias 0/1 foram conferidas no XLSX fornecido pela equipe; outros valores são tratados conservadoramente como desconhecidos. Não inferir causas ou tendências de apenas um ano.
