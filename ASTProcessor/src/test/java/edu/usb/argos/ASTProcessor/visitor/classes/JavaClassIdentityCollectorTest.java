package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.AnnotationInfo;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassIdentityCollector;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JavaClassIdentityCollectorTest {
    private JavaClassIdentityCollector classIdentityCollector;
    private CommonTokenStream tokenStream;
    private JavaParser parser;
    private JavaParser.CompilationUnitContext compilationUnit;

    @BeforeEach
    void setUp() {
        String testClass =
                "package com.example;\n" +
                        "\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "@TestAnnotation(value = \"test\")\n" +
                        "public class TestClass extends BaseClass implements Interface1, Interface2 {\n" +
                        "    private String field;\n" +
                        "    public void method() {}\n" +
                        "}";

        CharStream input = CharStreams.fromString(testClass);
        JavaLexer lexer = new JavaLexer(input);
        tokenStream = new CommonTokenStream(lexer);
        parser = new JavaParser(tokenStream);
        compilationUnit = parser.compilationUnit();
        classIdentityCollector = new JavaClassIdentityCollector();
    }

    @Test
    void getClassNameShouldReturnCorrectClassName() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        Optional<String> className = classIdentityCollector.getClassName(ctx);

        assertEquals("TestClass", className.get());
    }

    @Test
    void getPackageNameShouldReturnCorrectPackage() {
        Optional<String> packageName = classIdentityCollector.getPackageName(compilationUnit.packageDeclaration());
        assertEquals("com.example", packageName.get());
    }

    @Test
    void getClassModifiersShouldReturnCorrectModifiers() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<String> modifiers = classIdentityCollector.getClassModifiers(ctx);
        assertTrue(modifiers.contains("public"));
    }

    @Test
    void getClassAnnotationsShouldReturnCorrectAnnotations() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<AnnotationInfo> annotations = classIdentityCollector.getClassAnnotations(ctx);

        assertFalse(annotations.isEmpty());
        assertEquals("TestAnnotation", annotations.get(0).getName());
        assertEquals("test", annotations.get(0).getAttributes().get("value"));
    }
}