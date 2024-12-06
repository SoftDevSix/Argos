package edu.usb.argos.astprocessor.visitor.classes;

import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassMemberService;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassIdentityService;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassMemberService;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassStructureService;
import edu.usb.argos.astprocessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.astprocessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.astprocessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaClassVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaClassVisitorTest {

    @Test
    void testVisitClassDeclaration() {
        ClassInformation<JavaParser.StatementContext> classInformation = extractClassInformation(getTestJavaSource());
        assertNotNull(classInformation);
        testClassIdentity(classInformation);
        testClassStructure(classInformation);
        testClassMembers(classInformation);
    }

    private String getTestJavaSource() {
        return """
                package com.example;

                public class TestClass extends BaseClass implements InterfaceOne, InterfaceTwo {
                    private int attribute;
                    public TestClass() {}
                    public TestClass(int attribute) { this.attribute = attribute; }
                    public void method() {}
                }
                """;
    }

    private ClassInformation<JavaParser.StatementContext> extractClassInformation(String javaSource) {
        CommonTokenStream tokenStream = tokenize(javaSource);
        JavaParser parser = createParser(tokenStream);
        JavaClassVisitor visitor = createVisitor();
        return visitClass(parser, visitor);
    }

    private CommonTokenStream tokenize(String javaSource) {
        CharStream charStream = CharStreams.fromString(javaSource);
        JavaLexer lexer = new JavaLexer(charStream);
        return new CommonTokenStream(lexer);
    }

    private JavaParser createParser(CommonTokenStream tokenStream) {
        return new JavaParser(tokenStream);
    }

    private JavaClassVisitor createVisitor() {
        JavaMethodVisitor methodVisitor = createMethodVisitor();
        JavaAttributeVisitor attributeVisitor = createAttributeVisitor();
        JavaConstructorVisitor constructorVisitor = new JavaConstructorVisitor();

        JavaClassIdentityService identityService = new JavaClassIdentityService();
        JavaClassStructureService structureService = new JavaClassStructureService();
        IClassMemberService<ParserRuleContext, JavaParser.StatementContext> memberService = createMemberService(
                methodVisitor,
                attributeVisitor,
                constructorVisitor
        );

        return new JavaClassVisitor(identityService, structureService, memberService);
    }

    private JavaMethodVisitor createMethodVisitor() {
        return new JavaMethodVisitor(
                new ModifierService(),
                new ParameterService(),
                new AnnotationService()
        );
    }

    private JavaAttributeVisitor createAttributeVisitor() {
        AttributeHandler attributeHandler = new AttributeHandler();
        return new JavaAttributeVisitor(attributeHandler);
    }

    private JavaClassMemberService createMemberService(
            JavaMethodVisitor methodVisitor,
            JavaAttributeVisitor attributeVisitor,
            JavaConstructorVisitor constructorVisitor
    ) {
        return new JavaClassMemberService(methodVisitor, attributeVisitor, constructorVisitor);
    }

    private ClassInformation<JavaParser.StatementContext> visitClass(JavaParser parser, JavaClassVisitor visitor) {
        JavaParser.CompilationUnitContext context = parser.compilationUnit();
        JavaParser.ClassDeclarationContext classCtx = context.typeDeclaration(0).classDeclaration();
        return visitor.visitClass(classCtx);
    }

    private void testClassIdentity(ClassInformation<JavaParser.StatementContext> classInformation) {
        assertTrue(classInformation.getIdentity().getName().isPresent());
        assertTrue(classInformation.getIdentity().getPackageName().isPresent());
        assertEquals("TestClass", classInformation.getIdentity().getName().get());
        assertEquals("com.example", classInformation.getIdentity().getPackageName().get());
        assertTrue(classInformation.getIdentity().getModifiers().contains("public"));
    }

    private void testClassStructure(ClassInformation<JavaParser.StatementContext> classInformation) {
        assertTrue(classInformation.getStructure().getSuperClass().isPresent());
        assertEquals("BaseClass", classInformation.getStructure().getSuperClass().get());
        assertTrue(classInformation.getStructure().getInterfaces().contains("InterfaceOne"));
        assertTrue(classInformation.getStructure().getInterfaces().contains("InterfaceTwo"));
    }

    private void testClassMembers(ClassInformation<JavaParser.StatementContext> classInformation) {
        assertEquals(1, classInformation.getMembers().getAttributes().size());
        assertEquals(1, classInformation.getMembers().getMethods().size());
        assertEquals(2, classInformation.getMembers().getConstructors().size());
    }
}