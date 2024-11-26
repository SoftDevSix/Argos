package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClassInformation<T> {
    ClassIdentity identity;
    ClassStructure structure;
    ClassMembers<T> members;
}
