package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.visitor.domain.services.collectors.classes.JavaClassStructureCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.antlr.v4.runtime.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.usb.argos.ASTProcessor.antlr.*;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;

import java.util.List;

public class JavaClassStructureCollectorTest {
    private JavaClassStructureCollector structureCollector;
    private JavaParser.CompilationUnitContext compilationUnit;

    @BeforeEach
    void setUp() {
        String testClass =
                "public class TestClass extends BaseClass implements Interface1, Interface2 {\n" +
                        "    private String field;\n" +
                        "    public void method() {}\n" +
                        "}";

        CharStream input = CharStreams.fromString(testClass);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        compilationUnit = parser.compilationUnit();

        structureCollector = new JavaClassStructureCollector();
    }

    @Test
    void getSuperClass_ShouldReturnCorrectSuperClass() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        String superClass = structureCollector.getSuperClass(ctx);
        assertEquals("BaseClass", superClass);
    }

    @Test
    void getImplementedInterfaces_ShouldReturnCorrectInterfaces() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<String> interfaces = structureCollector.getImplementedInterfaces(ctx);

        assertEquals(2, interfaces.size());
        assertTrue(interfaces.contains("Interface1"));
        assertTrue(interfaces.contains("Interface2"));
    }
}
