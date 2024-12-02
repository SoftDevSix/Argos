package edu.usb.argos.astprocessor.visitor.core.interfaces.services;

import java.util.List;
import java.util.Optional;

public interface IModifierExtractor<N> {
    Optional<List<String>> extractModifiers(N bodyCtx);
}
