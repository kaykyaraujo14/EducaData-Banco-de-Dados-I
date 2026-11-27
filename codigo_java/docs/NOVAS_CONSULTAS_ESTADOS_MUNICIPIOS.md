# Melhoria solicitada pelo professor — Estados e municípios

**Projeto:** EducaData — Banco de Dados I  
**Integrantes:** Kayky Pereira de Araujo e Caique Henrique Da Silva Mendonça.

## Como executar no IntelliJ

1. Antes de alterar seu projeto antigo, faça uma **cópia** da pasta que contém `dados/educadata_inep2025.sqlite`.
2. Abra o projeto Maven atualizado (pasta `codigo_java`, com arquivo `pom.xml`) no IntelliJ.
3. Copie o arquivo original `educadata_inep2025.sqlite` para a pasta `dados` **somente na cópia do projeto**. O repositório não inclui o banco SQLite local.
4. Execute `AppOficial.java` (e **não** `ImportarInep2025.java`, pois seus dados já foram importados).
5. No menu, digite **9** para comparar estados; ou **10** para escolher `PE`, `AC`, `AM` ou outra sigla e ver o ranking municipal.
6. Na opção 10, use **Enter = 10** no mínimo de respostas. Digite o ID de um município do ranking para ver a comparação urbana/rural local, ou pressione Enter para voltar ao menu.

## Como demonstrar ao professor

- Na opção **9**, mostre a diferença percentual entre estados, sem confundir percentuais com números absolutos.
- Na opção **10**, digite `PE` e mostre quais municípios pernambucanos possuem maiores percentuais de escolas sem internet e quantas escolas responderam.
- Depois escolha outro estado para comparar. Explique que a carência é **no indicador internet escolar**, não pobreza geral.
- Os dados **NULL** não significam "sem internet"; aparecem como desconhecidos.

## Onde está o código alterado?

- `src/main/java/br/edu/cesa/educadata/App.java`: adiciona as opções 9 e 10 ao menu.
- `src/main/java/br/edu/cesa/educadata/Consultas.java`: implementa SQL `JOIN`, `GROUP BY`, somas, percentuais e filtros de UF.
- `sql/06_RANKING_ESTADO_MUNICIPIO.sql`: consultas equivalentes para testar no SQLite.

O modelo continua com a UF como atributo da tabela `municipio`, sem criar tabela adicional de estado; isso atende à **divisão analítica por UF e município**. Se o professor exigir explicitamente uma entidade `estado` separada no DER, será uma mudança de modelagem distinta.

**Nota:** as variáveis utilizadas foram conferidas no XLSX fornecido pela equipe. Os resultados das opções 9 e 10 estão registrados em [VALIDACAO_FINAL.md](../../evidencias/VALIDACAO_FINAL.md).
