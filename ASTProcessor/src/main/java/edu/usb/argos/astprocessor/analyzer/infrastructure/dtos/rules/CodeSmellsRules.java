package edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSmellsRules {
    Integer id;
    boolean excessiveParameters;
    boolean magicNumbers;
    boolean methodTooLong;
    boolean noDuplicatedCode;
    int maxMethodLength;
    int maxParameters;
}
