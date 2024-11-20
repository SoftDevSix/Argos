package edu.usb.argos.ASTProcessor.visitor.core.entities.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClassInfo {

    private final ClassIdentity identity;
    private final ClassStructure structure;
    private final ClassMembers members;

}
