package edu.usb.argos.ASTProcessor.complexity.services;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import edu.usb.argos.ASTProcessor.complexity.core.services.analyzers.MethodCyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters.MethodAdapter;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MethodCyclomaticComplexityAnalyzerTest {
    private MethodCyclomaticComplexityAnalyzer analyzer;
    private ComplexityRulesManager rulesManager;
    private JavaStatementAnalyzer statementAnalyzer;

    @BeforeEach
    void setUp() {
        rulesManager = new ComplexityRulesManager();
        statementAnalyzer = new JavaStatementAnalyzer();
    }

    private MethodInformation<JavaParser.StatementContext> parseMethod(String code) {
        try {
            JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);

            JavaParser.MethodDeclarationContext methodContext = parser.compilationUnit()
                    .typeDeclaration(0)
                    .classDeclaration()
                    .classBody()
                    .classBodyDeclaration(0)
                    .memberDeclaration()
                    .methodDeclaration();

            JavaMethodVisitor visitor = new JavaMethodVisitor(
                    new ModifierService(),
                    new ParameterService(),
                    new AnnotationService()
            );

            return visitor.visitMethodDeclaration(methodContext);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    void shouldAnalyzeSimpleMethod() {
        String code = """
                public class TestClass {
                    public void simpleMethod() {
                        if (x > 0) {
                            System.out.println("Positive");
                        }
                    }
                }
                """;

        MethodInformation<JavaParser.StatementContext> methodInfo = parseMethod(code);
        MethodAdapter methodAdapter = new MethodAdapter(methodInfo);

        analyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(methodAdapter)
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("simpleMethod", result.getElementName());
        assertEquals(2, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertEquals(1, result.getComplexityLocations().size());
        assertEquals(ComplexityType.IF_STATEMENT,
                result.getComplexityLocations().get(0).getComplexityType());
    }

    @Test
    void shouldAnalyzeComplexMethod() {
        String code = """
                public class TestClass {
                    public void complexMethod() {
                        try {
                            if (condition1 && condition2) {
                                for (int i = 0; i < maxIterations; i++) {
                                    while (running) {
                                        if (data[i] > threshold) {
                                            processData();
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            handleError();
                        }
                    }
                }
                """;

        MethodInformation<JavaParser.StatementContext> methodInfo = parseMethod(code);
        MethodAdapter methodAdapter = new MethodAdapter(methodInfo);

        analyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(methodAdapter)
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("complexMethod", result.getElementName());
        assertEquals(7, result.getComplexityScore());
        assertEquals(ComplexityLevel.MEDIUM, result.getComplexityLevel());
        assertFalse(result.getComplexityLocations().isEmpty());
        assertTrue(result.getComplexityLocations().stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.CATCH_BLOCK));
    }

    @Test
    void shouldAnalyzeMethodWithLogicalOperators() {
        String code = """
                public class TestClass {
                    public void logicalMethod() {
                        if (a > 0 && b < 10 || c == 0) {
                            doSomething();
                        }
                        while (x > 0 && y < 100) {
                            process();
                        }
                    }
                }
                """;

        MethodInformation<JavaParser.StatementContext> methodInfo = parseMethod(code);
        MethodAdapter methodAdapter = new MethodAdapter(methodInfo);

        analyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(methodAdapter)
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("logicalMethod", result.getElementName());
        assertEquals(6, result.getComplexityScore());
        assertTrue(result.getComplexityLocations().stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.IF_STATEMENT ||
                        loc.getComplexityType() == ComplexityType.LOOP));
    }

    @Test
    void shouldAnalyzeMethodWithSwitch() {
        String code = """
                public class TestClass {
                    public void switchMethod() {
                        switch (value) {
                            case 1:
                                doOne();
                                break;
                            case 2:
                                if (condition) {
                                    doTwo();
                                }
                                break;
                            case 3:
                                doThree();
                                break;
                            default:
                                doDefault();
                        }
                    }
                }
                """;

        MethodInformation<JavaParser.StatementContext> methodInfo = parseMethod(code);
        MethodAdapter methodAdapter = new MethodAdapter(methodInfo);

        analyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(methodAdapter)
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("switchMethod", result.getElementName());
        assertEquals(5, result.getComplexityScore());
        assertTrue(result.getComplexityLocations().stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.SWITCH_CASE));
    }

    @Test
    void shouldAnalyzeEmptyMethod() {
        String code = """
                public class TestClass {
                    public void emptyMethod() {
                    }
                }
                """;

        MethodInformation<JavaParser.StatementContext> methodInfo = parseMethod(code);
        MethodAdapter methodAdapter = new MethodAdapter(methodInfo);

        analyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(methodAdapter)
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("emptyMethod", result.getElementName());
        assertEquals(1, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertTrue(result.getComplexityLocations().isEmpty());
    }
}
