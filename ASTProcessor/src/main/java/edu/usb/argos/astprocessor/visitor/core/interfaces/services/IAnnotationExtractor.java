package edu.usb.argos.astprocessor.visitor.core.interfaces.services;

import java.util.List;
import java.util.Optional;

public interface IAnnotationExtractor<Node> {
    Optional<List<String>> extractAnnotation(Node bodyCtx);
}
