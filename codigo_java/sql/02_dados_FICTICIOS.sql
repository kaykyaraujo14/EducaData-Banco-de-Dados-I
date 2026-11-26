-- ATENÇÃO: DADOS 100% FICTÍCIOS, PARA DEMONSTRAR O FUNCIONAMENTO DO PROJETO.
-- Nomes de municípios reais para orientar testes; escolas e indicadores inventados.
-- Não utilizar para pesquisas nem apresentar como resultados do Inep.
INSERT INTO municipio (id_municipio, nome, uf) VALUES
(1, 'Arcoverde', 'PE'),
(2, 'Recife', 'PE'),
(3, 'Venturosa', 'PE'),
(4, 'Garanhuns', 'PE');

-- codigo_inep = NULL: não inventamos códigos oficiais.
INSERT INTO escola (id_escola, codigo_inep, nome, id_municipio) VALUES
(1, NULL, 'Escola Fictícia Arcoverde A', 1),
(2, NULL, 'Escola Fictícia Arcoverde B', 1),
(3, NULL, 'Escola Fictícia Recife A', 2),
(4, NULL, 'Escola Fictícia Venturosa A', 3),
(5, NULL, 'Escola Fictícia Recife B', 2),
(6, NULL, 'Escola Fictícia Garanhuns A', 4),
(7, NULL, 'Escola Fictícia Venturosa B', 3);

INSERT INTO censo (ano_referencia, fonte, origem_dados) VALUES
(2024, 'Exemplo acadêmico - sem dados oficiais', 'FICTICIOS'),
(2025, 'Exemplo acadêmico - sem dados oficiais', 'FICTICIOS');

INSERT INTO registro_escolar (id_registro, id_escola, ano_referencia, localizacao, dependencia) VALUES
(1, 1, 2025, 'Urbana', 'Municipal'),
(2, 2, 2025, 'Rural', 'Municipal'),
(3, 3, 2025, 'Urbana', 'Estadual'),
(4, 4, 2025, 'Rural', 'Estadual'),
(5, 5, 2025, 'Urbana', 'Federal'),
(6, 6, 2025, 'Urbana', 'Municipal'),
(7, 7, 2025, 'Rural', 'Municipal'),
(8, 1, 2024, 'Urbana', 'Municipal'),
(9, 3, 2024, 'Urbana', 'Estadual');

INSERT INTO infraestrutura
(id_registro, internet, internet_alunos, computadores_alunos, lousa_digital, projetor, laboratorio_info) VALUES
(1, 1, 1, 22, 1, 1, 1),
(2, 0, 0, 0, 0, 0, 0),
(3, 1, 1, 36, 1, 1, 1),
(4, NULL, NULL, NULL, 0, 0, 0),
(5, 1, 1, 65, 1, 1, 1),
(6, 1, 0, 12, 0, 1, 0),
(7, 0, 0, 0, 0, 0, 0),
(8, 1, 0, 10, 0, 1, 0),
(9, 1, NULL, 20, 0, 1, 0);
