package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AnnotationInfo {

    private String name;
    private Map<String, String> attributes;

    public AnnotationInfo() {
        this.attributes = new HashMap<>();
    }
}
