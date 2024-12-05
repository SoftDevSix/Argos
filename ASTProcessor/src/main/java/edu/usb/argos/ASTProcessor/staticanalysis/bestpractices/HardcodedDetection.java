package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HardcodedDetection {
    int lineNumber;
    String hardcodedValue;
}
