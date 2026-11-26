-- EDUCADATA - FASE 02 - MODELO LÓGICO
-- PROJETO DIDÁTICO: os nomes de atributos devem ser validados com o dicionário oficial INEP.
-- Executar com SQLite (chaves estrangeiras ativadas por conexão).
PRAGMA foreign_keys = ON;

-- Um município pertence a uma UF; usamos UF como atributo, como no modelo conceitual.
CREATE TABLE IF NOT EXISTS municipio (
    id_municipio INTEGER PRIMARY KEY,
    nome         TEXT NOT NULL,
    uf           TEXT NOT NULL CHECK (length(uf) = 2),
    codigo_ibge  TEXT UNIQUE,
    UNIQUE (nome, uf)
);

-- Cada escola pertence a um município. Código INEP pode estar indisponível na base pública.
CREATE TABLE IF NOT EXISTS escola (
    id_escola     INTEGER PRIMARY KEY,
    codigo_inep   TEXT UNIQUE,
    nome          TEXT NOT NULL,
    id_municipio  INTEGER NOT NULL,
    FOREIGN KEY (id_municipio) REFERENCES municipio(id_municipio)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Cada edição do Censo possui um ano e uma informação explícita de origem.
CREATE TABLE IF NOT EXISTS censo (
    ano_referencia INTEGER PRIMARY KEY CHECK (ano_referencia BETWEEN 1990 AND 2100),
    fonte          TEXT NOT NULL,
    origem_dados   TEXT NOT NULL CHECK (origem_dados IN ('FICTICIOS', 'OFICIAIS'))
);

-- Histórico: uma mesma escola só pode ter um registro por ano.
CREATE TABLE IF NOT EXISTS registro_escolar (
    id_registro    INTEGER PRIMARY KEY,
    id_escola      INTEGER NOT NULL,
    ano_referencia INTEGER NOT NULL,
    localizacao    TEXT NOT NULL CHECK (localizacao IN ('Urbana', 'Rural')),
    dependencia    TEXT NOT NULL CHECK (dependencia IN ('Municipal', 'Estadual', 'Federal', 'Privada')),
    UNIQUE (id_escola, ano_referencia),
    FOREIGN KEY (id_escola) REFERENCES escola(id_escola)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (ano_referencia) REFERENCES censo(ano_referencia)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Indicadores booleanos: 1 = sim, 0 = não, NULL = desconhecido/não informado.
-- Não confundir NULL com 0.
CREATE TABLE IF NOT EXISTS infraestrutura (
    id_registro        INTEGER PRIMARY KEY,
    internet           INTEGER CHECK (internet IN (0, 1)),
    internet_alunos    INTEGER CHECK (internet_alunos IN (0, 1)),
    computadores_alunos INTEGER CHECK (computadores_alunos >= 0),
    lousa_digital      INTEGER CHECK (lousa_digital IN (0, 1)),
    projetor           INTEGER CHECK (projetor IN (0, 1)),
    laboratorio_info   INTEGER CHECK (laboratorio_info IN (0, 1)),
    FOREIGN KEY (id_registro) REFERENCES registro_escolar(id_registro)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_escola_municipio ON escola(id_municipio);
CREATE INDEX IF NOT EXISTS idx_registro_ano ON registro_escolar(ano_referencia);
CREATE INDEX IF NOT EXISTS idx_registro_localizacao ON registro_escolar(localizacao);
CREATE INDEX IF NOT EXISTS idx_registro_dependencia ON registro_escolar(dependencia);
