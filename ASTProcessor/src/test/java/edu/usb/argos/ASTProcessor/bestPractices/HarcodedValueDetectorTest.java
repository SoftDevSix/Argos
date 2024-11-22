package edu.usb.argos.ASTProcessor.bestPractices;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.bestPractices.HardcodedValueDetector;
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

import static org.junit.jupiter.api.Assertions.*;


public class HarcodedValueDetectorTest {
    @Test
    void testDetectHardcodedValuesInClass() {

        String javaSource = """
                package com.example;
                
                public class TestClass {
                    private int attribute;
                    private String text;
                    
                    public TestClass() {
                        attribute = 40;
                        text = "Hardcoded String Constructor";
                    }
                    
                    public void method() {
                        int number = 100;
                        int x = attribute + 1;
                        System.out.println("Hardcoded Message");
                    }
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

        HardcodedValueDetector detector = new HardcodedValueDetector();
        detector.detectHardcodedValues(classInformation);

//        assertEquals(4, detector.getHardcodedValues().size());
        System.out.println(detector.getHardcodedValues());

        assertTrue(detector.getHardcodedValues().contains("100"));
        assertTrue(detector.getHardcodedValues().contains("40"));
        assertTrue(detector.getHardcodedValues().contains("\"Hardcoded String Constructor\""));
        assertTrue(detector.getHardcodedValues().contains("\"Hardcoded Message\""));
    }
}
