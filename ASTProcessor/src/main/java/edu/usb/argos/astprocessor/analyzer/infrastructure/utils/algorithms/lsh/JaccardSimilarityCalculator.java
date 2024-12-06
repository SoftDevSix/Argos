package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.ISimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.exceptions.JaccardSimilarityException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class JaccardSimilarityCalculator implements ISimilarityCalculator<List<Integer>> {

    private final MinHashConfiguration hashConfiguration;

    @Override
    public double computeSimilarity(List<Integer> signature1, List<Integer> signature2) {
        if (signature1.size() != signature2.size()) {
            throw new JaccardSimilarityException("Signatures must be of equal length");
        }

        int matches = 0;
        for (int i = 0; i < signature1.size(); i++) {
            if (signature1.get(i).equals(signature2.get(i))) {
                matches++;
            }
        }

        return (double) matches / hashConfiguration.getNumberOfHashFunctions();
    }
}
