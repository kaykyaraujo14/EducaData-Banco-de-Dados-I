-- EducaData - Banco de Dados I - análise de internet por UF e município.
-- Autor(es): Kayky Pereira de Araujo e Caique Henrique Da Silva Mendonça.
-- Desenvolvido com assistência de IA; verificar dicionário do INEP.
-- EXECUTAR na base SQLite do Censo 2025 já importada; apenas SELECT.
-- "Mais carente" = maior proporção de escolas SEM INTERNET (i.internet=0).
-- Respostas desconhecidas (NULL) não entram na porcentagem.

-- 1. Resumo por estado (UF), ordenado por percentual sem internet.
SELECT m.uf,
       COUNT(*) AS total_escolas,
       COUNT(i.internet) AS respostas_validas,
       SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS escolas_sem_internet,
       COUNT(*) - COUNT(i.internet) AS desconhecidas,
       ROUND(100.0 * SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
           / NULLIF(COUNT(i.internet),0), 1) AS percentual_sem_internet
FROM registro_escolar AS r
JOIN escola AS e ON e.id_escola=r.id_escola
JOIN municipio AS m ON m.id_municipio=e.id_municipio
LEFT JOIN infraestrutura AS i ON i.id_registro=r.id_registro
WHERE r.ano_referencia=2025
GROUP BY m.uf
ORDER BY 1.0*SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END)
          / NULLIF(COUNT(i.internet),0) DESC;

-- 2. Top 20 municípios com maiores % de escolas SEM internet em Pernambuco.
-- Substitua PE pela UF desejada; mude o mínimo 10 conforme necessário.
WITH indicadores AS (
  SELECT m.id_municipio, m.nome, m.uf, COUNT(*) AS total,
         COUNT(i.internet) AS respostas_validas,
         SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
         COUNT(*)-COUNT(i.internet) AS desconhecidas
  FROM registro_escolar AS r
  JOIN escola AS e ON e.id_escola=r.id_escola
  JOIN municipio AS m ON m.id_municipio=e.id_municipio
  LEFT JOIN infraestrutura AS i ON i.id_registro=r.id_registro
  WHERE r.ano_referencia=2025 AND m.uf='PE'
  GROUP BY m.id_municipio, m.nome, m.uf
)
SELECT nome, uf, total, respostas_validas, sem_internet, desconhecidas,
       ROUND(100.0*sem_internet/NULLIF(respostas_validas,0),1) AS percentual_sem_internet
FROM indicadores
WHERE respostas_validas>=10
ORDER BY 1.0*sem_internet/respostas_validas DESC,
         sem_internet DESC, respostas_validas DESC, nome COLLATE NOCASE
LIMIT 20;
