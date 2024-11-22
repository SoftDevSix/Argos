package edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.classes.IClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import edu.usb.argos.ASTProcessor.visitor.shared.validation.ContextValidator;
import lombok.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
public class JavaClassMemberCollector implements IClassMemberCollector<ParserRuleContext> {
    JavaMethodVisitor methodVisitor;
    JavaAttributeVisitor attributeVisitor;
    JavaConstructorVisitor constructorVisitor;

    @Override
    public List getClassMethods(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                this::extractMethodsFromClassBody,
                Collections.emptyList()
        );
    }

    private List extractMethodsFromClassBody(JavaParser.ClassDeclarationContext classCtx) {
        List methods = new ArrayList<>();
        classCtx.classBody().classBodyDeclaration().stream()
                .filter(this::isMethodDeclaration)
                .forEach(bodyDecl -> addMethodIfValid(bodyDecl, methods));
        return methods;
    }

    private boolean isMethodDeclaration(JavaParser.ClassBodyDeclarationContext bodyDecl) {
        return bodyDecl.memberDeclaration() != null
                && bodyDecl.memberDeclaration().methodDeclaration() != null;
    }

    private void addMethodIfValid(JavaParser.ClassBodyDeclarationContext bodyDecl, List methods) {
        MethodInformation method = methodVisitor.visitMethod(
                bodyDecl.memberDeclaration().methodDeclaration()
        );
        if (method != null) {
            methods.add(method);
        }
    }

    public List<AttributeInformation> getClassAttributes(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> attributeVisitor.visitClassBody(classCtx.classBody()),
                Collections.emptyList()
        );
    }

    @Override
    public List<ConstructorInformation> getClassConstructors(ParserRuleContext ctx) {
        return (List<ConstructorInformation>) ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> constructorVisitor.visitConstructors(classCtx.classBody()),
                Collections.emptyList()
        );
    }
}

