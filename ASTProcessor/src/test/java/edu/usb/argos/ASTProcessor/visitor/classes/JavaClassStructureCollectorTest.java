package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassStructureCollector;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void getSuperClassShouldReturnCorrectSuperClass() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        Optional<String> superClass = structureCollector.getSuperClass(ctx);
        assertEquals("BaseClass", superClass.get());
    }

    @Test
    void getImplementedInterfacesShouldReturnCorrectInterfaces() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        List<String> interfaces = structureCollector.getImplementedInterfaces(ctx);

        assertEquals(2, interfaces.size());
        assertTrue(interfaces.contains("Interface1"));
        assertTrue(interfaces.contains("Interface2"));
    }
}
