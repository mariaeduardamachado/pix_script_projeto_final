# Relatório Técnico - Projeto PIX Script

## 1. Objetivo

O objetivo do projeto é implementar um analisador léxico e sintático para a linguagem PIX Script, conforme o enunciado da disciplina de Compiladores. A linguagem é sensível a maiúsculas e minúsculas, possui tipos próprios representados por símbolos e permite declarações, atribuições, comando de saída, expressões aritméticas, expressões relacionais, expressões lógicas e estrutura condicional.

## 2. Tecnologias utilizadas

O projeto foi desenvolvido em Java com Maven. O analisador léxico é definido em JFlex, o analisador sintático é definido em JCup e os testes automatizados usam JUnit 5. O banco de dados usado é H2 em modo arquivo, acessado por JDBC.

## 3. Analisador léxico

As regras léxicas estão no arquivo `src/main/jflex/PixLexer.flex`. O lexer reconhece palavras reservadas, tipos, identificadores, literais, operadores aritméticos, operadores relacionais, operadores lógicos, delimitadores e comando de saída. Cada lexema reconhecido é inserido em uma tabela de símbolos, sem repetição.

## 4. Analisador sintático

A gramática sintática está no arquivo `src/main/cup/parser.cup`. O parser valida a estrutura geral do programa, que deve iniciar com `LEDGER <nome>` e terminar com `CLOSE`. Também valida comandos internos, blocos condicionais e precedência de operadores.

## 5. Log de erros

Os erros são registrados pela classe `ErrorLogger`. Erros léxicos são detectados pelo lexer e erros sintáticos são detectados pelo parser. Ao final da execução, o log é gravado na tabela `errorlog` do banco de dados.

## 6. Tabela de símbolos

A tabela de símbolos é montada pela classe `SymbolTable`. Foi usado um `LinkedHashMap` para impedir repetição de lexemas e manter a ordem de primeira ocorrência. Ao final da análise, a tabela é gravada na tabela `symbols` do banco de dados.

## 7. Banco de dados

O banco possui três tabelas principais: `codeinfo`, `symbols` e `errorlog`. A tabela `codeinfo` registra o arquivo analisado, data e hora. A tabela `symbols` recebe os lexemas/tokens do código. A tabela `errorlog` recebe os erros encontrados.

## 8. Árvore de derivação

Quando o código é válido, o parser constrói uma árvore de derivação usando a classe `AstNode`. O projeto gera dois arquivos de saída: um `.dot`, compatível com Graphviz, e uma imagem `.png` gerada em Java.

## 9. Testes automatizados

Os testes estão em `src/test/java`. Eles verificam código válido com `<-`, código válido com `=`, código inválido e a regra que impede repetição de lexemas na tabela de símbolos.
