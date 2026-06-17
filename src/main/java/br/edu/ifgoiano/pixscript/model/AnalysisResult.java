package br.edu.ifgoiano.pixscript.model;

import br.edu.ifgoiano.pixscript.ast.AstNode;
import java.nio.file.Path;
import java.util.List;

public class AnalysisResult {
    private final boolean valid;
    private final AstNode ast;
    private final List<SymbolEntry> symbols;
    private final List<ErrorEntry> errors;
    private final Path dotFile;
    private final Path pngFile;
    private final long codeInfoId;

    public AnalysisResult(boolean valid, AstNode ast, List<SymbolEntry> symbols, List<ErrorEntry> errors,
                          Path dotFile, Path pngFile, long codeInfoId) {
        this.valid = valid;
        this.ast = ast;
        this.symbols = symbols;
        this.errors = errors;
        this.dotFile = dotFile;
        this.pngFile = pngFile;
        this.codeInfoId = codeInfoId;
    }

    public boolean isValid() { return valid; }
    public AstNode getAst() { return ast; }
    public List<SymbolEntry> getSymbols() { return symbols; }
    public List<ErrorEntry> getErrors() { return errors; }
    public Path getDotFile() { return dotFile; }
    public Path getPngFile() { return pngFile; }
    public long getCodeInfoId() { return codeInfoId; }
}
