package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class HardcodedDetection {
    private int lineNumber;
    private String hardcodedValue;
}
