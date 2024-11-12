package edu.usb.argos.ASTProcessor.visitor.domain.entities.classes;

import java.util.List;

public class ClassInfo {

    private final ClassIdentity identity;
    private final ClassStructure structure;
    private final ClassMembers members;
    private final ClassMetrics metrics;

    public ClassInfo(ClassIdentity identity, ClassStructure structure,
                     ClassMembers members, ClassMetrics metrics) {
        this.identity = identity;
        this.structure = structure;
        this.members = members;
        this.metrics = metrics;
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

    public ClassMetrics getMetrics() {
        return metrics;
    }
}
