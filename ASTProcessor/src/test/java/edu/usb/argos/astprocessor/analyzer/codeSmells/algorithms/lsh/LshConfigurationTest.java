package edu.usb.argos.astprocessor.analyzer.codeSmells.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.exceptions.LshConfigurationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LshConfigurationTest {

    @Test
    void validLshConfigurationShouldNotThrowException() {
        assertDoesNotThrow(() -> {
            MinHashConfiguration minHashConfig = MinHashConfiguration.builder()
                    .seed(123)
                    .prime(31)
                    .numberOfHashFunctions(12)
                    .build();

            LshConfiguration.builder()
                    .numberOfBands(3)
                    .shinglesFrequency(5)
                    .similarityThreshold(0.75)
                    .minHashConfiguration(minHashConfig)
                    .build();
        });
    }

    @Test
    void invalidMinHashConfigurationShouldThrowException() {
        Exception exception = assertThrows(LshConfigurationException.class, () -> {
            LshConfiguration.builder()
                    .numberOfBands(3)
                    .shinglesFrequency(5)
                    .similarityThreshold(0.75)
                    .minHashConfiguration(null)
                    .build();
        });

        assertEquals("'minHashConfiguration' can not be null", exception.getMessage());
    }

    @Test
    void invalidNumberOfBandsShouldThrowException() {
        Exception exception = assertThrows(LshConfigurationException.class, () -> {
            MinHashConfiguration minHashConfig = MinHashConfiguration.builder()
                    .seed(123)
                    .prime(31)
                    .numberOfHashFunctions(10)
                    .build();

            LshConfiguration.builder()
                    .numberOfBands(4)
                    .shinglesFrequency(5)
                    .similarityThreshold(0.75)
                    .minHashConfiguration(minHashConfig)
                    .build();
        });

        assertEquals("'numberOfHashFunctions' must be divisible by 'numberOfBands'.", exception.getMessage());
    }

    @Test
    void invalidShinglesFrequencyShouldThrowException() {
        Exception exception = assertThrows(LshConfigurationException.class, () -> {
            MinHashConfiguration minHashConfig = MinHashConfiguration.builder()
                    .seed(123)
                    .prime(31)
                    .numberOfHashFunctions(10)
                    .build();

            LshConfiguration.builder()
                    .numberOfBands(2)
                    .shinglesFrequency(0)
                    .similarityThreshold(0.75)
                    .minHashConfiguration(minHashConfig)
                    .build();
        });

        assertEquals("'shinglesFrequency' must be a positive integer.", exception.getMessage());
    }

    @Test
    void invalidSimilarityThresholdShouldThrowException() {
        Exception exception = assertThrows(LshConfigurationException.class, () -> {
            MinHashConfiguration minHashConfig = MinHashConfiguration.builder()
                    .seed(123)
                    .prime(31)
                    .numberOfHashFunctions(10)
                    .build();

            LshConfiguration.builder()
                    .numberOfBands(2)
                    .shinglesFrequency(5)
                    .similarityThreshold(1.5)
                    .minHashConfiguration(minHashConfig)
                    .build();
        });

        assertEquals("'similarityThreshold' must be a value between 0.0 (exclusive) and 1.0 (inclusive).", exception.getMessage());
    }
}
