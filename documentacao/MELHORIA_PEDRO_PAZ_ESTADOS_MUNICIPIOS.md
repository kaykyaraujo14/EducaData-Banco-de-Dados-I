# Melhoria sugerida pelo professor — 24/11/2026

## O que mudou no programa

- Nova **opção 9**: compara estados pelas escolas com internet ausente, mostrando total de escolas, respostas válidas, registros sem internet e desconhecidos.
- Nova **opção 10**: escolha uma UF, veja um ranking dos 20 municípios com maior proporção de escolas sem internet e, opcionalmente, selecione um município para detalhar a situação urbana/rural.
- Ambas as funções operam sobre o banco já importado, sem mudar a estrutura ou os dados.
- O mínimo de respostas válidas por município é 10 por padrão; a equipe pode alterar esse filtro.

## Como demonstrar na apresentação

1. Execute `AppOficial.java` no IntelliJ com `dados/educadata_inep2025.sqlite` disponível.
2. Use a opção 9 e comente os indicadores por UF.
3. Use a opção 10 com `PE`, pressione Enter para manter o mínimo 10 e leia o ranking dos municípios pernambucanos.
4. Selecione um ID para o detalhamento urbano/rural.
5. Apresente os resultados registrados em [VALIDACAO_FINAL.md](../evidencias/VALIDACAO_FINAL.md), citando o recorte utilizado.

O termo "município mais carente" refere-se apenas ao **indicador de escolas sem internet**, não a qualquer índice geral de pobreza. Os resultados foram registrados após execução pela equipe e estão incorporados aos materiais revisados.

**Integrantes:** Kayky Pereira de Araujo e Caique Henrique Da Silva Mendonça.
