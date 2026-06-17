package br.edu.ifgoiano.pixscript.symbols;

import br.edu.ifgoiano.pixscript.model.SymbolEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final Map<String, SymbolEntry> symbols = new LinkedHashMap<>();

    public void add(String lexeme, String token, int line, int column) {
        if (lexeme == null || lexeme.isBlank()) {
            return;
        }
        symbols.putIfAbsent(lexeme, new SymbolEntry(lexeme, token, line, column));
    }

    public List<SymbolEntry> entries() {
        return new ArrayList<>(symbols.values());
    }

    public boolean containsLexeme(String lexeme) {
        return symbols.containsKey(lexeme);
    }

    public int size() {
        return symbols.size();
    }
}
