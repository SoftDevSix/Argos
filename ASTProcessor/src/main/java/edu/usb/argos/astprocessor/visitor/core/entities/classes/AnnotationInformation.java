package edu.usb.argos.astprocessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AnnotationInformation {
    String name;
    Map<String, String> attributes;
}
