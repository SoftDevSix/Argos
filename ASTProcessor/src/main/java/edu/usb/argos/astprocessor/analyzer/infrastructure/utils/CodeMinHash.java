package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.CodeMinHashConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodeMinHash {
    
    private final IPlainTextHasher textHasher;
    private final CodeMinHashConfig hashConfig;
    private final int[] aValues;
    private final int[] bValues;

    public CodeMinHash(final CodeMinHashConfig hashConfig, IPlainTextHasher textHasher) {
        this.hashConfig = hashConfig;
        this.aValues = new int[hashConfig.getNumHashFunctions()];
        this.bValues = new int[hashConfig.getNumHashFunctions()];

        this.textHasher = textHasher;
        createHashFunctions();
    }

    private void createHashFunctions() {
        Random random = new Random();

        for (int i = 0; i < hashConfig.getNumHashFunctions(); i++) {
            aValues[i] = random.nextInt(Integer.MAX_VALUE - 1) + 1;
            bValues[i] = random.nextInt(Integer.MAX_VALUE - 1) + 1;
        }
    }

    public List<Integer> computeMinHash(List<String> shingles) {
        List<Integer> minHashSignature = new ArrayList<>(hashConfig.getNumHashFunctions());

        for (int i = 0; i < hashConfig.getNumHashFunctions(); i++) {
            int minHash = Integer.MAX_VALUE;

            for (String shingle : shingles) {
                int hashValue = computeHashValue(shingle, aValues[i], bValues[i], hashConfig.getPrime());
                minHash = Math.min(minHash, hashValue);
            }

            minHashSignature.add(minHash);
        }

        return minHashSignature;
    }

    private int computeHashValue(String shingle, int a, int b, int prime) {
        byte[] hash = textHasher.hashAsBytes(shingle);

        int hashInt = 0;
        for (int i = 0; i < 4; i++) {
            hashInt = (hashInt << 8) | (hash[i] & 0xFF);
        }

        return Math.abs((a * hashInt + b) % prime);
    }

    public double computeSimilarity(List<Integer> signature1, List<Integer> signature2) {
        if (signature1.size() != signature2.size()) {
            throw new IllegalArgumentException("Signatures must be of equal length");
        }

        int matches = 0;
        for (int i = 0; i < signature1.size(); i++) {
            if (signature1.get(i).equals(signature2.get(i))) {
                matches++;
            }
        }

        return (double) matches / hashConfig.getNumHashFunctions();
    }
}