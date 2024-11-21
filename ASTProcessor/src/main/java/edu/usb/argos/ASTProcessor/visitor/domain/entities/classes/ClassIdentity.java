package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassIdentity {
    private final String name;
    private final String packageName;
    private final List<String> modifiers;
    private final List<AnnotationInfo> annotations;
}
