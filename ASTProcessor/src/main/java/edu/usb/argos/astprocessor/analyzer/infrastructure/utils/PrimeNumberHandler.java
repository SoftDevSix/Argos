package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import java.util.Random;

public class PrimeNumberHandler {

    public boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;

        if (number % 2 == 0 || number % 3 == 0) return false;

        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }

        return true;
    }

    public int generateLargePrime(Random random) {
        while (true) {
            int candidate = random.nextInt(Integer.MAX_VALUE / 2) * 2 + 1;

            if (isPrime(candidate)) {
                return candidate;
            }
        }
    }

    public int[] generateLargePrimeValues(int count, int seed) {
        int[] values = new int[count];
        Random random = new Random(seed);

        for (int i = 0; i < count; i++) {
            values[i] = generateLargePrime(random);
        }

        return values;
    }
}
