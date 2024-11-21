package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import lombok.Getter;

@Getter
public enum ModifierType {
    PUBLIC("PUBLIC", "public"),
    PRIVATE("PRIVATE", "private"),
    PROTECTED("PROTECTED", "protected"),
    STATIC("STATIC", "static"),
    FINAL("FINAL", "final");

    private final String methodName;
    private final String keyword;

    ModifierType(String methodName, String keyword) {
        this.methodName = methodName;
        this.keyword = keyword;
    }
}
