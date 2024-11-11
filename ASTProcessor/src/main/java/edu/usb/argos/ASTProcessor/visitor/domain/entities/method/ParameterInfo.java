package edu.usb.argos.ASTProcessor.visitor.domain.entities.method;

public class ParameterInfo {
    private final String name;
    private final String type;
    
    public ParameterInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
