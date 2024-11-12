package edu.usb.argos.ASTProcessor.visitor;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.domain.entities.method.*;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavaMethodVisitorTest {
    private JavaMethodVisitor visitor;
    private CommonTokenStream tokenStream;

    @BeforeEach
    void setUp() {
        tokenStream = new CommonTokenStream(new JavaLexer(CharStreams.fromString("")));
        visitor = new JavaMethodVisitor(tokenStream);
    }

    private JavaParser.MethodDeclarationContext parseMethod(String methodCode) {
        String wrappedCode = "class Test { " + methodCode + " }";
        JavaLexer lexer = new JavaLexer(CharStreams.fromString(wrappedCode));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        return parser.classDeclaration()
                .classBody()
                .classBodyDeclaration(0)
                .memberDeclaration()
                .methodDeclaration();
    }

    @Test
    void visitMethod_SimpleMethod_ShouldParseCorrectly() {
        String methodCode = "public void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        MethodInfo methodInfo = visitor.visitMethod(ctx);

        assertNotNull(methodInfo);
        assertEquals("testMethod", methodInfo.getName());
        assertEquals("void", methodInfo.getReturnType());
        assertTrue(methodInfo.getModifiers().contains("public"));
        assertTrue(methodInfo.getParameters().isEmpty());
    }

    @Test
    void visitMethod_WithParameters_ShouldParseCorrectly() {
        String methodCode = "public int calculateSum(int a, String b) { return a; }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        MethodInfo methodInfo = visitor.visitMethod(ctx);

        List<ParameterInfo> params = methodInfo.getParameters();
        assertEquals(2, params.size());

        assertEquals("a", params.get(0).getName());
        assertEquals("int", params.get(0).getType());

        assertEquals("b", params.get(1).getName());
        assertEquals("String", params.get(1).getType());
    }

    @Test
    void visitMethod_WithMultipleModifiers_ShouldParseCorrectly() {
        String methodCode = "public static final void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        MethodInfo methodInfo = visitor.visitMethod(ctx);

        List<String> modifiers = methodInfo.getModifiers();
        assertTrue(modifiers.contains("public"));
        assertTrue(modifiers.contains("static"));
        assertTrue(modifiers.contains("final"));
    }

    @Test
    void visitMethod_WithStatements_ShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    System.out.println(\"Hello\");\n" +
                        "    if (true) {\n" +
                        "        System.out.println(\"True\");\n" +
                        "    }\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        MethodInfo methodInfo = visitor.visitMethod(ctx);

        List<JavaParser.StatementContext> statements = methodInfo.getStatements();

        assertEquals(2, statements.size(),
                "Expected 2 statements: println and if statement");

        assertNotNull(statements.get(0), "First statement (println) should not be null");
        assertNotNull(statements.get(1), "Second statement (if) should not be null");
    }

    @Test
    void visitMethod_WithExpressionStatement_ShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    System.out.println(\"Test\");\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        MethodInfo methodInfo = visitor.visitMethod(ctx);

        List<JavaParser.StatementContext> statements = methodInfo.getStatements();
        assertEquals(1, statements.size(), "Expected 1 statement");
        assertNotNull(statements.get(0), "Statement should not be null");
    }

    @Test
    void visitMethod_WithIfStatement_ShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    if (true) {\n" +
                        "        System.out.println(\"True\");\n" +
                        "    }\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        MethodInfo methodInfo = visitor.visitMethod(ctx);

        List<JavaParser.StatementContext> statements = methodInfo.getStatements();
        assertEquals(1, statements.size(), "Expected 1 if statement");
        assertNotNull(statements.get(0), "If statement should not be null");
    }

    @Test
    void visitMethod_WithComplexStatements_ShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    if (true) {\n" +
                        "        System.out.println(\"true\");\n" +
                        "    }\n" +
                        "    return;\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        MethodInfo methodInfo = visitor.visitMethod(ctx);

        List<JavaParser.StatementContext> statements = methodInfo.getStatements();
        assertEquals(2, statements.size(),
                "Expected 2 statements: if statement and return statement");
    }


    @Test
    void getMethodModifiers_WithNoModifiers_ShouldReturnEmptyList() {
        String methodCode = "void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        List<String> modifiers = visitor.getMethodModifiers(ctx);

        assertTrue(modifiers.isEmpty());
    }

    @Test
    void getReturnType_ComplexType_ShouldParseCorrectly() {
        String methodCode = "public List<String> testMethod() { return null; }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        String returnType = visitor.getReturnType(ctx);

        assertEquals("List<String>", returnType);
    }

    @Test
    void getParameters_WithArrayParameter_ShouldParseCorrectly() {
        String methodCode = "public void testMethod(String[] args) { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        List<ParameterInfo> parameters = visitor.getParameters(ctx);

        assertEquals(1, parameters.size());
        assertEquals("args", parameters.get(0).getName());
        assertEquals("String[]", parameters.get(0).getType());
    }
}