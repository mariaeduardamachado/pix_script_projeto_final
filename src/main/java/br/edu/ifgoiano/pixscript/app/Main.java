package br.edu.ifgoiano.pixscript.app;

import br.edu.ifgoiano.pixscript.model.AnalysisResult;
import br.edu.ifgoiano.pixscript.model.ErrorEntry;
import br.edu.ifgoiano.pixscript.model.SymbolEntry;
import br.edu.ifgoiano.pixscript.service.AnalyzerService;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: mvn exec:java -Dexec.args=\"exemplos/valido.pix\"");
            System.out.println("Ou:  java -jar target/pix-script-compiladores-1.0.0.jar exemplos/valido.pix");
            return;
        }

        Path input = Path.of(args[0]);
        if (!Files.exists(input)) {
            System.out.println("Arquivo não encontrado: " + input.toAbsolutePath());
            return;
        }

        AnalysisResult result = new AnalyzerService().analyze(input);

        System.out.println("======================================");
        System.out.println("PIX SCRIPT - ANALISADOR");
        System.out.println("======================================");
        System.out.println("Arquivo: " + input.toAbsolutePath());
        System.out.println("Código gravado no banco com ID: " + result.getCodeInfoId());
        System.out.println("Resultado: " + (result.isValid() ? "CÓDIGO VÁLIDO" : "CÓDIGO INVÁLIDO"));

        System.out.println("\nTabela de símbolos:");
        for (SymbolEntry entry : result.getSymbols()) {
            System.out.printf("  %-20s %-18s linha=%d coluna=%d%n", entry.lexeme(), entry.token(), entry.line(), entry.column());
        }

        if (result.getErrors().isEmpty()) {
            System.out.println("\nNenhum erro encontrado.");
            System.out.println("Árvore DOT: " + result.getDotFile().toAbsolutePath());
            System.out.println("Árvore PNG: " + result.getPngFile().toAbsolutePath());
        } else {
            System.out.println("\nLog de erros:");
            for (ErrorEntry error : result.getErrors()) {
                System.out.printf("  [%s] linha=%d coluna=%d - %s%n", error.type(), error.line(), error.column(), error.description());
            }
        }
    }
}
