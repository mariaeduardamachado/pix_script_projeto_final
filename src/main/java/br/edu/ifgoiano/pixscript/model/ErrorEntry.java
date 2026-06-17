package br.edu.ifgoiano.pixscript.model;

public record ErrorEntry(String description, int line, int column, String type) {
}
