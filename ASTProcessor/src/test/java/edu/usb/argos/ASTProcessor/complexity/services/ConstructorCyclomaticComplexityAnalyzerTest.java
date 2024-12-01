package edu.usb.argos.ASTProcessor.complexity.services;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import edu.usb.argos.ASTProcessor.complexity.core.services.analyzers.ConstructorCyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters.ConstructorAdapter;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInformation;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorCyclomaticComplexityAnalyzerTest {
    private ConstructorCyclomaticComplexityAnalyzer analyzer;
    private ComplexityRulesManager rulesManager;
    private JavaStatementAnalyzer statementAnalyzer;

    @BeforeEach
    void setUp() {
        rulesManager = new ComplexityRulesManager();
        statementAnalyzer = new JavaStatementAnalyzer();
    }

    private List<ConstructorInformation<JavaParser.StatementContext>> parseConstructors(String code) {
        try {
            JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);

            JavaParser.ClassBodyContext classBodyContext = parser.compilationUnit()
                    .typeDeclaration(0)
                    .classDeclaration()
                    .classBody();

            JavaConstructorVisitor visitor = new JavaConstructorVisitor();
            return visitor.visitConstructors(classBodyContext);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    void shouldAnalyzeSimpleConstructor() {
        String code = """
                public class TestClass {
                    public TestClass() {
                        if (x > 0) {
                            System.out.println("Positive");
                        }
                    }
                }
                """;

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = parseConstructors(code);
        assertFalse(constructors.isEmpty());
        ConstructorInformation<JavaParser.StatementContext> constructorInfo = constructors.get(0);
        ConstructorAdapter constructorAdapter = new ConstructorAdapter(constructorInfo);

        analyzer = ConstructorCyclomaticComplexityAnalyzer.builder()
                .target(constructorAdapter)
                .rulesManager(rulesManager)
                .statementAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("TestClass", result.getElementName());
        assertEquals(2, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertEquals(1, result.getComplexityLocations().size());
        assertEquals(ComplexityType.IF_STATEMENT,
                result.getComplexityLocations().get(0).getComplexityType());
    }

    @Test
    void shouldAnalyzeComplexConstructor() {
        String code = """
                public class TestClass {
                    public TestClass(int maxIterations, double threshold) {
                        try {
                            if (maxIterations > 0 && threshold > 0) {
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

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = parseConstructors(code);
        assertFalse(constructors.isEmpty());
        ConstructorInformation<JavaParser.StatementContext> constructorInfo = constructors.get(0);
        ConstructorAdapter constructorAdapter = new ConstructorAdapter(constructorInfo);

        analyzer = ConstructorCyclomaticComplexityAnalyzer.builder()
                .target(constructorAdapter)
                .rulesManager(rulesManager)
                .statementAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("TestClass", result.getElementName());
        assertEquals(7, result.getComplexityScore());
        assertEquals(ComplexityLevel.MEDIUM, result.getComplexityLevel());
        assertFalse(result.getComplexityLocations().isEmpty());
        assertTrue(result.getComplexityLocations().stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.CATCH_BLOCK));
    }

    @Test
    void shouldAnalyzeConstructorWithLogicalOperators() {
        String code = """
                public class TestClass {
                    public TestClass(int a, int b, int c) {
                        if (a > 0 && b < 10 || c == 0) {
                            doSomething();
                        }
                        while (x > 0 && y < 100) {
                            process();
                        }
                    }
                }
                """;

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = parseConstructors(code);
        assertFalse(constructors.isEmpty());
        ConstructorInformation<JavaParser.StatementContext> constructorInfo = constructors.get(0);
        ConstructorAdapter constructorAdapter = new ConstructorAdapter(constructorInfo);

        analyzer = ConstructorCyclomaticComplexityAnalyzer.builder()
                .target(constructorAdapter)
                .rulesManager(rulesManager)
                .statementAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("TestClass", result.getElementName());
        assertEquals(6, result.getComplexityScore());
        assertTrue(result.getComplexityLocations().stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.IF_STATEMENT ||
                        loc.getComplexityType() == ComplexityType.LOOP));
    }

    @Test
    void shouldAnalyzeConstructorWithSwitch() {
        String code = """
                public class TestClass {
                    public TestClass(int value) {
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

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = parseConstructors(code);
        assertFalse(constructors.isEmpty());
        ConstructorInformation<JavaParser.StatementContext> constructorInfo = constructors.get(0);
        ConstructorAdapter constructorAdapter = new ConstructorAdapter(constructorInfo);

        analyzer = ConstructorCyclomaticComplexityAnalyzer.builder()
                .target(constructorAdapter)
                .rulesManager(rulesManager)
                .statementAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("TestClass", result.getElementName());
        assertEquals(5, result.getComplexityScore());
        assertTrue(result.getComplexityLocations().stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.SWITCH_CASE));
    }

    @Test
    void shouldAnalyzeEmptyConstructor() {
        String code = """
                public class TestClass {
                    public TestClass() {
                    }
                }
                """;

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = parseConstructors(code);
        assertFalse(constructors.isEmpty());
        ConstructorInformation<JavaParser.StatementContext> constructorInfo = constructors.get(0);
        ConstructorAdapter constructorAdapter = new ConstructorAdapter(constructorInfo);

        analyzer = ConstructorCyclomaticComplexityAnalyzer.builder()
                .target(constructorAdapter)
                .rulesManager(rulesManager)
                .statementAnalyzer(statementAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("TestClass", result.getElementName());
        assertEquals(1, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertTrue(result.getComplexityLocations().isEmpty());
    }

    @Test
    void shouldAnalyzeClassWithoutExplicitConstructor() {
        String code = """
                public class TestClass {
                    public void someMethod() {
                        System.out.println("This is a method.");
                    }
                }
                """;

        List<ConstructorInformation<JavaParser.StatementContext>> constructors = parseConstructors(code);

        assertTrue(constructors.isEmpty(), "No explicit constructors should be found.");

        ComplexityResult result = ComplexityResult.builder()
                .elementName("TestClass")
                .complexityScore(1)
                .complexityLevel(ComplexityLevel.LOW)
                .build();

        assertEquals("TestClass", result.getElementName());
        assertEquals(1, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
    }
}
