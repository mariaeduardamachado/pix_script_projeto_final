# Projeto PIX Script - Java + JFlex + JCup + JUnit

Projeto da disciplina de Compiladores para validar códigos escritos na linguagem PIX Script.

## O que este projeto atende

- Linguagem implementada em **Java**.
- Analisador léxico feito com **JFlex**: `src/main/jflex/PixLexer.flex`.
- Analisador sintático feito com **JCup**: `src/main/cup/parser.cup`.
- Testes automatizados com **JUnit 5**: `src/test/java`.
- Sistema de log de erros léxicos e sintáticos.
- Tabela de símbolos sem repetição de lexemas.
- Gravação da tabela de símbolos e dos erros em banco H2.
- Geração da árvore de derivação em `.dot` e `.png`.
- Exemplos de código válido e inválido.

## Pré-requisitos

Instale:

- Java JDK 17 ou superior;
- Maven 3.8 ou superior.

Para conferir:

```bash
java -version
mvn -version
```

## Como executar os testes JUnit

Na pasta raiz do projeto:

```bash
mvn clean test
```

Esse comando faz o Maven gerar automaticamente o lexer pelo JFlex, gerar o parser pelo JCup e depois executar os testes JUnit.

## Como executar um exemplo válido

```bash
mvn exec:java -Dexec.args="exemplos/valido.pix"
```

Também existe um exemplo que usa `=` como no exemplo final do enunciado:

```bash
mvn exec:java -Dexec.args="exemplos/valido_com_igual.pix"
```

## Como executar um exemplo inválido

```bash
mvn exec:java -Dexec.args="exemplos/invalido.pix"
```

## Saídas geradas

Depois da execução, os arquivos ficam em:

```text
target/saida/<nome>_arvore.dot
target/saida/<nome>_arvore.png
target/db/pixscript.mv.db
```

A árvore `.dot` pode ser aberta com Graphviz. A imagem `.png` é gerada automaticamente pelo próprio Java.

## Banco de dados

O banco H2 fica em `target/db/pixscript.mv.db`.

As tabelas criadas são:

- `codeinfo`: informações do arquivo analisado;
- `symbols`: lexemas/tokens sem repetição;
- `errorlog`: erros léxicos e sintáticos encontrados.

O modelo SQL está em `docs/modelo_banco.sql`.

## Observação sobre o operador de atribuição

O enunciado mostra o operador de atribuição como `<-`, porém o exemplo completo do final usa `=`. Para evitar problema na apresentação, o analisador aceita os dois formatos.
