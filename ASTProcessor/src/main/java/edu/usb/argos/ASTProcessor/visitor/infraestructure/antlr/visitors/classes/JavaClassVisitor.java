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
import lombok.Value;
import org.antlr.v4.runtime.ParserRuleContext;

@Value
public class JavaClassVisitor extends JavaParserBaseVisitor<ClassInformation>
        implements IClassAnalyzerVisitor<ParserRuleContext> {

    IClassIdentityCollector<ParserRuleContext> identityCollector;
    IClassStructureCollector<ParserRuleContext> structureCollector;
    IClassMemberCollector<ParserRuleContext> memberCollector;

    @Override
    public ClassInformation visitClass(ParserRuleContext ctx) {
        ClassIdentity identity = buildClassIdentity(ctx);
        ClassStructure structure = buildClassStructure(ctx);
        ClassMembers members = buildClassMembers(ctx);

        return ClassInformation.builder()
                .identity(identity)
                .structure(structure)
                .members(members)
                .build();
    }

    private ClassIdentity buildClassIdentity(ParserRuleContext ctx) {
        return ClassIdentity.builder()
                .name(identityCollector.getClassName(ctx))
                .packageName(identityCollector.getPackageName(ctx))
                .modifiers(identityCollector.getClassModifiers(ctx))
                .annotations(identityCollector.getClassAnnotations(ctx))
                .build();
    }

    private ClassStructure buildClassStructure(ParserRuleContext ctx) {
        return ClassStructure.builder()
                .superClass(structureCollector.getSuperClass(ctx))
                .interfaces(structureCollector.getImplementedInterfaces(ctx))
                .build();
    }

    private ClassMembers buildClassMembers(ParserRuleContext ctx) {
        return ClassMembers.builder()
                .methods(memberCollector.getClassMethods(ctx))
                .attributes(memberCollector.getClassAttributes(ctx))
                .constructors(memberCollector.getClassConstructors(ctx))
                .build();
    }
}
