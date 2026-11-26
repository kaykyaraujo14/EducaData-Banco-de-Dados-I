# Dicionário: mapeamento do CSV INEP 2025 → EducaData

**Escopo do importador:** arquivo `Tabela_Escola_2025_V2.csv`, escolas públicas em funcionamento. Os nomes das colunas esperadas deverão ser conferidos com o dicionário da versão baixada. O conteúdo da base não acompanha o projeto.

| Coluna CSV | Tabela/atributo | Tratamento |
|---|---|---|
| `CO_MUNICIPIO` | `municipio.codigo_ibge` | String do código IBGE, único por município |
| `NO_MUNICIPIO` | `municipio.nome` | Nome do município |
| `SG_UF` | `municipio.uf` | Sigla UF de 2 letras |
| `CO_ENTIDADE` | `escola.codigo_inep` | Identificador público da escola, preservado como texto |
| `NO_ENTIDADE` | `escola.nome` | Nome da escola |
| `TP_SITUACAO_FUNCIONAMENTO` | **filtro** | `1` = em atividade; demais não são importadas |
| `TP_DEPENDENCIA` | `registro_escolar.dependencia` | `1` federal, `2` estadual, `3` municipal; `4` privada fica fora |
| `TP_LOCALIZACAO` | `registro_escolar.localizacao` | `1` urbana, `2` rural |
| `IN_INTERNET` | `infraestrutura.internet` | `1` sim, `0` não, outros `NULL` |
| `IN_INTERNET_ALUNOS` | `infraestrutura.internet_alunos` | `1` sim, `0` não, outros `NULL` |
| `QT_DESKTOP_ALUNO` | `infraestrutura.computadores_alunos` | Com `QT_COMP_PORTATIL_ALUNO`: soma das contagens válidas |
| `QT_COMP_PORTATIL_ALUNO` | `infraestrutura.computadores_alunos` | Se um componente não for válido, o total é `NULL` |
| `IN_EQUIP_LOUSA_DIGITAL` | `infraestrutura.lousa_digital` | `1` sim, `0` não, outros `NULL` |
| `IN_LABORATORIO_INFORMATICA` | `infraestrutura.laboratorio_info` | `1` sim, `0` não, outros `NULL` |
| — | `infraestrutura.projetor` | Não preenchido: requer mapeamento conceitual exato |

**Regras:** `NULL` não equivale a `0`; uma linha é rejeitada quando faltar identificador, município, UF, localização ou nome; códigos de escola duplicados são rejeitados; porcentagens usam apenas respostas válidas. O campo `computadores_alunos` não representa todos os dispositivos digitais (tablets não são somados). Se `QT_DESKTOP_ALUNO`/`QT_COMP_PORTATIL_ALUNO` tiver marcador `88888` ou outro valor extremo, será tratado como desconhecido. Confirme as regras na edição do dicionário oficial do arquivo efetivamente baixado.

**Qualidade da informação:** o importador confere nomes de colunas, valores esperados e FKs, mas **não comprova a autenticidade do arquivo CSV**. Essa conferência é responsabilidade da equipe por meio da origem e, se disponível, dos checksums divulgados pelo INEP.


> **Status da revisão (23/11/2026):** a existência das colunas é verificada pelo importador, mas as definições detalhadas, os códigos de respostas especiais e a interpretação de cada indicador **ainda não foram auditados contra o arquivo XLSX de dicionário da edição V2 que está no computador da equipe**. Este é um mapeamento de implementação, não uma certificação oficial dos significados. Consulte `docs/CONFERIR_DICIONARIO_OFICIAL.md`.
