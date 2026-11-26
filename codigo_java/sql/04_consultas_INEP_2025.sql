-- IA: ChatGPT auxiliou na preparacao/revisao do projeto. Link publico: pendente em documentacao/DECLARACAO_USO_IA.md.
-- Consultas analíticas da edição 2025.
-- EXECUTAR APENAS na base dados/educadata_inep2025.sqlite depois da importação.
-- Calcular percentuais somente entre respostas válidas (0 ou 1).

-- 1) Quantidade de escolas públicas em funcionamento por UF
SELECT m.uf, COUNT(*) AS escolas
FROM registro_escolar r
JOIN escola e ON e.id_escola = r.id_escola
JOIN municipio m ON m.id_municipio = e.id_municipio
WHERE r.ano_referencia = 2025
GROUP BY m.uf ORDER BY escolas DESC;

-- 2) Internet geral: urbano / rural
SELECT r.localizacao,
       COUNT(*) AS registros,
       COUNT(i.internet) AS respostas_validas,
       SUM(CASE WHEN i.internet = 1 THEN 1 ELSE 0 END) AS com_internet,
       ROUND(100.0 * SUM(CASE WHEN i.internet = 1 THEN 1 ELSE 0 END)
            / NULLIF(COUNT(i.internet), 0), 2) AS percentual_com_internet
FROM registro_escolar r JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025 GROUP BY r.localizacao;

-- 3) Internet para alunos: rede administrativa
SELECT r.dependencia,
       COUNT(i.internet_alunos) AS respostas_validas,
       SUM(CASE WHEN i.internet_alunos=1 THEN 1 ELSE 0 END) AS com_internet_alunos,
       ROUND(100.0 * SUM(CASE WHEN i.internet_alunos=1 THEN 1 ELSE 0 END)
            / NULLIF(COUNT(i.internet_alunos), 0), 2) AS percentual
FROM registro_escolar r JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025 GROUP BY r.dependencia;

-- 4) Municípios de PE com maior percentual SEM internet, mínimo de 10 respostas válidas
-- O mínimo de respostas evita interpretar municípios com 1 resposta como evidência forte.
SELECT m.nome, m.uf, COUNT(i.internet) AS respostas_validas,
       SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
       ROUND(100.0 * SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
            / NULLIF(COUNT(i.internet), 0), 2) AS percentual_sem_internet
FROM registro_escolar r
JOIN escola e ON e.id_escola=r.id_escola
JOIN municipio m ON m.id_municipio=e.id_municipio
JOIN infraestrutura i ON i.id_registro=r.id_registro
WHERE r.ano_referencia=2025 AND m.uf='PE'
GROUP BY m.id_municipio HAVING COUNT(i.internet)>=10
ORDER BY percentual_sem_internet DESC LIMIT 30;

-- 5) Qualidade: desconhecidos por indicador
SELECT COUNT(*) AS escolas,
       SUM(i.internet IS NULL) AS internet_nao_informada,
       SUM(i.internet_alunos IS NULL) AS internet_alunos_nao_informada,
       SUM(i.computadores_alunos IS NULL) AS qtd_computadores_nao_informada
FROM registro_escolar r JOIN infraestrutura i ON i.id_registro=r.id_registro
WHERE r.ano_referencia=2025;

-- 6) Integridade e procedência: nenhum registro fictício deverá estar na base oficial
SELECT ano_referencia, origem_dados, fonte FROM censo;
PRAGMA foreign_key_check;
