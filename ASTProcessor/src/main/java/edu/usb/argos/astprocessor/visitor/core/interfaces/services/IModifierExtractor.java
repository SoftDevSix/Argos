package edu.usb.argos.astprocessor.visitor.core.interfaces.services;

import java.util.List;
import java.util.Optional;

public interface IModifierExtractor<Node> {
    Optional<List<String>> extractModifiers(Node bodyCtx);
}
