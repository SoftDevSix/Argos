package edu.usb.argos.astprocessor.analyzer.codeSmells.utils;

import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.PrimeNumberHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeNumberHandlerTest {

    private static PrimeNumberHandler primeNumberHandler;

    @BeforeAll
    public static void setupAll() {
        primeNumberHandler = new PrimeNumberHandler();
    }

    @Test
    void isPrimeShouldReturnTrueForPrimeNumbers() {
        assertTrue(primeNumberHandler.isPrime(2));
        assertTrue(primeNumberHandler.isPrime(3));
        assertTrue(primeNumberHandler.isPrime(5));
        assertTrue(primeNumberHandler.isPrime(31));
        assertTrue(primeNumberHandler.isPrime(97));
    }

    @Test
    void isPrimeShouldReturnFalseForNonPrimeNumbers() {
        assertFalse(primeNumberHandler.isPrime(1));
        assertFalse(primeNumberHandler.isPrime(4));
        assertFalse(primeNumberHandler.isPrime(9));
        assertFalse(primeNumberHandler.isPrime(100));
        assertFalse(primeNumberHandler.isPrime(121));
    }

    @Test
    void generateLargePrimeShouldReturnValidPrimeNumber() {
        Random random = new Random(1234);
        int prime = primeNumberHandler.generateLargePrime(random);
        assertTrue(primeNumberHandler.isPrime(prime));
    }

    @Test
    void generateLargePrimeValuesShouldReturnArrayOfPrimes() {
        int count = 5;
        int seed = 5678;
        int[] primes = primeNumberHandler.generateLargePrimeValues(count, seed);

        assertEquals(count, primes.length);
        for (int prime : primes) {
            assertTrue(primeNumberHandler.isPrime(prime));
        }
    }
}
