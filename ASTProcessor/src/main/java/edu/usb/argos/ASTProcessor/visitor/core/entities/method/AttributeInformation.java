package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import lombok.Builder;
import lombok.Value;
import java.util.List;
import java.util.Optional;

@Value
@Builder
public class AttributeInformation {
    String name;
    String type;
    List<String> modifiers;
    Optional<String> value;
}
