# Roteiro de Apresentação - 15 minutos

## Integrante 1 - Introdução e linguagem PIX Script

Apresentar o objetivo do trabalho: criar um analisador para a linguagem PIX Script. Explicar que a linguagem começa com `LEDGER`, termina com `CLOSE`, é tipada, é sensível ao caso e usa símbolos para representar tipos, como `$` para decimal, `#` para inteiro, `@` para texto, `?` para booleano e `!` para chave PIX.

## Integrante 2 - Analisador léxico com JFlex

Explicar o arquivo `PixLexer.flex`. Mostrar que o JFlex reconhece palavras reservadas, operadores, literais, identificadores e delimitadores. Mostrar que cada token reconhecido também é enviado para a tabela de símbolos.

## Integrante 3 - Analisador sintático com JCup

Explicar o arquivo `parser.cup`. Mostrar a regra principal `program ::= LEDGER ID statement_list CLOSE`. Explicar que o parser valida declarações, atribuições, saída e IF/ELSE. Mostrar que o parser também cria a árvore de derivação.

## Integrante 4 - Banco de dados, erros e testes

Apresentar as tabelas `codeinfo`, `symbols` e `errorlog`. Explicar que os símbolos são gravados sem repetição e que os erros são armazenados no banco. Depois, mostrar os testes JUnit executando com `mvn test`.

## Demonstração final

Executar:

```bash
mvn clean test
mvn exec:java -Dexec.args="exemplos/valido.pix"
mvn exec:java -Dexec.args="exemplos/invalido.pix"
```

Mostrar no terminal se o código foi aceito ou rejeitado, a tabela de símbolos, o log de erros e os arquivos gerados em `target/saida`.
