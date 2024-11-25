package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class ConstructorInformation<S> {
    private String name;
    private List<String> modifiers;
    private List<String> parameters;
    private List<S> bodyStatements;
}
