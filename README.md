# Projeto PIX Script — Analisador Léxico e Sintático

## Apresentação do Projeto

Este projeto foi desenvolvido para a disciplina de **Compiladores**, com o objetivo de implementar um analisador para a linguagem fictícia **PIX Script**.

A linguagem PIX Script foi proposta como uma linguagem de programação inspirada no sistema de pagamentos PIX. Ela possui uma estrutura própria, com palavras reservadas, tipos de dados, operadores, comandos de saída, declaração de variáveis e estruturas condicionais.

O principal objetivo do projeto é verificar se um código escrito em PIX Script está correto de acordo com as regras léxicas e sintáticas definidas no enunciado do trabalho.

## Objetivo Geral

Desenvolver um analisador capaz de ler um arquivo contendo código em PIX Script e verificar se ele está válido ou inválido.

Durante a análise, o sistema identifica os tokens da linguagem, valida a estrutura do programa, registra possíveis erros, gera a tabela de símbolos e cria uma árvore de derivação do código analisado.

## Tecnologias Utilizadas

O projeto foi desenvolvido em **Java**, utilizando ferramentas tradicionais da disciplina de Compiladores:

* **Java**: linguagem principal do projeto;
* **JFlex**: utilizado para criar o analisador léxico;
* **JCup**: utilizado para criar o analisador sintático;
* **JUnit**: utilizado para testes automatizados;
* **Maven**: utilizado para organizar, compilar e executar o projeto;
* **Graphviz**: utilizado para gerar a imagem da árvore de derivação;
* **Banco de dados**: utilizado para armazenar informações da tabela de símbolos e logs de erro.

## O que o Analisador Léxico Faz

O analisador léxico é responsável por ler o código-fonte e separar cada parte do programa em tokens.

Ele reconhece, por exemplo:

* palavras reservadas como `LEDGER`, `CLOSE`, `LET`, `IF`, `TRUE` e `FALSE`;
* tipos da linguagem como `$`, `#`, `@`, `?`, `!` e `~`;
* identificadores de variáveis;
* números inteiros e decimais;
* textos entre aspas;
* operadores aritméticos, relacionais e lógicos;
* símbolos como parênteses, chaves e operadores de atribuição.

Quando encontra algum símbolo inválido, o analisador registra o erro no sistema de log.

## O que o Analisador Sintático Faz

O analisador sintático verifica se os tokens encontrados pelo analisador léxico estão organizados corretamente de acordo com a gramática da linguagem PIX Script.

Ele valida estruturas como:

```pix
LEDGER MeuPrograma
    LET @nome <- 'Aluno'
    LET $valor <- 250.50

    IF ($valor >> 100.00) {
        $> 'Valor alto'
    }
    :: {
        $> 'Valor baixo'
    }
CLOSE
```

O programa precisa começar com `LEDGER`, possuir um nome e terminar com `CLOSE`.

Dentro do programa, podem existir declarações de variáveis, atribuições, comandos de saída e estruturas condicionais.

## Gramática Livre de Contexto

A gramática livre de contexto foi criada para representar as principais regras da linguagem PIX Script.

Ela define como um programa deve ser formado, quais comandos são permitidos e como expressões podem ser escritas.

De forma resumida, a estrutura principal da linguagem segue o seguinte modelo:

```txt
programa → LEDGER IDENTIFICADOR comandos CLOSE

comandos → comando comandos | vazio

comando → declaracao
        | atribuicao
        | saida
        | condicional

declaracao → LET tipo IDENTIFICADOR
           | LET tipo IDENTIFICADOR ATRIBUICAO expressao

atribuicao → variavel ATRIBUICAO expressao

saida → SAIDA texto

condicional → IF '(' expressao ')' '{' comandos '}'
            | IF '(' expressao ')' '{' comandos '}' ELSE '{' comandos '}'
```

Essa gramática permite validar se o código segue a estrutura esperada da linguagem.

## Tabela de Símbolos

Durante a análise, o programa gera uma tabela de símbolos contendo os lexemas identificados no código.

A tabela de símbolos armazena informações como:

* lexema;
* token;
* linha;
* coluna;
* código analisado.

Uma regra importante implementada no projeto é que os lexemas não devem se repetir na tabela de símbolos. Assim, caso o mesmo lexema apareça mais de uma vez no código, ele é registrado apenas uma vez.

## Sistema de Log de Erros

O sistema também possui um log de erros para armazenar problemas encontrados durante a análise.

Os erros podem ser:

* erros léxicos, quando aparece um símbolo inválido;
* erros sintáticos, quando a estrutura do código está incorreta;
* erros relacionados à formação dos comandos.

Cada erro registrado contém informações como:

* descrição do erro;
* linha em que ocorreu;
* coluna;
* tipo do erro.

Esses erros também podem ser gravados no banco de dados, conforme solicitado no trabalho.

## Árvore de Derivação

Além da análise léxica e sintática, o projeto também gera uma árvore de derivação.

A árvore de derivação mostra visualmente como o código foi reconhecido pela gramática da linguagem.

Ela é gerada em formato `.dot` e pode ser convertida em imagem utilizando o Graphviz.

Essa parte é importante porque ajuda a demonstrar o funcionamento interno do analisador sintático.

## Como Executar o Projeto

Para executar o projeto, é necessário ter instalado:

* Java JDK;
* Maven;
* Graphviz, caso deseje gerar a imagem da árvore.

Depois de baixar o projeto, abra o terminal dentro da pasta principal e execute:

```bash
mvn clean test
```

Esse comando compila o projeto e executa os testes automatizados com JUnit.

Para analisar um arquivo PIX Script válido, execute:

```bash
mvn exec:java -Dexec.args="exemplos/valido.pix"
```

Para testar um arquivo com erro, execute:

```bash
mvn exec:java -Dexec.args="exemplos/invalido.pix"
```

## Exemplos de Arquivos

O projeto possui exemplos para facilitar a apresentação e os testes.

O arquivo `valido.pix` contém um código correto em PIX Script.

O arquivo `invalido.pix` contém erros propositalmente inseridos para demonstrar o funcionamento do log de erros.

## Testes com JUnit

Foram criados testes automatizados com JUnit para verificar partes importantes do projeto.

Os testes ajudam a confirmar se o analisador reconhece corretamente códigos válidos e identifica problemas em códigos inválidos.

Isso torna o projeto mais confiável e facilita a verificação durante a apresentação.

## Resultado Esperado

Ao executar o programa com um código válido, o sistema deve informar que a análise foi concluída com sucesso.

Também são gerados os registros da tabela de símbolos e a árvore de derivação.

Ao executar com um código inválido, o sistema deve apresentar os erros encontrados e registrar essas informações no log.

## Conclusão

Este projeto permitiu aplicar, na prática, conceitos estudados na disciplina de Compiladores.

Com ele, foi possível desenvolver as etapas básicas de um compilador, incluindo análise léxica, análise sintática, geração de tabela de símbolos, registro de erros e geração de árvore de derivação.

O uso do JFlex, JCup e JUnit ajudou a organizar melhor o projeto e aproximou a implementação de ferramentas utilizadas em projetos reais de análise de linguagens.
