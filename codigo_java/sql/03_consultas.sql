-- IA: ChatGPT auxiliou na preparacao/revisao do projeto. Link publico: pendente em documentacao/DECLARACAO_USO_IA.md.
-- EDUCADATA - CONSULTAS DIDÁTICAS (2025)
-- Os resultados só demonstram a lógica, porque a base de exemplo é fictícia.

-- 1. Escolas de 2025 com localização, rede e infraestrutura.
SELECT e.nome AS escola, m.nome AS municipio, m.uf,
       r.localizacao, r.dependencia,
       CASE i.internet WHEN 1 THEN 'Sim' WHEN 0 THEN 'Não' ELSE 'Não informado' END AS internet
FROM registro_escolar r
JOIN escola e ON e.id_escola = r.id_escola
JOIN municipio m ON m.id_municipio = e.id_municipio
LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025
ORDER BY m.nome, e.nome;

-- 2. Escolas que informaram NÃO ter acesso à internet.
SELECT e.nome AS escola, m.nome AS municipio, r.localizacao
FROM registro_escolar r
JOIN escola e ON e.id_escola = r.id_escola
JOIN municipio m ON m.id_municipio = e.id_municipio
JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025 AND i.internet = 0
ORDER BY municipio, escola;

-- 3. Percentual de escolas com internet por localização.
-- O denominador inclui apenas respostas 0 ou 1, nunca NULL.
SELECT r.localizacao,
       COUNT(*) AS registros,
       COUNT(i.internet) AS respostas_validas,
       SUM(CASE WHEN i.internet = 1 THEN 1 ELSE 0 END) AS com_internet,
       ROUND(100.0 * SUM(CASE WHEN i.internet = 1 THEN 1 ELSE 0 END)
             / NULLIF(COUNT(i.internet), 0), 1) AS percentual
FROM registro_escolar r
LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025
GROUP BY r.localizacao
ORDER BY r.localizacao;

-- 4. Percentual de internet disponível para alunos por dependência.
SELECT r.dependencia,
       COUNT(i.internet_alunos) AS respostas_validas,
       SUM(CASE WHEN i.internet_alunos = 1 THEN 1 ELSE 0 END) AS com_internet_alunos,
       ROUND(100.0 * SUM(CASE WHEN i.internet_alunos = 1 THEN 1 ELSE 0 END)
             / NULLIF(COUNT(i.internet_alunos), 0), 1) AS percentual
FROM registro_escolar r
LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025
GROUP BY r.dependencia
ORDER BY r.dependencia;

-- 5. Escolas sem internet por município (proporção das respostas conhecidas).
SELECT m.nome AS municipio, m.uf,
       COUNT(i.internet) AS respostas_validas,
       SUM(CASE WHEN i.internet = 0 THEN 1 ELSE 0 END) AS sem_internet,
       ROUND(100.0 * SUM(CASE WHEN i.internet = 0 THEN 1 ELSE 0 END)
             / NULLIF(COUNT(i.internet), 0), 1) AS percentual_sem_internet
FROM registro_escolar r
JOIN escola e ON e.id_escola = r.id_escola
JOIN municipio m ON m.id_municipio = e.id_municipio
LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025
GROUP BY m.id_municipio
ORDER BY percentual_sem_internet DESC, m.nome;

-- 6. Comparar recursos por escola no ano de 2025.
SELECT e.nome, r.localizacao, r.dependencia,
       i.computadores_alunos, i.lousa_digital, i.projetor, i.laboratorio_info
FROM registro_escolar r
JOIN escola e ON e.id_escola = r.id_escola
LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025
ORDER BY e.nome;
