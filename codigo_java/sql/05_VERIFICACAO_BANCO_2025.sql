-- EDUCADATA - CONSULTAS DE AUDITORIA E INTEGRIDADE (somente leitura)
-- Execute sobre dados/educadata_inep2025.sqlite (NÃO no banco fictício).
-- Estes testes NÃO substituem a conferência do dicionário INEP.

-- 1. Integridade estrutural do SQLite
PRAGMA integrity_check;
PRAGMA foreign_key_check;

-- 2. Conferência de origem e totais
SELECT ano_referencia, fonte, origem_dados FROM censo ORDER BY ano_referencia;
SELECT 'escolas' tipo, COUNT(*) total FROM escola
UNION ALL SELECT 'registros_2025', COUNT(*) FROM registro_escolar WHERE ano_referencia=2025
UNION ALL SELECT 'fichas_infraestrutura', COUNT(*) FROM infraestrutura
UNION ALL SELECT 'municipios', COUNT(*) FROM municipio;

-- 3. Registros anuais sem ficha (esperado zero para registros importados)
SELECT COUNT(*) AS registros_sem_infraestrutura
FROM registro_escolar r LEFT JOIN infraestrutura i ON i.id_registro = r.id_registro
WHERE r.ano_referencia = 2025 AND i.id_registro IS NULL;

-- 4. Escolas com registros duplicados no mesmo ano (esperado zero)
SELECT COUNT(*) AS grupos_duplicados FROM (
  SELECT id_escola, ano_referencia FROM registro_escolar
  GROUP BY id_escola, ano_referencia HAVING COUNT(*) > 1
);

-- 5. Indicadores por localização, com contagem explícita de desconhecidos
SELECT r.localizacao, COUNT(*) AS registros,
  COUNT(i.internet) AS respostas_validas,
  COUNT(*)-COUNT(i.internet) AS desconhecidos,
  SUM(CASE WHEN i.internet=1 THEN 1 ELSE 0 END) AS com_internet,
  SUM(CASE WHEN i.internet=0 THEN 1 ELSE 0 END) AS sem_internet,
  ROUND(100.0 * SUM(CASE WHEN i.internet=1 THEN 1 ELSE 0 END)
       / NULLIF(COUNT(i.internet),0), 1) AS pct_com_internet
FROM registro_escolar r
LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
WHERE r.ano_referencia=2025
GROUP BY r.localizacao ORDER BY r.localizacao;

-- 6. Indicador específico de internet para alunos, por rede
SELECT r.dependencia, COUNT(*) AS registros,
  COUNT(i.internet_alunos) AS respostas_validas,
  COUNT(*)-COUNT(i.internet_alunos) AS desconhecidos,
  SUM(CASE WHEN i.internet_alunos=1 THEN 1 ELSE 0 END) AS sim,
  ROUND(100.0*SUM(CASE WHEN i.internet_alunos=1 THEN 1 ELSE 0 END)
       / NULLIF(COUNT(i.internet_alunos),0),1) AS percentual
FROM registro_escolar r
LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
WHERE r.ano_referencia=2025
GROUP BY r.dependencia ORDER BY r.dependencia;

-- 7. Valores inesperados: devem ser zero (os CHECKs restringem as colunas)
SELECT COUNT(*) AS valores_invalidos_internet FROM infraestrutura
WHERE internet NOT IN (0,1) OR internet_alunos NOT IN (0,1)
   OR lousa_digital NOT IN (0,1) OR laboratorio_info NOT IN (0,1);
