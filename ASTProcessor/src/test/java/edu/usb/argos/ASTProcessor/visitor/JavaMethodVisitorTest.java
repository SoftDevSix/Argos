package edu.usb.argos.ASTProcessor.visitor;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JavaMethodVisitorTest {
    private JavaMethodVisitor visitor;
    private ModifierService modifierService;
    private ParameterService parameterService;
    private AnnotationService annotationService;

    @BeforeEach
    void setUp() {
        modifierService = new ModifierService();
        parameterService = new ParameterService();
        annotationService = new AnnotationService();
        visitor = new JavaMethodVisitor(modifierService, parameterService, annotationService);
    }

    private JavaParser.MethodDeclarationContext parseMethod(String code) {
        try {
            JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();

            return compilationUnit
                    .typeDeclaration(0)
                    .classDeclaration()
                    .classBody()
                    .classBodyDeclaration(0)
                    .memberDeclaration()
                    .methodDeclaration();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    void testSimpleMethod() {
        String code = """
                public class Test {
                    public void simpleMethod() {
                        int x = 1;
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        assertNotNull(ctx, "Method context should not be null");

        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
        assertNotNull(info, "Method information should not be null");

        assertEquals("simpleMethod", info.getName());
        assertEquals("void", info.getReturnType());
        assertTrue(info.getParameters().isEmpty());
        assertFalse(info.isVarArgs());
        assertEquals(1, info.getStatements().size());
    }

    @Test
    void testMethodWithParameters() {
        String code = """
                class Test {
                    public int calculate(int a, String b) {
                        return a + b.length();
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);

        assertEquals("calculate", info.getName());
        assertEquals("int", info.getReturnType());
        assertEquals(2, info.getParameters().size());

        ParameterInformation param1 = info.getParameters().get(0);
        assertEquals("a", param1.getName());
        assertEquals("int", param1.getType());

        ParameterInformation param2 = info.getParameters().get(1);
        assertEquals("b", param2.getName());
        assertEquals("String", param2.getType());
    }

    @Test
    void testMethodWithVarArgs() {
        String code = """
                class Test {
                    public void printAll(String... messages) {
                        for(String msg : messages) {
                            System.out.println(msg);
                        }
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);

        assertTrue(info.isVarArgs());
        assertEquals(1, info.getParameters().size());

        ParameterInformation param = info.getParameters().get(0);
        assertEquals("messages", param.getName());
        assertEquals("String", param.getType());
        assertTrue(param.isVarArgs());
    }

    @Test
    void testMethodWithModifiersAndAnnotations() {
        String code = """
                class Test {
                    @Override
                    @Deprecated
                    public static final synchronized void complexMethod() {
                        // Empty method
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);

        List<String> modifiers = info.getModifiers();
        assertTrue(modifiers.contains("public"));
        assertTrue(modifiers.contains("static"));
        assertTrue(modifiers.contains("final"));
        assertTrue(modifiers.contains("synchronized"));

        List<String> annotations = info.getAnnotations();
        assertTrue(annotations.contains("@Override"));
        assertTrue(annotations.contains("@Deprecated"));
    }

    @Test
    void testMethodWithThrowsClause() {
        String code = """
                class Test {
                    public void riskyMethod() throws IOException, SQLException {
                        throw new IOException("Error");
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);

        List<String> exceptions = info.getThrowsExceptions();
        assertEquals(2, exceptions.size());
        assertTrue(exceptions.contains("IOException"));
        assertTrue(exceptions.contains("SQLException"));
    }

    @Test
    void testMethodWithComplexBody() {
        MethodInformation<JavaParser.StatementContext> info = parseAndVisitComplexMethod();
        List<Statement<JavaParser.StatementContext>> statements = info.getStatements();

        assertValidStatements(statements);
        assertStatementTypes(statements);
    }

    private MethodInformation<JavaParser.StatementContext> parseAndVisitComplexMethod() {
        String code = """
                public class Test {
                    public int complexMethod(int n) {
                        if (n <= 1) {
                            return 1;
                        }
                        int result = 1;
                        for (int i = 2; i <= n; i++) {
                            result *= i;
                        }
                        return result;
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        return visitor.visitMethodDeclaration(ctx);
    }

    private void assertValidStatements(List<Statement<JavaParser.StatementContext>> statements) {
        assertNotNull(statements, "Statement list should not be null");
        assertFalse(statements.isEmpty(), "Should have statements");

        statements.forEach(stmt -> {
            assertNotNull(stmt, "Each statement should exist");
            assertNotNull(stmt.getNode(), "Each statement should have a valid node");
        });
    }

    private void assertStatementTypes(List<Statement<JavaParser.StatementContext>> statements) {
        boolean hasIf = false;
        boolean hasFor = false;
        boolean hasReturn = false;

        for (Statement<JavaParser.StatementContext> stmt : statements) {
            JavaParser.StatementContext node = stmt.getNode();
            if (node.IF() != null) hasIf = true;
            if (node.FOR() != null) hasFor = true;
            if (node.RETURN() != null) hasReturn = true;
        }

        assertTrue(hasIf, "Should have an if statement");
        assertTrue(hasFor, "Should have a for statement");
        assertTrue(hasReturn, "Should have a return statement");
    }

    @Test
    void testAbstractMethod() {
        String code = """
                abstract class Test {
                    abstract void doSomething();
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);

        assertEquals("doSomething", info.getName());
        assertTrue(info.getModifiers().contains("abstract"));
        assertTrue(info.getStatements().isEmpty());
    }

    @Test
    void testMethodWithFinalParameters() {
        String code = """
                class Test {
                    void process(final int count, final String name) {
                        System.out.println(count + name);
                    }
                }
                """;

        JavaParser.MethodDeclarationContext ctx = parseMethod(code);
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);

        List<ParameterInformation> parameters = info.getParameters();
        assertEquals(2, parameters.size());

        for (ParameterInformation param : parameters) {
            assertTrue(param.getModifiers().contains("final"));
        }
    }
}
