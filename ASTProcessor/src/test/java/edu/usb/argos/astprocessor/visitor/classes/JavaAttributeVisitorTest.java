package edu.usb.argos.astprocessor.visitor.classes;

import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;
import edu.usb.argos.astprocessor.visitor.core.entities.method.AttributeInformation;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaAttributeVisitorTest {

    private static JavaAttributeVisitor visitor;

    @BeforeAll
    public static void init() {
        AttributeHandler attributeHandler = new AttributeHandler();
        visitor = new JavaAttributeVisitor(attributeHandler);
    }

    private Optional<JavaParser.ClassBodyContext> getClassFromText(String path) {
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
    void testExtractAttributes_WithEmptyVariableDeclarator() {
        String classContent = """
                class TestClass {
                    private int;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<AttributeInformation> attributes = visitor.visitAttribute(classBody.get());
        
        assertTrue(classBody.isPresent());
        assertTrue(attributes.isEmpty());
    }

    @Test
    void testExtractAttributes_WithFieldDeclaration() {
        String classContent = """
                class TestClass {
                    private int attribute1;
                    public String attribute2;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<AttributeInformation> attributes = visitor.visitAttribute(classBody.get());
        
        assertTrue(classBody.isPresent());
        assertEquals(2, attributes.size());
        assertEquals("attribute1", attributes.get(0).getName());
        assertEquals("int", attributes.get(0).getType());
        assertEquals("attribute2", attributes.get(1).getName());
        assertEquals("String", attributes.get(1).getType());
    }

    @Test
    void testExtractAttributes_WithoutFieldDeclaration() {
        String classContent = """
                class TestClass {
                    public void method1() {}
                    public static void main(String[] args) {}
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<AttributeInformation> attributes = visitor.visitAttribute(classBody.get());
        
        assertTrue(classBody.isPresent());
        assertTrue(attributes.isEmpty());
    }

    @Test
    void testExtractAttributes_NullMemberContext() {
        String classContent = """
                class TestClass {
                }
                """;

        Optional<JavaParser.ClassBodyContext> classBody = getClassFromText(classContent);
        List<AttributeInformation> attributes = visitor.visitAttribute(classBody.get());
        
        assertTrue(classBody.isPresent());
        assertTrue(attributes.isEmpty());
    }

    @Test
    void testVisitAttributeWithModifiers() {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                    public String name;
                    protected double ratio = 3.14;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        for (AttributeInformation attributeInfo : visitor.visitAttribute(classFound.get())) {
            assertFalse(visitor.getAttributeModifiers(attributeInfo).isEmpty());
        }
    }

    @Test
    void testVisitAttributeWithoutModifiers() {
        String classBody = """
                public class Example {
                    int someValue = 12;
                    String name;
                    double ratio = 3.14;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        for (AttributeInformation attributeInfo : visitor.visitAttribute(classFound.get())) {
            assertTrue(attributeInfo.getModifiers().isEmpty());
        }
    }

    @Test
    void testGetAttributeType() {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        Optional<AttributeInformation> attributeInfo = Optional.ofNullable(visitor.visitAttribute(classFound.get()).get(0));
        assertFalse(attributeInfo.isEmpty());
        assertEquals("int", visitor.getAttributeType(attributeInfo.get()));
    }

    @Test
    void testGetAttributeName() {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertFalse(classFound.isEmpty());
        Optional<AttributeInformation> attributeInfo = Optional.ofNullable(visitor.visitAttribute(classFound.get()).get(0));
        assertFalse(attributeInfo.isEmpty());
        assertEquals("someValue", attributeInfo.get().getName());
    }

    @Test
    void testVisitMultipleAttributesWithoutModifiers() {
        String classBody = """
                public class Example {
                    int someValue = 12;
                    String name;
                    double value = 3.14;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInformation> attributes = visitor.visitAttribute(classFound.get());
        AttributeInformation attribute1 = attributes.get(0);
        AttributeInformation attribute2 = attributes.get(1);
        AttributeInformation attribute3 = attributes.get(2);

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
    void testVisitAttributeWithPrivateModifier() {
        String classBody = """
                public class Example {
                    private int someValue = 12;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInformation> attributes = visitor.visitAttribute(classFound.get());
        AttributeInformation attribute = attributes.get(0);

        assertEquals(1, attributes.size());
        assertEquals("someValue", attribute.getName());
        assertEquals("int", attribute.getType());
        assertEquals(List.of("private"), attribute.getModifiers());
    }

    @Test
    void testVisitAttributeWithFinalModifier() {
        String classBody = """
                public class Example {
                    public final int VALUE = 42;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInformation> attributes = visitor.visitAttribute(classFound.get());
        AttributeInformation attribute = attributes.get(0);

        assertEquals("VALUE", attribute.getName());
        assertEquals("int", attribute.getType());
        assertEquals(List.of("public", "final"), attribute.getModifiers());
    }

    @Test
    void testVisitAttributeWithMultipleModifiers() {
        String classBody = """
                public class Example {
                    public static final String VALUE = "value";
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInformation> attributes = visitor.visitAttribute(classFound.get());
        AttributeInformation attribute = attributes.get(0);

        assertEquals("VALUE", attribute.getName());
        assertEquals("String", attribute.getType());
        assertEquals(List.of("public", "static", "final"), attribute.getModifiers());
    }

    @Test
    void testVisitAttributeWithTwoAttributesAndModifiers() {
        String classBody = """
                public class Example {
                    private int id;
                    protected String name;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        List<AttributeInformation> attributes = visitor.visitAttribute(classFound.get());
        AttributeInformation attribute1 = attributes.get(0);
        AttributeInformation attribute2 = attributes.get(1);

        assertEquals(2, attributes.size());
        assertEquals("id", attribute1.getName());
        assertEquals("int", attribute1.getType());
        assertEquals(List.of("private"), attribute1.getModifiers());

        assertEquals("name", attribute2.getName());
        assertEquals("String", attribute2.getType());
        assertEquals(List.of("protected"), attribute2.getModifiers());
    }

    @Test
    void testVisitAttributeWithTwoAttributesAndMultipleModifiers() {
        String classBody = """
                public class Example {
                    private static int id;
                    protected final String NAME;
                }
                """;

        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);
        List<AttributeInformation> attributes = visitor.visitAttribute(classFound.get());
        AttributeInformation attribute1 = attributes.get(0);
        AttributeInformation attribute2 = attributes.get(1);

        assertEquals("id", attribute1.getName());
        assertEquals("int", attribute1.getType());
        assertEquals(List.of("private", "static"), attribute1.getModifiers());

        assertEquals("NAME", attribute2.getName());
        assertEquals("String", attribute2.getType());
        assertEquals(List.of("protected", "final"), attribute2.getModifiers());
    }

    @Test
    void testVisitAttributeWithInitialValues() {
        String classBody = """
                public class Example {
                    private final int someValue = 12;
                    public String name = "John";
                    protected double ratio = 3.14;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertTrue(classFound.isPresent());
        List<AttributeInformation> attributesInfo = visitor.visitAttribute(classFound.get());

        assertFalse(attributesInfo.isEmpty());
        for (AttributeInformation attributeInfo : attributesInfo) {
            if (attributeInfo.getName().equals("someValue")) {
                assertEquals("12", attributeInfo.getValue().orElse(""));
            }
            if (attributeInfo.getName().equals("name")) {
                assertEquals("\"John\"", attributeInfo.getValue().orElse(""));
            }
            if (attributeInfo.getName().equals("ratio")) {
                assertEquals("3.14", attributeInfo.getValue().orElse(""));
            }
        }
    }

    @Test
    void testVisitAttributeWithoutInitialValues() {
        String classBody = """
                public class Example {
                    int someValue;
                    String name;
                    double ratio;
                }
                """;
        Optional<JavaParser.ClassBodyContext> classFound = getClassFromText(classBody);

        assertTrue(classFound.isPresent());
        List<AttributeInformation> attributesInfo = visitor.visitAttribute(classFound.get());

        assertFalse(attributesInfo.isEmpty());
        for (AttributeInformation attributeInfo : attributesInfo) {
            assertTrue(attributeInfo.getValue().isEmpty());
        }
    }

}
