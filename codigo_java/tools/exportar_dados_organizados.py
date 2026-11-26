"""Exporta a base organizada do EducaData, sem alterar o SQLite.
Uso no Windows: python tools/exportar_dados_organizados.py dados/educadata_inep2025.sqlite
Gera CSV completo na pasta dados_organizados/; evite upload de arquivo grande.
Apoio de IA: ChatGPT; documentar link em DECLARACAO_USO_IA.md.
"""
from pathlib import Path
import sqlite3,csv,sys

if len(sys.argv) < 2:
    print('Uso: python tools/exportar_dados_organizados.py dados/educadata_inep2025.sqlite')
    raise SystemExit(1)
base=Path(sys.argv[1])
if not base.is_file():
    print(f'Banco nao encontrado: {base}')
    raise SystemExit(1)
output=Path('dados_organizados');output.mkdir(exist_ok=True)
file=output/'completo_escolas_publicas_2025.csv'
sql="""SELECT e.codigo_inep, e.nome AS escola, m.nome AS municipio, m.uf,
       r.ano_referencia, r.localizacao, r.dependencia,
       i.internet, i.internet_alunos, i.computadores_alunos,
       i.lousa_digital, i.projetor, i.laboratorio_info
       FROM registro_escolar r JOIN escola e ON e.id_escola=r.id_escola
       JOIN municipio m ON m.id_municipio=e.id_municipio
       LEFT JOIN infraestrutura i ON i.id_registro=r.id_registro
       WHERE r.ano_referencia=2025 ORDER BY m.uf, m.nome, e.nome"""
con=sqlite3.connect(f'file:{base.resolve().as_posix()}?mode=ro',uri=True)
count=0
try:
    cur=con.execute(sql)
    with file.open('w',encoding='utf-8-sig',newline='') as f:
        wr=csv.writer(f,delimiter=';');wr.writerow([d[0] for d in cur.description])
        for row in cur:
            wr.writerow(row);count+=1
finally:con.close()
print(f'Exportadas {count} linhas para {file.resolve()}')
print('Conferir se o total e 138085, e validar com o dicionario do INEP.')
