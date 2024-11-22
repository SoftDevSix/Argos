package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AnnotationInfo {
    String name;
    Map<String, String> attributes;
}
