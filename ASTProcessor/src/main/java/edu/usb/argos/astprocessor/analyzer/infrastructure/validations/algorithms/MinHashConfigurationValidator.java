package edu.usb.argos.astprocessor.analyzer.infrastructure.validations.algorithms;

import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.exceptions.MinHashConfigurationException;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.PrimeNumberHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinHashConfigurationValidator {

    private final PrimeNumberHandler primeNumberHandler;

    public void validateMinHashConfiguration(MinHashConfiguration configuration) {
        validatePrime(configuration.getPrime());
        validateNumberOfHashFunctions(configuration.getNumberOfHashFunctions());
        validateSeed(configuration.getSeed());
    }

    private void validatePrime(int prime) {
        if (prime <= 1 || !primeNumberHandler.isPrime(prime)) {
            throw new MinHashConfigurationException("'prime' must be a prime number greater than 1.");
        }
    }

    private void validateNumberOfHashFunctions(int numberOfHashFunctions) {
        if (numberOfHashFunctions <= 0) {
            throw new MinHashConfigurationException("'numberOfHashFunctions' must be a positive integer.");
        }
    }

    private void validateSeed(int seed) {
        if (seed <= 2) {
            throw new MinHashConfigurationException("'seed' must be greater than 1.");
        }
    }
}
