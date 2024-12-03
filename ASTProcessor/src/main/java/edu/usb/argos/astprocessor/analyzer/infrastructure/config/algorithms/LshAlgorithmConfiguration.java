package edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LshAlgorithmConfiguration {
    @Bean
    public MinHashConfiguration minHashConfiguration() {
        return MinHashConfiguration
                .builder()
                .seed(7)
                .numberOfHashFunctions(15)
                .prime(16777619)
                .build();
    }

    @Bean
    public LshConfiguration lshConfiguration(MinHashConfiguration minHashConfiguration) {
        return LshConfiguration.builder()
                .shinglesFrequency(3)
                .minHashConfiguration(minHashConfiguration)
                .numberOfBands(5)
                .similarityThreshold(0.4)
                .build();
    }
}
