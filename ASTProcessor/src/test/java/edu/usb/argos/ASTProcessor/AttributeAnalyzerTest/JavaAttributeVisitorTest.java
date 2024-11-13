package edu.usb.argos.ASTProcessor.AttributeAnalyzerTest;

import edu.usb.argos.ASTProcessor.AttributeAnalyzer.AttributeHandlers.AttributeHandler;
import edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;
import edu.usb.argos.ASTProcessor.AttributeAnalyzer.AttributeAnalyzerVisitor.JavaAttributeVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;

import java.io.IOException;
import java.util.List;
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

    @Test
    void testVisitMultipleAttributes_withoutModifiers() throws IOException {
        String classBody = """
                public class Example {
                    int someValue = 12;
                    String name;
                    double value = 3.14;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInfo> attributes = visitor.visitAttribute(classFound.get());
        AttributeInfo attribute1 = attributes.get(0);
        AttributeInfo attribute2 = attributes.get(1);
        AttributeInfo attribute3 = attributes.get(2);

        assertEquals("someValue", attribute1.getName());
        assertEquals("int", attribute1.getType());
        assertTrue(attribute1.getModifiers().isEmpty());

        assertEquals("name", attribute2.getName());
        assertEquals("String", attribute2.getType());
        assertTrue(attribute2.getModifiers().isEmpty());

        assertEquals("value", attribute3.getName());
        assertEquals("double", attribute3.getType());
        assertTrue(attribute3.getModifiers().isEmpty());
    }

    @Test
    void testVisitAttribute_withPrivateModifier() throws IOException {
        String classBody = """
                public class Example {
                    private int someValue = 12;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInfo> attributes = visitor.visitAttribute(classFound.get());
        AttributeInfo attribute = attributes.get(0);

        assertEquals(1, attributes.size());
        assertEquals("someValue", attribute.getName());
        assertEquals("int", attribute.getType());
        assertEquals(List.of("private"), attribute.getModifiers());
    }

    @Test
    void testVisitAttribute_withFinalModifier() throws IOException {
        String classBody = """
                public class Example {
                    public final int VALUE = 42;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInfo> attributes = visitor.visitAttribute(classFound.get());
        AttributeInfo attribute = attributes.get(0);

        assertEquals("VALUE", attribute.getName());
        assertEquals("int", attribute.getType());
        assertEquals(List.of("public", "final"), attribute.getModifiers());
    }

    @Test
    void testVisitAttribute_withMultipleModifiers() throws IOException {
        String classBody = """
                public class Example {
                    public static final String VALUE = "value";
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInfo> attributes = visitor.visitAttribute(classFound.get());
        AttributeInfo attribute = attributes.get(0);

        assertEquals("VALUE", attribute.getName());
        assertEquals("String", attribute.getType());
        assertEquals(List.of("public", "static", "final"), attribute.getModifiers());
    }

    @Test
    void testVisitAttribute_withTwoAttributesAndModifiers() throws IOException {
        String classBody = """
                public class Example {
                    private int id;
                    protected String name;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        List<AttributeInfo> attributes = visitor.visitAttribute(classFound.get());
        AttributeInfo attribute1 = attributes.get(0);
        AttributeInfo attribute2 = attributes.get(1);

        assertEquals(2, attributes.size());
        assertEquals("id", attribute1.getName());
        assertEquals("int", attribute1.getType());
        assertEquals(List.of("private"), attribute1.getModifiers());

        assertEquals("name", attribute2.getName());
        assertEquals("String", attribute2.getType());
        assertEquals(List.of("protected"), attribute2.getModifiers());
    }

    @Test
    void testVisitAttribute_withTwoAttributesAndMultipleModifiers() throws IOException {
        String classBody = """
                public class Example {
                    private static int id;
                    protected final String NAME;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInfo> attributes = visitor.visitAttribute(classFound.get());
        AttributeInfo attribute1 = attributes.get(0);

        assertEquals("id", attribute1.getName());
        assertEquals("int", attribute1.getType());
        assertEquals(List.of("private", "static"), attribute1.getModifiers());

        AttributeInfo attribute2 = attributes.get(1);
        assertEquals("NAME", attribute2.getName());
        assertEquals("String", attribute2.getType());
        assertEquals(List.of("protected", "final"), attribute2.getModifiers());
    }
}
