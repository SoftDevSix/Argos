package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.MethodPair;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.cantidateSelectors.LSHCandidateSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.CodeMinHashConfig;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.LSHConfig;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.CodeMinHash;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LSHCandidateSelectorTest {

    private static CodeMinHash minHash;

    @BeforeAll
    public static void setup() {
        IPlainTextHasher textHasher = new SHATextHasher();
        CodeMinHashConfig hashConfig = CodeMinHashConfig.builder()
                .numHashFunctions(100)
                .prime(16777619)
                .build();

        minHash = new CodeMinHash(hashConfig, textHasher);
    }

    @Test
    public void testCandidateSelector() {
        LSHConfig lshConfig = LSHConfig.builder()
                .numHashFunctions(100)
                .numBands(20)
                .prime(16777619)
                .similarityThreshold(0.7)
                .build();

        LSHCandidateSelector lsh = new LSHCandidateSelector(lshConfig);

        Map<String, List<Integer>> methodSignatures = new HashMap<>();
        methodSignatures.put("Method1", minHash.computeMinHash(Arrays.asList("shingle1", "shingle2")));
        methodSignatures.put("Method2", minHash.computeMinHash(Arrays.asList("shingle1", "shingle2")));
        methodSignatures.put("Method3", minHash.computeMinHash(Arrays.asList("different", "shingles")));

        Set<MethodPair> candidates = lsh.findCandidatePairs(methodSignatures);
        Set<MethodPair> similarPairs = lsh.filterCandidates(
                candidates,
                methodSignatures,
                minHash
        );

        int expectedNumberOfCandidates = 1;
        int expectedNumberOfSimilarPairs = 1;

        assertEquals(expectedNumberOfCandidates, candidates.size());
        assertEquals(expectedNumberOfSimilarPairs, similarPairs.size());
    }
}
