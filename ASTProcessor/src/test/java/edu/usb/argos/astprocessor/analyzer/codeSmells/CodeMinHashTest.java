package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.CodeMinHashConfig;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.CodeMinHash;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.TokenShingleGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeMinHashTest {

    private final int TOKEN_FREQUENCY = 3;
    private static IShingleGenerator<String> shingleGenerator;

    @BeforeAll
    public static void setup() {
        shingleGenerator = new TokenShingleGenerator();
    }

    @Test
    public void testMinHashSignatureGeneratedFromShingles() {
        IPlainTextHasher textHasher = new SHATextHasher();
        CodeMinHashConfig hashConfig = CodeMinHashConfig.builder()
                .numHashFunctions(100)
                .prime(16777619)
                .build();

        CodeMinHash minHash = new CodeMinHash(hashConfig, textHasher);

        List<String> tokens1 = List.of("FOR", "LPAREN", "INT", "IDENTIFIER", "ASSIGN", "LITERAL");;
        List<String> tokens2 = List.of("FOR", "LPAREN", "INT", "IDENTIFIER", "ASSIGN", "NUMERIC");;

        List<List<String>> shingles1 = shingleGenerator.generate(tokens1, TOKEN_FREQUENCY);
        List<List<String>> shingles2 = shingleGenerator.generate(tokens2, TOKEN_FREQUENCY);

        List<String> shingleStrings1 = shingles1.stream()
                .map(shingle -> String.join("", shingle))
                .collect(Collectors.toList());

        List<String> shingleStrings2 = shingles2.stream()
                .map(shingle -> String.join("", shingle))
                .collect(Collectors.toList());

        List<Integer> signature1 = minHash.computeMinHash(shingleStrings1);
        List<Integer> signature2 = minHash.computeMinHash(shingleStrings2);

        double similarity = minHash.computeSimilarity(signature1, signature2);
        assertTrue(similarity >= 0.4);
    }
}
