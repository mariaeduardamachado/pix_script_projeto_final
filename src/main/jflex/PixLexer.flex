package br.edu.ifgoiano.pixscript.lexer;

import br.edu.ifgoiano.pixscript.errors.ErrorLogger;
import br.edu.ifgoiano.pixscript.parser.sym;
import br.edu.ifgoiano.pixscript.symbols.SymbolTable;
import java_cup.runtime.Symbol;

%%

%public
%class PixLexer
%unicode
%line
%column
%cup

%{
    private SymbolTable symbolTable;
    private ErrorLogger errorLogger;

    public PixLexer(java.io.Reader reader, SymbolTable symbolTable, ErrorLogger errorLogger) {
        this(reader);
        this.symbolTable = symbolTable;
        this.errorLogger = errorLogger;
    }

    private Symbol token(int type, String tokenName) {
        return token(type, yytext(), tokenName);
    }

    private Symbol token(int type, Object value, String tokenName) {
        if (symbolTable != null) {
            symbolTable.add(yytext(), tokenName, yyline + 1, yycolumn + 1);
        }
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }

    private String removeQuotes(String text) {
        if (text == null || text.length() < 2) {
            return text;
        }
        return text.substring(1, text.length() - 1)
                .replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\t", "\t");
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Identifier = [A-Za-z_][A-Za-z0-9_]*
Integer = [0-9]+
Decimal = [0-9]+\.[0-9]+
SingleText = \'([^\'\\]|\\.)*\'
DoubleText = \"([^\"\\]|\\.)*\"

%%

{WhiteSpace}+              { /* ignora espaços e quebras de linha */ }

"LEDGER"                  { return token(sym.LEDGER, "LEDGER"); }
"CLOSE"                   { return token(sym.CLOSE, "CLOSE"); }
"LET"                     { return token(sym.LET, "LET"); }
"IF"                      { return token(sym.IF, "IF"); }
"TRUE"                    { return token(sym.BOOL_LITERAL, Boolean.TRUE, "BOOL_LITERAL"); }
"FALSE"                   { return token(sym.BOOL_LITERAL, Boolean.FALSE, "BOOL_LITERAL"); }

"$>"                      { return token(sym.PRINT, "PRINT"); }
"<-"                      { return token(sym.ASSIGN, "ASSIGN"); }
"="                       { return token(sym.ASSIGN_EQ, "ASSIGN_EQ"); }
"::"                      { return token(sym.ELSE, "ELSE"); }

"=="                      { return token(sym.EQ, "EQ"); }
"!="                      { return token(sym.NEQ, "NEQ"); }
">="                      { return token(sym.GTE, "GTE"); }
"<="                      { return token(sym.LTE, "LTE"); }
">>"                      { return token(sym.GT, "GT"); }
"<<"                      { return token(sym.LT, "LT"); }

"&&"                      { return token(sym.AND, "AND"); }
"||"                      { return token(sym.OR, "OR"); }
"!!"                      { return token(sym.NOT, "NOT"); }

"++"                      { return token(sym.PLUS, "PLUS"); }
"--"                      { return token(sym.MINUS, "MINUS"); }
"**"                      { return token(sym.TIMES, "TIMES"); }
"//"                      { return token(sym.DIVIDE, "DIVIDE"); }
"%%"                      { return token(sym.MOD, "MOD"); }

"("                       { return token(sym.LPAREN, "LPAREN"); }
")"                       { return token(sym.RPAREN, "RPAREN"); }
"{"                       { return token(sym.LBRACE, "LBRACE"); }
"}"                       { return token(sym.RBRACE, "RBRACE"); }

"$"                       { return token(sym.TYPE_DECIMAL, "TYPE_DECIMAL"); }
"#"                       { return token(sym.TYPE_INT, "TYPE_INT"); }
"@"                       { return token(sym.TYPE_TEXT, "TYPE_TEXT"); }
"?"                       { return token(sym.TYPE_BOOL, "TYPE_BOOL"); }
"!"                       { return token(sym.TYPE_PIX, "TYPE_PIX"); }
"~"                       { return token(sym.TYPE_NULL, "TYPE_NULL"); }

{Decimal}                  { return token(sym.DECIMAL_LITERAL, Double.valueOf(yytext()), "DECIMAL_LITERAL"); }
{Integer}                  { return token(sym.INT_LITERAL, Integer.valueOf(yytext()), "INT_LITERAL"); }
{SingleText}               { return token(sym.TEXT_LITERAL, removeQuotes(yytext()), "TEXT_LITERAL"); }
{DoubleText}               { return token(sym.PIX_LITERAL, removeQuotes(yytext()), "PIX_LITERAL"); }
{Identifier}               { return token(sym.ID, yytext(), "ID"); }

[^]                        {
                                if (errorLogger != null) {
                                    errorLogger.lexical("Caractere inválido: " + yytext(), yyline + 1, yycolumn + 1);
                                }
                                return token(sym.UNKNOWN, yytext(), "UNKNOWN");
                            }
