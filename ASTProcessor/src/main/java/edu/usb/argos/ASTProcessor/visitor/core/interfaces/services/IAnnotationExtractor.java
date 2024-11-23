package edu.usb.argos.ASTProcessor.visitor.core.interfaces.services;

import java.util.List;

public interface IAnnotationExtractor<Node> {
    List<String> extractAnnotation(Node bodyCtx);
}
