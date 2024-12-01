package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ISimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.JaccardSimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.MinHashingHandler;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.TokenShingleGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinHashingTest {

    private static final int TOKEN_FREQUENCY = 3;
    private static IShingleGenerator<String> shingleGenerator;

    @BeforeAll
    public static void setup() {
        shingleGenerator = new TokenShingleGenerator(TOKEN_FREQUENCY);
    }

    @Test
    public void testMinHashSignatureGeneratedFromShingles() {
        IPlainTextHasher textHasher = new SHATextHasher();
        MinHashConfiguration hashConfig = MinHashConfiguration.builder()
                .seed(7)
                .prime(16777619)
                .numberOfHashFunctions(200)
                .build();
        ISimilarityCalculator<List<Integer>> similarityCalculator = new JaccardSimilarityCalculator(hashConfig);
        MinHashingHandler minHash = new MinHashingHandler(hashConfig, textHasher);

        List<String> tokens1 = List.of("FOR", "LPAREN", "INT", "IDENTIFIER", "ASSIGN", "LITERAL");
        List<String> tokens2 = List.of("FOR", "LPAREN", "INT", "IDENTIFIER", "ASSIGN", "NUMERIC");

        List<List<String>> shingles1 = shingleGenerator.generate(tokens1);
        List<List<String>> shingles2 = shingleGenerator.generate(tokens2);

        List<String> shingleStrings1 = shingleGenerator.flatSingles(shingles1);
        List<String> shingleStrings2 = shingleGenerator.flatSingles(shingles2);

        List<Integer> signature1 = minHash.computeMinHash(shingleStrings1);
        List<Integer> signature2 = minHash.computeMinHash(shingleStrings2);

        double similarity = similarityCalculator.computeSimilarity(signature1, signature2);
        assertTrue(similarity >= 0.5);
    }
}
