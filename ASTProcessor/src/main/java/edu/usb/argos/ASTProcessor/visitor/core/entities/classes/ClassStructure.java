package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassStructure {
    private final String superClass;
    private final List<String> interfaces;
}
