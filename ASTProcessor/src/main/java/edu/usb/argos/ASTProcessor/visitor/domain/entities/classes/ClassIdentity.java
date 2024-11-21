package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
public class ClassIdentity {
    private final Optional<String> name;
    private final Optional<String> packageName;
    private final List<String> modifiers;
    private final List<AnnotationInfo> annotations;
}
