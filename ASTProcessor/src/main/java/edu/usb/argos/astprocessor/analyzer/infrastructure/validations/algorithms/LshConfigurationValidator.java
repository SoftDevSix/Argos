package edu.usb.argos.astprocessor.analyzer.infrastructure.validations.algorithms;

import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.exceptions.LshConfigurationException;

public class LshConfigurationValidator {

    public void validateLshConfiguration(LshConfiguration configuration) {
        validateMinHashConfiguration(configuration.getMinHashConfiguration());
        validateShinglesFrequency(configuration.getShinglesFrequency());
        validateNumberOfBands(configuration.getNumberOfBands(), configuration.getNumberOfHashFunctions());
        validateSimilarityThreshold(configuration.getSimilarityThreshold());
    }

    public void validateMinHashConfiguration(MinHashConfiguration minHashConfiguration) {
        if (minHashConfiguration == null) {
            throw new LshConfigurationException("'minHashConfiguration' can not be null");
        }
    }

    public void validateNumberOfBands(int numberOfBands, int numberOfHashFunctions) {
        if (numberOfBands <= 0) {
            throw new LshConfigurationException("'numberOfBands' must be a positive integer.");
        }
        if (numberOfHashFunctions % numberOfBands != 0) {
            throw new LshConfigurationException("'numberOfHashFunctions' must be divisible by 'numberOfBands'.");
        }
    }

    public void validateShinglesFrequency(int shinglesFrequency) {
        if (shinglesFrequency <= 0) {
            throw new LshConfigurationException("'shinglesFrequency' must be a positive integer.");
        }
    }

    public void validateSimilarityThreshold(double similarityThreshold) {
        if (similarityThreshold <= 0.0 || similarityThreshold > 1.0) {
            throw new LshConfigurationException("'similarityThreshold' must be a value between 0.0 (exclusive) and 1.0 (inclusive).");
        }
    }
}
