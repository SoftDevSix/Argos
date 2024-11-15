package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassIdentity;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInfo;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassMembers;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassStructure;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassAnalyzerVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassIdentityCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassStructureCollector;
import org.antlr.v4.runtime.ParserRuleContext;

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
    public ClassInfo visitClass(ParserRuleContext ctx) {

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

        ClassInfo classInfo = new ClassInfo(identity, structure, members);

        return classInfo;
    }
}
