package br.edu.ifgoiano.pixscript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AstNode {
    private final String name;
    private final String value;
    private final List<AstNode> children = new ArrayList<>();

    public AstNode(String name) {
        this(name, null);
    }

    public AstNode(String name, Object value) {
        this.name = name;
        this.value = value == null ? null : String.valueOf(value);
    }

    public AstNode add(AstNode child) {
        if (child != null) {
            children.add(child);
        }
        return this;
    }

    public AstNode addAll(List<AstNode> nodes) {
        if (nodes != null) {
            nodes.forEach(this::add);
        }
        return this;
    }

    public String getName() { return name; }
    public String getValue() { return value; }
    public List<AstNode> getChildren() { return Collections.unmodifiableList(children); }

    public String label() {
        return value == null || value.isBlank() ? name : name + "\\n" + value;
    }

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ArvoreDerivacao {\n");
        sb.append("  graph [rankdir=TB];\n");
        sb.append("  node [shape=box, fontname=\"Arial\"];\n");
        AtomicInteger counter = new AtomicInteger(0);
        buildDot(sb, counter, -1);
        sb.append("}\n");
        return sb.toString();
    }

    private int buildDot(StringBuilder sb, AtomicInteger counter, int parentId) {
        int id = counter.getAndIncrement();
        sb.append("  n").append(id).append(" [label=\"").append(escape(label())).append("\"];\n");
        if (parentId >= 0) {
            sb.append("  n").append(parentId).append(" -> n").append(id).append(";\n");
        }
        for (AstNode child : children) {
            child.buildDot(sb, counter, id);
        }
        return id;
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
