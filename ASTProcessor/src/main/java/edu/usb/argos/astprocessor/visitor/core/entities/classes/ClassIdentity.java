package edu.usb.argos.astprocessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class ClassIdentity {
    Optional<String> name;
    Optional<String> packageName;
    List<String> modifiers;
    List<AnnotationInformation> annotations;
}
