package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.EntityWithSignature;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.OrderedPair;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ISimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LshSelector<T> {

    private final LshConfiguration lshConfig;
    private final ISimilarityCalculator<List<Integer>> similarityCalculator;

    public Set<OrderedPair<UUID>> findCandidatePairs(List<EntityWithSignature<T, List<Integer>>> entityWithSignatures) {
        Map<Integer, List<EntityWithSignature<T, List<Integer>>>> bandBuckets = new HashMap<>();

        for (EntityWithSignature<T, List<Integer>> entity : entityWithSignatures) {
            handleEntity(entity, bandBuckets);
        }

        return findCandidatePairs(bandBuckets);
    }

    private void handleEntity(EntityWithSignature<T, List<Integer>> entity, Map<Integer, List<EntityWithSignature<T, List<Integer>>>> bandBuckets) {
        for (int bandIndex = 0; bandIndex < lshConfig.getNumberOfBands(); bandIndex++) {
            List<Integer> band = entity.getSignature().subList(
                    bandIndex * lshConfig.getRowsPerBand(),
                    (bandIndex + 1) * lshConfig.getRowsPerBand()
            );

            int bandHash = computeBandHash(band);
            bandBuckets.computeIfAbsent(bandHash, k -> new ArrayList<>()).add(entity);
        }
    }

    private Set<OrderedPair<UUID>> findCandidatePairs(Map<Integer, List<EntityWithSignature<T, List<Integer>>>> bandBuckets) {
        Set<OrderedPair<UUID>> candidatePairs = new HashSet<>();
        for (List<EntityWithSignature<T, List<Integer>>> bucket : bandBuckets.values()) {
            if (bucket.size() > 1) {
                handleBudget(bucket, candidatePairs);
            }
        }

        return candidatePairs;
    }

    private void handleBudget(List<EntityWithSignature<T, List<Integer>>> bucket, Set<OrderedPair<UUID>> candidatePairs) {
        for (int i = 0; i < bucket.size(); i++) {
            for (int j = i + 1; j < bucket.size(); j++) {
                candidatePairs.add(new OrderedPair<>(bucket.get(i).getId(), bucket.get(j).getId()));
            }
        }
    }

    public Set<OrderedPair<UUID>> filterCandidates(Set<OrderedPair<UUID>> candidatePairs, Map<UUID, EntityWithSignature<T, List<Integer>>> entityMap) {
        return candidatePairs.stream()
                .filter(pair -> {
                    List<Integer> signature1 = entityMap.get(pair.firstElement()).getSignature();
                    List<Integer> signature2 = entityMap.get(pair.secondElement()).getSignature();

                    double similarity = similarityCalculator.computeSimilarity(signature1, signature2);
                    return similarity >= lshConfig.getSimilarityThreshold();
                })
                .collect(Collectors.toSet());
    }

    private int computeBandHash(List<Integer> band) {
        int hash = 0;

        for (Integer value : band) {
            hash = hash * lshConfig.getPrime() + value.hashCode();
        }

        return Math.abs(hash);
    }
}
