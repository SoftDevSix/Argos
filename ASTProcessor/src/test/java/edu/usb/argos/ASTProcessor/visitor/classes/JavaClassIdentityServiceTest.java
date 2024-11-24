package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.AnnotationInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.classes.JavaClassIdentityService;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JavaClassIdentityServiceTest {
    private JavaClassIdentityService classIdentityService;
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
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        compilationUnit = parser.compilationUnit();
        classIdentityService = new JavaClassIdentityService();
    }

    @Test
    void getClassNameShouldReturnCorrectClassName() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        Optional<String> className = classIdentityService.getClassName(ctx);

        assertEquals("TestClass", className.get());
    }

    @Test
    void getPackageNameShouldReturnCorrectPackage() {
        Optional<String> packageName = classIdentityService.getPackageName(compilationUnit.packageDeclaration());
        assertEquals("com.example", packageName.get());
    }

    @Test
    void getClassModifiersShouldReturnCorrectModifiers() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<String> modifiers = classIdentityService.getClassModifiers(ctx);
        assertTrue(modifiers.contains("public"));
    }

    @Test
    void getClassAnnotationsShouldReturnCorrectAnnotations() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<AnnotationInformation> annotations = classIdentityService.getClassAnnotations(ctx);

        assertFalse(annotations.isEmpty());
        assertEquals("TestAnnotation", annotations.get(0).getName());
        assertEquals("test", annotations.get(0).getAttributes().get("value"));
    }
}