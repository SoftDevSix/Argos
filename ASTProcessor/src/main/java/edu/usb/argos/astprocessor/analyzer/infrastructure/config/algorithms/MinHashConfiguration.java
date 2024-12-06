package edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms;

import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.PrimeNumberHandler;
import edu.usb.argos.astprocessor.analyzer.infrastructure.validations.algorithms.MinHashConfigurationValidator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
public class MinHashConfiguration {

    int seed;
    int prime;
    int numberOfHashFunctions;

    @Getter(AccessLevel.NONE)
    PrimeNumberHandler primeNumberHandler;

    @Getter(AccessLevel.NONE)
    MinHashConfigurationValidator configurationValidator;

    @Builder
    private MinHashConfiguration(int seed, int prime, int numberOfHashFunctions) {
        this.seed = seed;
        this.prime = prime;
        this.numberOfHashFunctions = numberOfHashFunctions;

        this.primeNumberHandler = new PrimeNumberHandler();
        this.configurationValidator = new MinHashConfigurationValidator(primeNumberHandler);
        configurationValidator.validateMinHashConfiguration(this);
    }
}
