package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.Map;

public class AnnotationInfo {

    private final String name;
    private final Map<String, Object> attributes;

    public AnnotationInfo(String name, Map<String, Object> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
