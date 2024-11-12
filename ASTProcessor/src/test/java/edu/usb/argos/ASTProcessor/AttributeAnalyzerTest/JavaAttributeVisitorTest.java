package test.java.edu.usb.argos.ASTProcessor.AttributeAnalyzerTest;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer.Entities.AttributeInfo;
import main.java.edu.usb.argos.ASTProcessor.AttributeAnalyzer.JavaAttributeVisitor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaAttributeVisitorTest {

    private JavaAttributeVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new JavaAttributeVisitor();
    }

    private JavaParser.FieldDeclarationContext parseFieldDeclaration(String code) {
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        return parser.fieldDeclaration();
    }

    @Test
    void testVisitAttribute_withModifiers() {
        String code = "public final int value;";
        JavaParser.FieldDeclarationContext ctx = parseFieldDeclaration(code);

        AttributeInfo attributeInfo = visitor.visitAttribute(ctx);

        assertEquals("value", attributeInfo.getName());
        assertNotNull("int", attributeInfo.getType());
        assertNotNull(attributeInfo.getModifiers());
    }

    @Test
    void testVisitAttribute_withoutModifiers() {
        String code = "int value;";
        JavaParser.FieldDeclarationContext ctx = parseFieldDeclaration(code);

        AttributeInfo attributeInfo = visitor.visitAttribute(ctx);

        assertEquals("value", attributeInfo.getName());
        assertEquals("int", attributeInfo.getType());
        assertTrue(attributeInfo.getModifiers().isEmpty());
    }

    @Test
    void testGetAttributeModifiers_withMultipleModifiers() {
        String code = "private final String attribute;";
        JavaParser.FieldDeclarationContext ctx = parseFieldDeclaration(code);

        List<String> modifiers = visitor.getAttributeModifiers(ctx);

        assertNotNull(modifiers);
    }

    @Test
    void testGetAttributeType() {
        String code = "double number;";
        JavaParser.FieldDeclarationContext ctx = parseFieldDeclaration(code);

        String type = visitor.getAttributeType(ctx);

        assertEquals("double", type);
    }

    @Test
    void testGetAttributeName() {
        String code = "boolean number;";
        JavaParser.FieldDeclarationContext ctx = parseFieldDeclaration(code);

        String name = visitor.getAttributeName(ctx);

        assertEquals("number", name);
    }
}
