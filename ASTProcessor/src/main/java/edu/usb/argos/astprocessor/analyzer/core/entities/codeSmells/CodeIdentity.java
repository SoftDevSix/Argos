package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class CodeIdentity<S> {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String identifier;
    private CodeRange codeRange;
    private S signature;
}
