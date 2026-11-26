# Exploração preliminar — problemas e tratamento

Problemas identificados ou tratados na rotina de importação (resultados do console, não auditoria independente):

- **Grande volume:** 214.192 linhas lidas; importador Java organiza registros em SQLite.
- **Escopo heterogêneo:** 42.454 registros fora das redes públicas consideradas e 33.652 inativos, segundo a rotina; selecionamos redes municipal, estadual ou federal em funcionamento.
- **Registros inválidos/duplicados:** a rotina registrou 1 ocorrência nessa categoria; o caso precisa ser investigado no log original caso se deseje identificar a causa exata.
- **Respostas ausentes:** `NULL` não deve virar automaticamente 0; percentual usa somente respostas conhecidas.
- **Indicadores diferentes:** internet geral não deve ser confundida com internet disponível a alunos.
- **Campos ainda sem mapeamento inequívoco:** projetor e outros recursos precisam ser confrontados com o dicionário de variáveis do pacote 2025.

Somatório reportado: 138.085 incluídos + 42.454 outras redes + 33.652 inativos + 1 inválido/duplicado = 214.192 linhas. Essa partição representa a classificação operacional do importador.
