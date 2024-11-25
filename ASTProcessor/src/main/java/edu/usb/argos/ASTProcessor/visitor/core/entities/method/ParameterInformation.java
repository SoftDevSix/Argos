package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParameterInformation {
    String name;
    String type;
}
