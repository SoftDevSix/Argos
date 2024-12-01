package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.OrderedPair;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LSHCandidateSelector {

    private final LshConfiguration lshConfig;

    public Set<OrderedPair<String>> findCandidatePairs(Map<String, List<Integer>> methodSignatures) {
        Map<Integer, List<String>> bandBuckets = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : methodSignatures.entrySet()) {
            String methodId = entry.getKey();
            List<Integer> signature = entry.getValue();

            for (int bandIndex = 0; bandIndex < lshConfig.getNumberOfBands(); bandIndex++) {
                List<Integer> band = signature.subList(
                        bandIndex * lshConfig.getRowsPerBand(),
                        (bandIndex + 1) * lshConfig.getRowsPerBand()
                );

                int bandHash = computeBandHash(band);
                bandBuckets.computeIfAbsent(bandHash, k -> new ArrayList<>()).add(methodId);
            }
        }

        Set<OrderedPair<String>> candidatePairs = new HashSet<>();
        for (List<String> bucket : bandBuckets.values()) {
            if (bucket.size() > 1) {
                for (int i = 0; i < bucket.size(); i++) {
                    for (int j = i + 1; j < bucket.size(); j++) {
                        candidatePairs.add(new OrderedPair<>(bucket.get(i), bucket.get(j)));
                    }
                }
            }
        }

        return candidatePairs;
    }

    public Set<OrderedPair<String>> filterCandidates(Set<OrderedPair<String>> candidatePairs, Map<String, List<Integer>> methodSignatures, CodeMinHash codeMinHash) {
        return candidatePairs.stream()
                .filter(pair -> {
                    List<Integer> signature1 = methodSignatures.get(pair.firstElement());
                    List<Integer> signature2 = methodSignatures.get(pair.secondElement());

                    double similarity = codeMinHash.computeSimilarity(signature1, signature2);
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