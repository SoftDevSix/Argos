package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.ExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.ModifierCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.StatementCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassIdentityCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassStructureCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaClassVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaClassVisitorTest {

    @Test
    void testVisitClassDeclaration() {

        String javaSource = """
                package com.example;
                
                public class TestClass extends BaseClass implements InterfaceOne, InterfaceTwo {
                    private int attribute;
                    public TestClass() {}
                    public TestClass(int attribute) { this.attribute = attribute; }
                    public void method() {}
                }
                """;

        CharStream charStream = CharStreams.fromString(javaSource);
        JavaLexer lexer = new JavaLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        JavaMethodVisitor methodVisitor = new JavaMethodVisitor(
                tokenStream,
                new ExpressionCollector(),
                new StatementCollector(),
                new ModifierCollector()
        );

        AttributeHandler attributeHandler = new AttributeHandler();
        JavaAttributeVisitor attributeVisitor = new JavaAttributeVisitor(attributeHandler);

        JavaConstructorVisitor constructorVisitor = new JavaConstructorVisitor();

        JavaClassIdentityCollector identityCollector = new JavaClassIdentityCollector();
        JavaClassStructureCollector structureCollector = new JavaClassStructureCollector();
        JavaClassMemberCollector memberCollector = new JavaClassMemberCollector(
                methodVisitor,
                attributeVisitor,
                constructorVisitor
        );

        JavaClassVisitor visitor = new JavaClassVisitor(identityCollector, structureCollector, memberCollector);

        JavaParser.CompilationUnitContext context = parser.compilationUnit();
        JavaParser.ClassDeclarationContext classCtx = context.typeDeclaration(0).classDeclaration();

        ClassInformation classInformation = visitor.visitClass(classCtx);

        assertNotNull(classInformation);
        assertEquals("TestClass", classInformation.getIdentity().getName().get());
        assertEquals("com.example", classInformation.getIdentity().getPackageName().get());
        assertTrue(classInformation.getIdentity().getModifiers().contains("public"));
        assertEquals("BaseClass", classInformation.getStructure().getSuperClass().get());
        assertTrue(classInformation.getStructure().getInterfaces().contains("InterfaceOne"));
        assertTrue(classInformation.getStructure().getInterfaces().contains("InterfaceTwo"));
        assertEquals(1, classInformation.getMembers().getAttributes().size());
        assertEquals(1, classInformation.getMembers().getMethods().size());
        assertEquals(2, classInformation.getMembers().getConstructors().size());
    }
}
