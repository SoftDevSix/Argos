package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AttributeInformation {
    private String name;
    private String type;
    private List<String> modifiers;
}
