package br.edu.ifgoiano.pixscript;

import br.edu.ifgoiano.pixscript.model.AnalysisResult;
import br.edu.ifgoiano.pixscript.service.AnalyzerService;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PixScriptAnalyzerTest {

    @Test
    void deveAceitarCodigoValidoComAtribuicaoSeta() throws Exception {
        AnalyzerService service = new AnalyzerService(Path.of("target", "test-saida"), Path.of("target", "test-db", "pixscript"));
        AnalysisResult result = service.analyze(Path.of("exemplos", "valido.pix"));

        assertTrue(result.isValid(), "O código válido não deveria gerar erros.");
        assertNotNull(result.getAst(), "A árvore de derivação deve ser criada.");
        assertTrue(result.getSymbols().stream().anyMatch(s -> s.lexeme().equals("valor")));
    }

    @Test
    void deveAceitarCodigoValidoComAtribuicaoIgual() throws Exception {
        AnalyzerService service = new AnalyzerService(Path.of("target", "test-saida"), Path.of("target", "test-db", "pixscript"));
        AnalysisResult result = service.analyze(Path.of("exemplos", "valido_com_igual.pix"));

        assertTrue(result.isValid(), "O exemplo final do enunciado usa =; por isso o analisador aceita = e <-.");
    }

    @Test
    void deveRegistrarErroSintatico() throws Exception {
        AnalyzerService service = new AnalyzerService(Path.of("target", "test-saida"), Path.of("target", "test-db", "pixscript"));
        AnalysisResult result = service.analyze(Path.of("exemplos", "invalido.pix"));

        assertFalse(result.isValid(), "O código inválido deve ser recusado.");
        assertFalse(result.getErrors().isEmpty(), "O log de erros deve receber pelo menos um erro.");
    }

    @Test
    void tabelaDeSimbolosNaoPodeRepetirLexema() throws Exception {
        AnalyzerService service = new AnalyzerService(Path.of("target", "test-saida"), Path.of("target", "test-db", "pixscript"));
        AnalysisResult result = service.analyze(Path.of("exemplos", "valido.pix"));

        long ocorrenciasDoLexemaLet = result.getSymbols().stream()
                .filter(s -> s.lexeme().equals("LET"))
                .count();
        assertEquals(1, ocorrenciasDoLexemaLet);
    }
}
