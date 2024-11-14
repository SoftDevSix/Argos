package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.List;

public class ClassInfo {

    private final ClassIdentity identity;
    private final ClassStructure structure;
    private final ClassMembers members;

    public ClassInfo(ClassIdentity identity, ClassStructure structure,
                     ClassMembers members) {
        this.identity = identity;
        this.structure = structure;
        this.members = members;
    }

    public ClassIdentity getIdentity() {
        return identity;
    }

    public ClassStructure getStructure() {
        return structure;
    }

    public ClassMembers getMembers() {
        return members;
    }
}
