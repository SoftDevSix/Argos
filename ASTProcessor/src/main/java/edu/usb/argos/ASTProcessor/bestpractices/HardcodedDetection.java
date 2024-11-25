package edu.usb.argos.ASTProcessor.bestpractices;

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
