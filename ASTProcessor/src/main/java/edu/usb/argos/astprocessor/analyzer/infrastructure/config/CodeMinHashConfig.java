package edu.usb.argos.astprocessor.analyzer.infrastructure.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CodeMinHashConfig {

    @Builder.Default
    int prime = 16777619;

    @Builder.Default
    int numHashFunctions = 100;

}
