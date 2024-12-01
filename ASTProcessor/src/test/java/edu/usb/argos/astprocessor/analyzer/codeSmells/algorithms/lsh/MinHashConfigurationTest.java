package edu.usb.argos.astprocessor.analyzer.codeSmells.algorithms.lsh;

import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.exceptions.MinHashConfigurationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinHashConfigurationTest {

    @Test
    void validMinHashConfigurationShouldNotThrowException() {
        assertDoesNotThrow(() -> {
            MinHashConfiguration.builder()
                    .seed(123)
                    .prime(31)
                    .numberOfHashFunctions(12)
                    .build();
        });
    }

    @Test
    void invalidPrimeShouldThrowException() {
        Exception exception = assertThrows(MinHashConfigurationException.class, () -> {
            MinHashConfiguration.builder()
                    .seed(123)
                    .prime(4)
                    .numberOfHashFunctions(12)
                    .build();
        });

        assertEquals("'prime' must be a prime number greater than 1.", exception.getMessage());
    }

    @Test
    void invalidSeedShouldThrowException() {
        Exception exception = assertThrows(MinHashConfigurationException.class, () -> {
            MinHashConfiguration.builder()
                    .seed(1)
                    .prime(31)
                    .numberOfHashFunctions(12)
                    .build();
        });

        assertEquals("'seed' must be greater than 1.", exception.getMessage());
    }

    @Test
    void invalidNumberOfHashFunctionsShouldThrowException() {
        Exception exception = assertThrows(MinHashConfigurationException.class, () -> {
            MinHashConfiguration.builder()
                    .seed(123)
                    .prime(31)
                    .numberOfHashFunctions(0)
                    .build();
        });

        assertEquals("'numberOfHashFunctions' must be a positive integer.", exception.getMessage());
    }
}
