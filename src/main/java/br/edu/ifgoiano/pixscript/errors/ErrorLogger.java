package br.edu.ifgoiano.pixscript.errors;

import br.edu.ifgoiano.pixscript.model.ErrorEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorLogger {
    private final List<ErrorEntry> errors = new ArrayList<>();

    public void lexical(String description, int line, int column) {
        add(description, line, column, "LEXICO");
    }

    public void syntactic(String description, int line, int column) {
        add(description, line, column, "SINTATICO");
    }

    public void semantic(String description, int line, int column) {
        add(description, line, column, "SEMANTICO");
    }

    public void add(String description, int line, int column, String type) {
        errors.add(new ErrorEntry(description, Math.max(line, 0), Math.max(column, 0), type));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ErrorEntry> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
