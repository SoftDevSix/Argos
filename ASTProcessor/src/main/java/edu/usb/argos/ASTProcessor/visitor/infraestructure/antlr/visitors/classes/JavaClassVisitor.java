package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.classes.ClassInfo;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes.*;
import org.antlr.v4.runtime.ParserRuleContext;

import edu.usb.argos.ASTProcessor.visitor.domain.entities.classes.*;

public class JavaClassVisitor extends JavaParserBaseVisitor<ClassInfo>
        implements IClassAnalyzerVisitor<ParserRuleContext> {

    private final IClassIdentityCollector<ParserRuleContext> identityCollector;
    private final IClassStructureCollector<ParserRuleContext> structureCollector;
    private final IClassMemberCollector<ParserRuleContext> memberCollector;

    public JavaClassVisitor(
            IClassIdentityCollector<ParserRuleContext> identityCollector,
            IClassStructureCollector<ParserRuleContext> structureCollector,
            IClassMemberCollector<ParserRuleContext> memberCollector) {
        this.identityCollector = identityCollector;
        this.structureCollector = structureCollector;
        this.memberCollector = memberCollector;
    }

    @Override
    public ClassInfo visitClassDeclaration(ParserRuleContext ctx) {

        ClassIdentity identity = new ClassIdentity(
                identityCollector.getClassName(ctx),
                identityCollector.getPackageName(ctx),
                identityCollector.getClassModifiers(ctx),
                identityCollector.getClassAnnotations(ctx)
        );

        ClassStructure structure = new ClassStructure(
                structureCollector.getSuperClass(ctx),
                structureCollector.getImplementedInterfaces(ctx)
        );

        ClassMembers members = new ClassMembers(
                memberCollector.getClassMethods(ctx),
                memberCollector.getClassAttributes(ctx)
        );

        return new ClassInfo(identity, structure, members);
    }
}
