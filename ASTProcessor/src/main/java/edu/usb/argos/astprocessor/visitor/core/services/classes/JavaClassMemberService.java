package edu.usb.argos.astprocessor.visitor.core.services.classes;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassMemberService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.shared.validation.ContextValidator;
import lombok.Data;
import lombok.Generated;
import lombok.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Value
@Generated
public class JavaClassMemberService implements IClassMemberService<ParserRuleContext, JavaParser.StatementContext> {
    JavaMethodVisitor methodVisitor;
    JavaAttributeVisitor attributeVisitor;
    JavaConstructorVisitor constructorVisitor;

    @Override
    public List<MethodInformation<JavaParser.StatementContext>> getClassMethods(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                this::extractMethodsFromClassBody,
                Collections.emptyList()
        );
    }

    private List<MethodInformation<JavaParser.StatementContext>> extractMethodsFromClassBody(
            JavaParser.ClassDeclarationContext classCtx) {
        List<MethodInformation<JavaParser.StatementContext>> methods = new ArrayList<>();
        classCtx.classBody().classBodyDeclaration().stream()
                .filter(this::isMethodDeclaration)
                .forEach(bodyDecl -> addMethodIfValid(bodyDecl, methods));
        return methods;
    }

    private boolean isMethodDeclaration(JavaParser.ClassBodyDeclarationContext bodyDecl) {
        return bodyDecl.memberDeclaration() != null
                && bodyDecl.memberDeclaration().methodDeclaration() != null;
    }

    private void addMethodIfValid(
            JavaParser.ClassBodyDeclarationContext bodyDecl,
            List<MethodInformation<JavaParser.StatementContext>> methods) {
        MethodInformation<JavaParser.StatementContext> method = methodVisitor.visitMethodDeclaration(
                bodyDecl.memberDeclaration().methodDeclaration()
        );
        if (method != null) {
            methods.add(method);
        }
    }

    @Override
    public List<AttributeInformation> getClassAttributes(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> attributeVisitor.visitClassBody(classCtx.classBody()),
                Collections.emptyList()
        );
    }

    @Override
    public List<ConstructorInformation<JavaParser.StatementContext>> getClassConstructors(ParserRuleContext ctx) {
        return ContextValidator.validateAndExecute(
                ctx,
                JavaParser.ClassDeclarationContext.class,
                classCtx -> constructorVisitor.visitConstructors(classCtx.classBody()),
                Collections.emptyList()
        );
    }
}