package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.PrimeNumberHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodeMinHash {
    
    private final IPlainTextHasher textHasher;
    private final MinHashConfiguration hashConfig;
    private final PrimeNumberHandler primeNumberHandler;
    private final int[] aValues;
    private final int[] bValues;

    public CodeMinHash(final MinHashConfiguration hashConfig, final IPlainTextHasher textHasher, PrimeNumberHandler primeNumberHandler) {
        this.hashConfig = hashConfig;
        this.textHasher = textHasher;
        this.primeNumberHandler = primeNumberHandler;

        this.aValues = primeNumberHandler.generatePrimeValues(hashConfig.getNumberOfHashFunctions(), hashConfig.getSeed());
        this.bValues = primeNumberHandler.generatePrimeValues(hashConfig.getNumberOfHashFunctions(), hashConfig.getSeed());
    }

    public int[] generateGoodHashValues(int count) {
        int[] values = new int[count];
        Random random = new Random(SEED);

        for (int i = 0; i < count; i++) {
            values[i] = generateLargePrime(random);
        }

        return values;
    }

    private int generateLargePrime(Random random) {
        while (true) {
            int candidate = random.nextInt(Integer.MAX_VALUE / 2) * 2 + 1;
            if (isPrime(candidate)) {
                return candidate;
            }
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;

        if (number % 2 == 0 || number % 3 == 0) return false;

        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }

        return true;
    }

    private void createHashFunctions() {
        Random random = new Random();

        for (int i = 0; i < hashConfig.getNumberOfHashFunctions(); i++) {
            aValues[i] = random.nextInt(Integer.MAX_VALUE - 1) + 1;
            bValues[i] = random.nextInt(Integer.MAX_VALUE - 1) + 1;
        }
    }

    public List<Integer> computeMinHash(List<String> shingles) {
        List<Integer> minHashSignature = new ArrayList<>(hashConfig.getNumberOfHashFunctions());

        for (int i = 0; i < hashConfig.getNumberOfHashFunctions(); i++) {
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
}
