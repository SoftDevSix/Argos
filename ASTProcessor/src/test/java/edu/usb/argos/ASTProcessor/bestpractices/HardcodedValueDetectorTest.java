package edu.usb.argos.ASTProcessor.bestpractices;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HardcodedValueDetectorTest {
    @Test
    void testDetectHardcodedValuesInClass() {
        String javaSource = """
                package com.example;
                
                public class TestClass {
                    private int attribute;
                    private String text;
                    private String hardcoded = "Text";
                 
                    public TestClass() {
                        attribute = 40;
                        text = "Hardcoded String Constructor";
                        method("hola");
                        method(1, 2);
                    }
                    
                    public void method() {
                        int number = 100;
                        int x = attribute + 1;
                        System.out.println("Hardcoded Message");
                        methodCall(1, 2, 3);
                        callMethod();
                    }
                }
                """;

        CharStream charStream = CharStreams.fromString(javaSource);
        JavaLexer lexer = new JavaLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        JavaParser.CompilationUnitContext context = parser.compilationUnit();
        JavaParser.ClassDeclarationContext classCtx = context.typeDeclaration(0).classDeclaration();

        HardcodedValueDetector detector = new HardcodedValueDetector(classCtx);
        detector.detectHardcodedValues();

//        assertEquals(4, detector.getHardcodedValues().size());
        System.out.println(detector.getHardcodedValues());

//        assertTrue(detector.getHardcodedValues().contains("100"));
//        assertTrue(detector.getHardcodedValues().contains("40"));
//        assertTrue(detector.getHardcodedValues().contains("\"Hardcoded String Constructor\""));
//        assertTrue(detector.getHardcodedValues().contains("\"Hardcoded Message\""));
    }
}
