package edu.usb.argos.ASTProcessor.visitor.core.interfaces.services;

import java.util.List;

public interface IModifierExtractor<Node> {
    List<String> extractModifiers(Node bodyCtx);
}
