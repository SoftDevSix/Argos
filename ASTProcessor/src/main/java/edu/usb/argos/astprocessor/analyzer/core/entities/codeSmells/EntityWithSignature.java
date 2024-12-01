package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class EntityWithSignature<T, S> {
    @Builder.Default
    UUID id = UUID.randomUUID();
    T entity;
    S signature;
}
