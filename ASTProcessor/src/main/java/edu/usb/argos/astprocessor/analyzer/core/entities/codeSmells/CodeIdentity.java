package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class CodeIdentity<O> {
    String identifier;
    CodeRange codeRange;
    O origin;
}
