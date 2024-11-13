package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.HashMap;
import java.util.Map;

public class AnnotationInfo {

    private String name;
    private Map<String, String> attributes;

    public AnnotationInfo() {
        this.attributes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
