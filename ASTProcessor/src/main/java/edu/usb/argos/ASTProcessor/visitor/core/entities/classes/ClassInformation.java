package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassInformation {
    private final ClassIdentity identity;
    private final ClassStructure structure;
    private final ClassMembers members;
}
