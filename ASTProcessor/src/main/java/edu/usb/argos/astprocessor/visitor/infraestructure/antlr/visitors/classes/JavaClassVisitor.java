package edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.antlr.JavaParserBaseVisitor;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassIdentity;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassMembers;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassStructure;
import edu.usb.argos.astprocessor.visitor.core.interfaces.visitor.IClassAnalyzerVisitor;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassIdentityService;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassMemberService;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassStructureService;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.antlr.v4.runtime.ParserRuleContext;

@EqualsAndHashCode(callSuper = true)
@Value
public class JavaClassVisitor extends JavaParserBaseVisitor<ClassInformation<JavaParser.StatementContext>>
        implements IClassAnalyzerVisitor<ParserRuleContext, JavaParser.StatementContext> {

    IClassIdentityService<ParserRuleContext> identityCollector;
    IClassStructureService<ParserRuleContext> structureCollector;
    IClassMemberService<ParserRuleContext, JavaParser.StatementContext> memberCollector;

    @Override
    public ClassInformation<JavaParser.StatementContext> visitClass(ParserRuleContext ctx) {
        ClassIdentity identity = buildClassIdentity(ctx);
        ClassStructure structure = buildClassStructure(ctx);
        ClassMembers<JavaParser.StatementContext> members = buildClassMembers(ctx);

        return ClassInformation.<JavaParser.StatementContext>builder()
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

    private ClassMembers<JavaParser.StatementContext> buildClassMembers(ParserRuleContext ctx) {
        return ClassMembers.<JavaParser.StatementContext>builder()
                .methods(memberCollector.getClassMethods(ctx))
                .attributes(memberCollector.getClassAttributes(ctx))
                .constructors(memberCollector.getClassConstructors(ctx))
                .build();
    }
}
