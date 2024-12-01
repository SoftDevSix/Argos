package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.EntityWithSignature;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.OrderedPair;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ISimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.JaccardSimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.LshSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.MinHashingHandler;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LshSelectorTest {

    private static MinHashingHandler minHash;
    private static MinHashConfiguration hashConfig;

    @BeforeAll
    public static void setup() {
        IPlainTextHasher textHasher = new SHATextHasher();
        hashConfig = MinHashConfiguration.builder()
                .seed(7)
                .numberOfHashFunctions(100)
                .prime(16777619)
                .build();

        minHash = new MinHashingHandler(hashConfig, textHasher);
    }

    @Test
    public void testCandidateSelector() {
        LshConfiguration lshConfig = LshConfiguration.builder()
                .numberOfBands(5)
                .shinglesFrequency(10)
                .similarityThreshold(0.7)
                .minHashConfiguration(hashConfig)
                .build();
        ISimilarityCalculator<List<Integer>> similarityCalculator = new JaccardSimilarityCalculator(lshConfig.getMinHashConfiguration());
        LshSelector<String> lsh = new LshSelector<>(lshConfig, similarityCalculator);

        List<EntityWithSignature<String, List<Integer>>> entityWithSignatures = List.of(
                EntityWithSignature.<String, List<Integer>>builder()
                        .entity("Entity1")
                        .signature(minHash.computeMinHash(Arrays.asList("shingle1", "shingle2")))
                        .build(),
                EntityWithSignature.<String, List<Integer>>builder()
                        .entity("Entity2")
                        .signature(minHash.computeMinHash(Arrays.asList("shingle1", "shingle2")))
                        .build(),
                EntityWithSignature.<String, List<Integer>>builder()
                        .entity("Entity3")
                        .signature(minHash.computeMinHash(Arrays.asList("different", "shingles")))
                        .build()
        );

        Map<UUID, EntityWithSignature<String, List<Integer>>> entityMap = new HashMap<>();
        entityWithSignatures.forEach(entity -> entityMap.put(entity.getId(), entity));

        Set<OrderedPair<UUID>> candidates = lsh.findCandidatePairs(entityWithSignatures);
        Set<OrderedPair<UUID>> similarPairs = lsh.filterCandidates(candidates, entityMap);

        int expectedNumberOfCandidates = 1;
        int expectedNumberOfSimilarPairs = 1;

        assertEquals(expectedNumberOfCandidates, candidates.size());
        assertEquals(expectedNumberOfSimilarPairs, similarPairs.size());
    }
}
