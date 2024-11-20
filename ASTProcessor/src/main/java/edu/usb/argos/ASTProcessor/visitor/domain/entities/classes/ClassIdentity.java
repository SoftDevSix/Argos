package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassIdentity {

    private String name;
    private String packageName;
    private List<String> modifiers;
    private List<AnnotationInfo> annotations;
}
