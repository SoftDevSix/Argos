package edu.usb.argos.ASTProcessor.complexity.infraestructure;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavaStatementAnalyzerTest {
    private JavaStatementAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new JavaStatementAnalyzer();
    }

    private JavaParser.StatementContext parseStatement(String code) {
        try {
            String wrappedCode = String.format("""
                    class Test {
                        void testMethod() {
                            %s
                        }
                    }
                    """, code);

            JavaLexer lexer = new JavaLexer(CharStreams.fromString(wrappedCode));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);

            return parser.compilationUnit()
                    .typeDeclaration(0)
                    .classDeclaration()
                    .classBody()
                    .classBodyDeclaration(0)
                    .memberDeclaration()
                    .methodDeclaration()
                    .methodBody()
                    .block()
                    .blockStatement(0)
                    .statement();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    void shouldAnalyzeIfStatement() {
        String code = """
                if (x > 0) {
                    System.out.println("Positive");
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(1, analyzer.analyzeNode(statement));

        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);
        assertFalse(locations.isEmpty());
        assertEquals(ComplexityType.IF_STATEMENT, locations.get(0).getComplexityType());
    }

    @Test
    void shouldAnalyzeIfElseIfStatement() {
        String code = """
                if (x > 0) {
                    System.out.println("Positive");
                } else if (x < 0) {
                    System.out.println("Negative");
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(2, analyzer.analyzeNode(statement));
    }

    @Test
    void shouldAnalyzeForLoop() {
        String code = """
                for (int i = 0; i < 10; i++) {
                    System.out.println(i);
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(1, analyzer.analyzeNode(statement));

        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);
        assertFalse(locations.isEmpty());
        assertEquals(ComplexityType.LOOP, locations.get(0).getComplexityType());
    }

    @Test
    void shouldAnalyzeWhileLoop() {
        String code = """
                while (condition) {
                    System.out.println("Loop");
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(1, analyzer.analyzeNode(statement));
    }

    @Test
    void shouldAnalyzeDoWhileLoop() {
        String code = """
                do {
                    System.out.println("Loop");
                } while (condition);
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(1, analyzer.analyzeNode(statement));
    }

    @Test
    void shouldAnalyzeTryCatch() {
        String code = """
                try {
                    riskyOperation();
                } catch (IOException e) {
                    handleError();
                } catch (SQLException e) {
                    handleDBError();
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(2, analyzer.analyzeNode(statement));

        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);
        assertEquals(2, locations.size());
        assertTrue(locations.stream()
                .allMatch(loc -> loc.getComplexityType() == ComplexityType.CATCH_BLOCK));
    }

    @Test
    void shouldAnalyzeSwitch() {
        String code = """
                switch (value) {
                    case 1:
                        handleOne();
                        break;
                    case 2:
                        handleTwo();
                        break;
                    default:
                        handleDefault();
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(2, analyzer.analyzeNode(statement));
    }

    @Test
    void shouldAnalyzeComplexCondition() {
        String code = """
                if (x > 0 && y < 10 || z == 0) {
                    System.out.println("Complex condition");
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(3, analyzer.analyzeNode(statement));
    }

    @Test
    void shouldAnalyzeNestedStructures() {
        String code = """
                if (x > 0) {
                    for (int i = 0; i < x; i++) {
                        try {
                            if (i % 2 == 0) {
                                doSomething();
                            }
                        } catch (Exception e) {
                            handleError();
                        }
                    }
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        assertEquals(4, analyzer.analyzeNode(statement));
    }

    @Test
    void shouldGetCorrectComplexityLocation() {
        String code = """
                if (x > 0) {
                    System.out.println("Positive");
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);
        assertFalse(locations.isEmpty());

        ComplexityLocation location = locations.get(0);

        assertEquals(ComplexityType.IF_STATEMENT, location.getComplexityType());
        assertEquals("Conditional branch", location.getDescription());
        assertEquals("if condition: (x>0)", location.getContextInfo());
        assertTrue(location.getLineNumber() > 0);
    }


    @Test
    void shouldGetCorrectComplexityLocationForLoop() {
        String code = """
                for (int i = 0; i < 10; i++) {
                    System.out.println(i);
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);
        assertFalse(locations.isEmpty());

        ComplexityLocation location = locations.get(0);
        assertEquals(ComplexityType.LOOP, location.getComplexityType());
        assertEquals("For loop", location.getDescription());
        assertTrue(location.getContextInfo().contains("for loop with control"));
    }

    @Test
    void shouldGetCorrectComplexityLocationForComplexCondition() {
        String code = """
                if (x > 0 && y < 10) {
                    System.out.println("Complex");
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);

        assertEquals(2, locations.size());
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.IF_STATEMENT));
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.LOGICAL_AND));
        assertTrue(locations.stream()
                .allMatch(loc -> loc.getLineNumber() > 0));
    }

    @Test
    void shouldAnalyzeSuperComplexMethod() {
        String code = """
                try {
                    if (condition1 && condition2) {
                        for (int i = 0; i < maxIterations; i++) {
                            if (data[i] > threshold) {
                                while (processFlag) {
                                    switch (state) {
                                        case 1:
                                            if (subCondition1 || subCondition2) {
                                                doSomething();
                                            }
                                            break;
                                        case 2:
                                            try {
                                                riskyOperation();
                                            } catch (SpecificException e) {
                                                handleError();
                                            }
                                            break;
                                        default:
                                            doDefault();
                                    }
                                    
                                    if (exitCondition) {
                                        break;
                                    }
                                }
                            } else if (data[i] < -threshold) {
                                do {
                                    processNegative();
                                } while (needsMoreProcessing && isValid);
                            }
                        }
                    }
                } catch (MainException e) {
                    handleMainError();
                } catch (FallbackException e) {
                    handleFallback();
                }
                """;

        JavaParser.StatementContext statement = parseStatement(code);
        int complexity = analyzer.analyzeNode(statement);

        assertEquals(14, complexity);

        List<ComplexityLocation> locations = analyzer.getComplexityLocation(statement);
        assertFalse(locations.isEmpty());
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.CATCH_BLOCK));
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.IF_STATEMENT));
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.LOOP));
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.LOGICAL_AND));
        assertTrue(locations.stream()
                .anyMatch(loc -> loc.getComplexityType() == ComplexityType.LOGICAL_OR));
    }
}
