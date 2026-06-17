# Gramática Livre de Contexto - PIX Script

Esta gramática descreve a versão implementada no projeto Java com JFlex, JCup e JUnit.

```bnf
<programa> ::= LEDGER ID <lista_comandos> CLOSE

<lista_comandos> ::= <lista_comandos> <comando> | ε

<comando> ::= <declaracao>
            | <atribuicao>
            | <saida>
            | <condicional>

<declaracao> ::= LET <tipo> ID
               | LET <tipo> ID <op_atribuicao> <expressao>

<atribuicao> ::= <variavel> <op_atribuicao> <expressao>

<op_atribuicao> ::= <- | =

<saida> ::= $> <expressao>

<condicional> ::= IF ( <expressao> ) <bloco>
                | IF ( <expressao> ) <bloco> :: <bloco>

<bloco> ::= { <lista_comandos> }

<tipo> ::= $ | # | @ | ? | ! | ~

<variavel> ::= <tipo> ID

<expressao> ::= <logico_or>
<logico_or> ::= <logico_or> || <logico_and> | <logico_and>
<logico_and> ::= <logico_and> && <igualdade> | <igualdade>
<igualdade> ::= <igualdade> == <relacional>
              | <igualdade> != <relacional>
              | <relacional>
<relacional> ::= <relacional> >> <aritmetica>
               | <relacional> << <aritmetica>
               | <relacional> >= <aritmetica>
               | <relacional> <= <aritmetica>
               | <aritmetica>
<aritmetica> ::= <aritmetica> ++ <termo>
               | <aritmetica> -- <termo>
               | <termo>
<termo> ::= <termo> ** <unario>
          | <termo> // <unario>
          | <termo> %% <unario>
          | <unario>
<unario> ::= !! <unario> | <fator>
<fator> ::= ( <expressao> ) | <variavel> | <literal>

<literal> ::= INTEIRO | DECIMAL | TEXTO | CHAVE_PIX | TRUE | FALSE
```

Observação: o enunciado apresenta a atribuição com `<-`, mas o exemplo completo da página 7 usa `=`. Por isso, o analisador aceita os dois operadores.
