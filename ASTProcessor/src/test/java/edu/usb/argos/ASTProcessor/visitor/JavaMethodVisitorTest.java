package edu.usb.argos.ASTProcessor.visitor;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Expression;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.ExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.ModifierCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.StatementCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
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
        visitor = new JavaMethodVisitor(
                tokenStream,
                new ExpressionCollector(),
                new StatementCollector(),
                new ModifierCollector());
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
    void visitMethodSimpleMethodShouldParseCorrectly() {
        String methodCode = "public void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        var methodInfo = visitor.visitMethod(ctx);

        assertNotNull(methodInfo);
        assertEquals("testMethod", methodInfo.getName());
        assertEquals("void", methodInfo.getReturnType());
        assertTrue(methodInfo.getModifiers().contains("public"));
        assertTrue(methodInfo.getParameters().isEmpty());
    }

    @Test
    void visitMethodWithParametersShouldParseCorrectly() {
        String methodCode = "public int calculateSum(int a, String b) { return a; }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        var methodInfo = visitor.visitMethod(ctx);

        List<ParameterInformation> params = methodInfo.getParameters();
        assertEquals(2, params.size());

        assertEquals("a", params.get(0).getName());
        assertEquals("int", params.get(0).getType());

        assertEquals("b", params.get(1).getName());
        assertEquals("String", params.get(1).getType());
    }

    @Test
    void visitMethodWithMultipleModifiersShouldParseCorrectly() {
        String methodCode = "public static final void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        var methodInfo = visitor.visitMethod(ctx);

        List<String> modifiers = methodInfo.getModifiers();
        assertTrue(modifiers.contains("public"));
        assertTrue(modifiers.contains("static"));
        assertTrue(modifiers.contains("final"));
    }

    @Test
    void visitMethodWithStatementsShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    System.out.println(\"Hello\");\n" +
                        "    if (true) {\n" +
                        "        System.out.println(\"True\");\n" +
                        "    }\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        var methodInfo = visitor.visitMethod(ctx);

        List<Statement<JavaParser.StatementContext>> statements = methodInfo.getStatements();

        assertEquals(2, statements.size(),
                "Expected 2 statements: println and if statement");

        assertNotNull(statements.get(0), "First statement (println) should not be null");
        assertNotNull(statements.get(1), "Second statement (if) should not be null");
    }

    @Test
    void visitMethodWithExpressionStatementShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    System.out.println(\"Test\");\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        var methodInfo = visitor.visitMethod(ctx);

        List<Statement<JavaParser.StatementContext>> statements = methodInfo.getStatements();
        assertEquals(1, statements.size(), "Expected 1 statement");
        assertNotNull(statements.get(0), "Statement should not be null");
    }

    @Test
    void visitMethodWithIfStatementShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    if (true) {\n" +
                        "        System.out.println(\"True\");\n" +
                        "    }\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        var methodInfo = visitor.visitMethod(ctx);

        List<Statement<JavaParser.StatementContext>> statements = methodInfo.getStatements();
        assertEquals(1, statements.size(), "Expected 1 if statement");
        assertNotNull(statements.get(0), "If statement should not be null");
    }

    @Test
    void visitMethodWithComplexStatementsShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    if (true) {\n" +
                        "        System.out.println(\"true\");\n" +
                        "    }\n" +
                        "    return;\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        var methodInfo = visitor.visitMethod(ctx);

        List<Statement<JavaParser.StatementContext>> statements = methodInfo.getStatements();
        assertEquals(2, statements.size(),
                "Expected 2 statements: if statement and return statement");
    }

    @Test
    void visitMethodWithExpressionsShouldParseCorrectly() {
        String methodCode =
                "public void testMethod() {\n" +
                        "    System.out.println(\"Test\" + \"Hello\");\n" +
                        "}";

        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);
        var methodInfo = visitor.visitMethod(ctx);

        List<Expression<JavaParser.ExpressionContext>> expressions = methodInfo.getExpressions();
        assertFalse(expressions.isEmpty(), "Should have expressions");

        assertNotNull(expressions.get(0).getNode(), "Expression node should not be null");
    }

    @Test
    void visitMethodVerifyTokenStreamShouldBeAccessible() {
        String methodCode = "public void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        var methodInfo = visitor.visitMethod(ctx);
        assertNotNull(methodInfo.getTokens().getTokenStream(),
                "Token stream should be accessible");
    }

    @Test
    void getMethodModifiersWithNoModifiersShouldReturnEmptyList() {
        String methodCode = "void testMethod() { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        List<String> modifiers = visitor.getMethodModifiers(ctx);

        assertTrue(modifiers.isEmpty());
    }

    @Test
    void getReturnTypeComplexTypeShouldParseCorrectly() {
        String methodCode = "public List<String> testMethod() { return null; }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        String returnType = visitor.getReturnType(ctx);

        assertEquals("List<String>", returnType);
    }

    @Test
    void getParametersWithArrayParameterShouldParseCorrectly() {
        String methodCode = "public void testMethod(String[] args) { }";
        JavaParser.MethodDeclarationContext ctx = parseMethod(methodCode);

        List<ParameterInformation> parameters = visitor.getParameters(ctx);

        assertEquals(1, parameters.size());
        assertEquals("args", parameters.get(0).getName());
        assertEquals("String[]", parameters.get(0).getType());
    }
}
