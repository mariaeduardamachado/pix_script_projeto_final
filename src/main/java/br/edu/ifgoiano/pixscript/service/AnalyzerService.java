package br.edu.ifgoiano.pixscript.service;

import br.edu.ifgoiano.pixscript.ast.AstNode;
import br.edu.ifgoiano.pixscript.db.DatabaseManager;
import br.edu.ifgoiano.pixscript.errors.ErrorLogger;
import br.edu.ifgoiano.pixscript.lexer.PixLexer;
import br.edu.ifgoiano.pixscript.model.AnalysisResult;
import br.edu.ifgoiano.pixscript.parser.PixParser;
import br.edu.ifgoiano.pixscript.symbols.SymbolTable;
import br.edu.ifgoiano.pixscript.tree.TreeImageGenerator;
import java_cup.runtime.Symbol;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AnalyzerService {
    private final Path outputDir;
    private final Path databasePath;

    public AnalyzerService() {
        this(Path.of("target", "saida"), Path.of("target", "db", "pixscript"));
    }

    public AnalyzerService(Path outputDir, Path databasePath) {
        this.outputDir = outputDir;
        this.databasePath = databasePath;
    }

    public AnalysisResult analyze(Path sourceFile) throws Exception {
        SymbolTable symbolTable = new SymbolTable();
        ErrorLogger errorLogger = new ErrorLogger();
        AstNode ast = null;

        try (BufferedReader reader = Files.newBufferedReader(sourceFile, StandardCharsets.UTF_8)) {
            PixLexer lexer = new PixLexer(reader, symbolTable, errorLogger);
            PixParser parser = new PixParser(lexer);
            parser.setErrorLogger(errorLogger);
            Symbol parsed = parser.parse();
            if (parsed != null && parsed.value instanceof AstNode node) {
                ast = node;
            }
        } catch (Exception ex) {
            if (!errorLogger.hasErrors()) {
                errorLogger.syntactic("Falha durante a análise: " + ex.getMessage(), 0, 0);
            }
        }

        String baseName = sanitize(sourceFile.getFileName().toString().replaceFirst("\\.[^.]+$", ""));
        Path dotFile = outputDir.resolve(baseName + "_arvore.dot");
        Path pngFile = outputDir.resolve(baseName + "_arvore.png");
        if (ast != null && !errorLogger.hasErrors()) {
            TreeImageGenerator generator = new TreeImageGenerator();
            generator.saveDot(ast, dotFile);
            generator.savePng(ast, pngFile);
        }

        long codeInfoId;
        try (DatabaseManager database = new DatabaseManager(databasePath)) {
            codeInfoId = database.insertCodeInfo(sourceFile.getFileName().toString());
            database.insertSymbols(codeInfoId, symbolTable.entries());
            database.insertErrors(codeInfoId, errorLogger.getErrors());
        }

        return new AnalysisResult(!errorLogger.hasErrors(), ast, symbolTable.entries(), errorLogger.getErrors(), dotFile, pngFile, codeInfoId);
    }

    private String sanitize(String name) {
        return name.replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
