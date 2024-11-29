package edu.usb.argos.astprocessor.analyzer.infrastructure.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LSHConfig {

    @Builder.Default
    int shinglesFrequency = 10;

    @Builder.Default
    int prime = 16777619;

    @Builder.Default
    int numBands = 20;

    @Builder.Default
    int numHashFunctions = 100;

    @Builder.Default
    double similarityThreshold = 0.7;

    public int getRowsPerBand() {
        return numHashFunctions / numBands;
    }

    public static void validate(LSHConfig config) {
        if (config.numHashFunctions % config.numBands != 0) {
            throw new IllegalArgumentException("Number of hash functions must be divisible by number of bands");
        }
    }

    public static LSHConfigBuilder builder() {
        return new LSHConfigBuilder() {
            @Override
            public LSHConfig build() {
                LSHConfig config = super.build();
                LSHConfig.validate(config);

                return config;
            }
        };
    }
}
