package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class AttributeInformation {
    private String name;
    private String type;
    private List<String> modifiers;
    private Optional<String> value;
}
