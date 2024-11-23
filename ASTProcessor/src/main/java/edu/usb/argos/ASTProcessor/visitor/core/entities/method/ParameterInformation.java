package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ParameterInformation {
    String name;
    String type;
    List<String> modifiers;
    boolean isVarArgs;
}
