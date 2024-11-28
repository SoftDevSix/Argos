package edu.usb.argos.astprocessor.visitor.classes;

import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.AnnotationInformation;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassIdentityService;
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
                """
                        package com.example;
                        
                        import java.util.List;
                        
                        @TestAnnotation(value = "test")
                        public class TestClass extends BaseClass implements Interface1, Interface2 {
                            private String field;
                            public void method() {}
                        }""";

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

        assertTrue(className.isPresent());
        assertEquals("TestClass", className.get());
    }

    @Test
    void getPackageNameShouldReturnCorrectPackage() {
        Optional<String> packageName = classIdentityService.getPackageName(compilationUnit.packageDeclaration());

        assertTrue(packageName.isPresent());
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

    @Test
    void getClassNameShouldHandleInvalidContext() {
        Optional<String> className = classIdentityService.getClassName(null);
        assertEquals(Optional.empty(), className);
    }

    @Test
    void getPackageNameShouldReturnDefaultWhenNoPackageDeclaration() {
        String javaClassWithoutPackage = """
            class TestClass {}
            """;
        CharStream input = CharStreams.fromString(javaClassWithoutPackage);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();

        Optional<String> packageName = classIdentityService.getPackageName(compilationUnit);
        assertEquals(Optional.empty(), packageName);
    }

    @Test
    void getClassModifiersShouldReturnEmptyWhenNoModifiers() {
        String classWithoutModifiers = """
            class TestClass {}
            """;
        CharStream input = CharStreams.fromString(classWithoutModifiers);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        List<String> modifiers = classIdentityService.getClassModifiers(ctx);
        assertTrue(modifiers.isEmpty());
    }

    @Test
    void getClassAnnotationsShouldReturnEmptyWhenNoAnnotations() {
        String classWithoutAnnotations = """
            public class TestClass {}
            """;
        CharStream input = CharStreams.fromString(classWithoutAnnotations);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);
        JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        List<AnnotationInformation> annotations = classIdentityService.getClassAnnotations(ctx);
        assertTrue(annotations.isEmpty());
    }
}