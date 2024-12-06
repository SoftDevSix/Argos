package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.PrimeNumberHandler;

import java.util.ArrayList;
import java.util.List;

public class MinHashingHandler {

    private final int[] aValues;
    private final int[] bValues;
    private final IPlainTextHasher textHasher;
    private final MinHashConfiguration hashConfig;

    public MinHashingHandler(MinHashConfiguration hashConfig, IPlainTextHasher textHasher) {
        this.hashConfig = hashConfig;
        this.textHasher = textHasher;

        PrimeNumberHandler primeHandler = new PrimeNumberHandler();
        this.aValues = primeHandler.generateLargePrimeValues(hashConfig.getNumberOfHashFunctions(), hashConfig.getSeed());
        this.bValues = primeHandler.generateLargePrimeValues(hashConfig.getNumberOfHashFunctions(), hashConfig.getSeed());
    }

    public List<Integer> computeMinHash(List<String> shingles) {
        List<Integer> signature = new ArrayList<>(hashConfig.getNumberOfHashFunctions());

        for (int i = 0; i < hashConfig.getNumberOfHashFunctions(); i++) {
            int minHash = Integer.MAX_VALUE;

            for (String shingle : shingles) {
                int hashValue = computeHashValue(shingle, aValues[i], bValues[i], hashConfig.getPrime());
                minHash = Math.min(minHash, hashValue);
            }

            signature.add(minHash);
        }

        return signature;
    }

    private int computeHashValue(String shingle, int a, int b, int prime) {
        byte[] hash = textHasher.hashAsBytes(shingle);

        int hashInt = 0;
        for (int i = 0; i < 4; i++) {
            hashInt = (hashInt << 8) | (hash[i] & 0xFF);
        }

        return Math.abs((a * hashInt + b) % prime);
    }
}
