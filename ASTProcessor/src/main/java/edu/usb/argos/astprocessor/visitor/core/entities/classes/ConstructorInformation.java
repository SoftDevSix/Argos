package edu.usb.argos.astprocessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class ConstructorInformation<S> {
    String name;
    List<String> modifiers;
    List<String> parameters;
    List<S> bodyStatements;
}
