package edu.usb.argos.ASTProcessor.visitor.domain.services.collectors.classes;

import edu.usb.argos.ASTProcessor.AttributeAnalyzer.AttributeAnalyzerVisitor.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.MethodInfo;
import edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes.IClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class JavaClassMemberCollector implements IClassMemberCollector<ParserRuleContext> {
    private final JavaMethodVisitor methodVisitor;
    private final JavaAttributeVisitor attributeVisitor;

    public JavaClassMemberCollector(JavaMethodVisitor methodVisitor, JavaAttributeVisitor attributeVisitor) {
        this.methodVisitor = methodVisitor;
        this.attributeVisitor = attributeVisitor;
    }

    @Override
    public List<MethodInfo> getClassMethods(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> {
                    List<MethodInfo> methods = new ArrayList<>();
                    classCtx.classBody().classBodyDeclaration().stream()
                            .filter(bodyDecl -> bodyDecl.memberDeclaration() != null)
                            .filter(bodyDecl -> bodyDecl.memberDeclaration().methodDeclaration() != null)
                            .forEach(bodyDecl -> {
                                MethodInfo method = methodVisitor.visitMethod(
                                        bodyDecl.memberDeclaration().methodDeclaration()
                                );
                                if (method != null) {
                                    methods.add(method);
                                }
                            });
                    return methods;
                },
                new ArrayList<>()
        );
    }

    public List<AttributeInfo> getClassAttributes(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> attributeVisitor.visitClassBody(classCtx.classBody()),
                new ArrayList<>()
        );
    }
}
