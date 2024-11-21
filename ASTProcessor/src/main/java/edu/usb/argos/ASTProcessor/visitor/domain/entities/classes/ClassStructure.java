package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
public class ClassStructure {
    private final Optional<String> superClass;
    private final List<String> interfaces;
}
