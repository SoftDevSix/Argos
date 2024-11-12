package edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities;

import java.util.List;

public class AttributeInfo {
    private String name;
    private String type;
    private List<String> modifiers;

    public AttributeInfo(String name, String type, List<String> modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        return "AttributeInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", modifiers=" + modifiers +
                '}';
    }
}
