package edu.usb.argos.ASTProcessor.AttributeAnalyzerTest;

import edu.usb.argos.ASTProcessor.AttributeAnalyzer.AttributeHandler;
import edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;
import edu.usb.argos.ASTProcessor.AttributeAnalyzer.JavaAttributeVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JavaAttributeVisitorTest {

    private static JavaAttributeVisitor visitor;

    @BeforeAll
    public static void init() {
        AttributeHandler attributeHandler = new AttributeHandler();
        visitor = new JavaAttributeVisitor(attributeHandler);
    }

    private Optional<JavaParser.ClassBodyContext> getClassFromText(String path) throws IOException {
        CharStream input = CharStreams.fromString(path);

        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        JavaParser.CompilationUnitContext compilationUnitContext = parser.compilationUnit();

        if (!compilationUnitContext.typeDeclaration().isEmpty()) {
            JavaParser.TypeDeclarationContext typeDecl = compilationUnitContext.typeDeclaration().get(0);
            if (typeDecl.classDeclaration() != null) {
                return Optional.ofNullable(typeDecl.classDeclaration().classBody());
            }
        }

        return Optional.empty();
    }

    @Test
    void testVisitAttribute_withModifiers() throws IOException {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                    public String name;
                    protected double ratio = 3.14;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        for (AttributeInfo attributeInfo : visitor.visitAttribute(classFound.get())) {
            assertFalse(visitor.getAttributeModifiers(attributeInfo).isEmpty());
        }
    }

    @Test
    void testVisitAttribute_withoutModifiers() throws IOException {
        String classBody = """
                public class Example {
                    int someValue = 12;
                    String name;
                    double ratio = 3.14;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        for (AttributeInfo attributeInfo : visitor.visitAttribute(classFound.get())) {
            assertTrue(attributeInfo.getModifiers().isEmpty());
        }
    }

    @Test
    void testGetAttributeType() throws IOException {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        Optional<AttributeInfo> attributeInfo = Optional.ofNullable(visitor.visitAttribute(classFound.get()).get(0));
        assertFalse(attributeInfo.isEmpty());
        assertEquals("int", visitor.getAttributeType(attributeInfo.get()));
    }

    @Test
    void testGetAttributeName() throws IOException {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        Optional<AttributeInfo> attributeInfo = Optional.ofNullable(visitor.visitAttribute(classFound.get()).get(0));
        assertFalse(attributeInfo.isEmpty());
        assertEquals("someValue", attributeInfo.get().getName());
    }
}
