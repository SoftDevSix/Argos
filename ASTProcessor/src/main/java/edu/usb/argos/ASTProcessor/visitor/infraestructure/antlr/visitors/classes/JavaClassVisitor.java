package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassIdentity;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassMembers;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassStructure;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassAnalyzerVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassIdentityCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassStructureCollector;
import org.antlr.v4.runtime.ParserRuleContext;

public class JavaClassVisitor extends JavaParserBaseVisitor<ClassInformation>
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
    public ClassInformation visitClass(ParserRuleContext ctx) {

        ClassIdentity identity = ClassIdentity.builder()
                .name(identityCollector.getClassName(ctx))
                .packageName(identityCollector.getPackageName(ctx))
                .modifiers(identityCollector.getClassModifiers(ctx))
                .annotations(identityCollector.getClassAnnotations(ctx))
                .build();

        ClassStructure structure = ClassStructure.builder()
                .superClass(structureCollector.getSuperClass(ctx))
                .interfaces(structureCollector.getImplementedInterfaces(ctx))
                .build();

        ClassMembers members = ClassMembers.builder()
                .methods(memberCollector.getClassMethods(ctx))
                .attributes(memberCollector.getClassAttributes(ctx))
                .constructors(memberCollector.getClassConstructors(ctx))
                .build();

        ClassInformation classInformation = ClassInformation.builder()
                .identity(identity)
                .structure(structure)
                .members(members)
                .build();

        return classInformation;
    }
}
