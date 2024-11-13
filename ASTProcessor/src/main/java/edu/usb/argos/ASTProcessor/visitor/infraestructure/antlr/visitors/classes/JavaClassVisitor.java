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
    private final IClassMetricsCollector<ParserRuleContext> metricsCollector;

    public JavaClassVisitor(
            IClassIdentityCollector<ParserRuleContext> identityCollector,
            IClassStructureCollector<ParserRuleContext> structureCollector,
            IClassMemberCollector<ParserRuleContext> memberCollector,
            IClassMetricsCollector<ParserRuleContext> metricsCollector) {
        this.identityCollector = identityCollector;
        this.structureCollector = structureCollector;
        this.memberCollector = memberCollector;
        this.metricsCollector = metricsCollector;
    }

    @Override
    public ClassInfo visitClassDeclaration(ParserRuleContext ctx) {
        // Construir la identidad de la clase
        ClassIdentity identity = new ClassIdentity(
                identityCollector.getClassName(ctx),
                identityCollector.getPackageName(ctx),
                identityCollector.getClassModifiers(ctx),
                identityCollector.getClassAnnotations(ctx)
        );

        // Construir la estructura de la clase
        ClassStructure structure = new ClassStructure(
                structureCollector.getSuperClass(ctx),
                structureCollector.getImplementedInterfaces(ctx)
        );

        // Construir los miembros de la clase
        ClassMembers members = new ClassMembers(
                memberCollector.getClassMethods(ctx)
        );

        // Construir las métricas de la clase
        ClassMetrics metrics = new ClassMetrics(
                metricsCollector.getTotalLines(ctx),
                metricsCollector.getCommentLines(ctx),
                metricsCollector.getCommentRatio(ctx),
                metricsCollector.getNumberOfMethods(ctx),
                metricsCollector.getNumberOfAttributes(ctx)
        );

        return new ClassInfo(identity, structure, members, metrics);
    }
}
