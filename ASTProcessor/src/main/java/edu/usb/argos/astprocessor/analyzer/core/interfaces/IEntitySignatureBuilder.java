package edu.usb.argos.astprocessor.analyzer.core.interfaces;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.EntityWithSignature;

import java.util.List;

public interface IEntitySignatureBuilder<T, S, R> {
    List<EntityWithSignature<T, S>> buildMultipleFromResource(R resource);
}
