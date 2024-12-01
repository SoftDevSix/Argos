package edu.usb.argos.astprocessor.analyzer.codeSmells.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.TokenShingleGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenShingleGeneratorTest {

    private static final int TOKEN_FREQUENCY = 10;
    private static IShingleGenerator<String> shingleGenerator;

    @BeforeAll
    public static void setup() {
        shingleGenerator = new TokenShingleGenerator(TOKEN_FREQUENCY);
    }

    private int calculateShinglesGenerates(int elementsSize, int frequency) {
        return elementsSize - frequency + 1;
    }

    @Test
    public void testFrequencySingleGenerator() {
        List<String> tokens = List.of("FOR", "LPAREN", "INT", "IDENTIFIER", "ASSIGN", "LITERAL", "SEMI", "IDENTIFIER", "LT", "LITERAL", "SEMI", "IDENTIFIER", "INC", "RPAREN", "LBRACE");
        List<List<String>> shingles = shingleGenerator.generate(tokens);

        int expectedShinglesGenerated = calculateShinglesGenerates(tokens.size(), TOKEN_FREQUENCY);
        assertEquals(expectedShinglesGenerated, shingles.size());

        shingles.forEach(single -> {
            assertEquals(single.size(), TOKEN_FREQUENCY);
        });
    }
}
