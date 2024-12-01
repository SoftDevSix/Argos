package edu.usb.argos.astprocessor.analyzer.infrastructure.config;

import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.PrimeNumberHandler;
import edu.usb.argos.astprocessor.analyzer.infrastructure.validations.MinHashConfigurationValidator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
public class MinHashConfiguration {

    int prime;
    int numberOfHashFunctions;

    @Getter(AccessLevel.NONE)
    PrimeNumberHandler primeNumberHandler;

    @Getter(AccessLevel.NONE)
    MinHashConfigurationValidator configurationValidator;

    @Builder
    private MinHashConfiguration(int prime, int numberOfHashFunctions) {
        this.prime = prime;
        this.numberOfHashFunctions = numberOfHashFunctions;

        this.primeNumberHandler = new PrimeNumberHandler();
        this.configurationValidator = new MinHashConfigurationValidator(primeNumberHandler);
        configurationValidator.validateMinHashConfiguration(this);
    }
}
