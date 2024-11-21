package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClassIdentity {
    private final String name;
    private final String packageName;
    private final List<String> modifiers;
    private final List<AnnotationInfo> annotations;
}
