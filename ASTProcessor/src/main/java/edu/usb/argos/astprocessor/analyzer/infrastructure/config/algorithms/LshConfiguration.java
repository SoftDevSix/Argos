package edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms;

import edu.usb.argos.astprocessor.analyzer.infrastructure.validations.algorithms.LshConfigurationValidator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
public class LshConfiguration {

    int numberOfBands;
    int shinglesFrequency;
    double similarityThreshold;
    MinHashConfiguration minHashConfiguration;

    @Getter(AccessLevel.NONE)
    LshConfigurationValidator configurationValidator;

    public int getPrime() {
        return minHashConfiguration.getPrime();
    }

    public int getNumberOfHashFunctions() {
        return minHashConfiguration.getNumberOfHashFunctions();
    }

    public int getRowsPerBand() {
        return getNumberOfHashFunctions() / numberOfBands;
    }

    @Builder
    private LshConfiguration(int numberOfBands, int shinglesFrequency, double similarityThreshold, MinHashConfiguration minHashConfiguration) {
        this.numberOfBands = numberOfBands;
        this.shinglesFrequency = shinglesFrequency;
        this.similarityThreshold = similarityThreshold;
        this.minHashConfiguration = minHashConfiguration;

        this.configurationValidator = new LshConfigurationValidator();
        configurationValidator.validateLshConfiguration(this);
    }
}
