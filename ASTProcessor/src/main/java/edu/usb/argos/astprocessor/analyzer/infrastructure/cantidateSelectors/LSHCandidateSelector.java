package edu.usb.argos.astprocessor.analyzer.infrastructure.cantidateSelectors;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.MethodPair;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.LSHConfig;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.CodeMinHash;
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

    private final LSHConfig lshConfig;

    public Set<MethodPair> findCandidatePairs(Map<String, List<Integer>> methodSignatures) {
        Map<Integer, List<String>> bandBuckets = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : methodSignatures.entrySet()) {
            String methodId = entry.getKey();
            List<Integer> signature = entry.getValue();

            for (int bandIndex = 0; bandIndex < lshConfig.getNumBands(); bandIndex++) {
                List<Integer> band = signature.subList(
                        bandIndex * lshConfig.getRowsPerBand(),
                        (bandIndex + 1) * lshConfig.getRowsPerBand()
                );

                int bandHash = computeBandHash(band);
                bandBuckets.computeIfAbsent(bandHash, k -> new ArrayList<>()).add(methodId);
            }
        }

        Set<MethodPair> candidatePairs = new HashSet<>();
        for (List<String> bucket : bandBuckets.values()) {
            if (bucket.size() > 1) {
                for (int i = 0; i < bucket.size(); i++) {
                    for (int j = i + 1; j < bucket.size(); j++) {
                        candidatePairs.add(new MethodPair(bucket.get(i), bucket.get(j)));
                    }
                }
            }
        }

        return candidatePairs;
    }

    public Set<MethodPair> filterCandidates(Set<MethodPair> candidatePairs, Map<String, List<Integer>> methodSignatures, CodeMinHash codeMinHash) {
        return candidatePairs.stream()
                .filter(pair -> {
                    List<Integer> signature1 = methodSignatures.get(pair.firstMethod);
                    List<Integer> signature2 = methodSignatures.get(pair.secondMethod);

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