package edu.usb.argos.ASTProcessor.complexity.services;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ClassComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.services.analyzers.ClassCyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters.ClassAdapter;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.classes.JavaClassIdentityService;
import edu.usb.argos.ASTProcessor.visitor.core.services.classes.JavaClassMemberService;
import edu.usb.argos.ASTProcessor.visitor.core.services.classes.JavaClassStructureService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaClassVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClassCyclomaticComplexityAnalyzerTest {
    private ClassCyclomaticComplexityAnalyzer analyzer;
    private ComplexityRulesManager rulesManager;
    private JavaClassVisitor classVisitor;

    @BeforeEach
    void setUp() {
        rulesManager = new ComplexityRulesManager();
        classVisitor = createClassVisitor();
    }

    private JavaClassVisitor createClassVisitor() {
        JavaClassIdentityService identityService = new JavaClassIdentityService();
        JavaClassStructureService structureService = new JavaClassStructureService();
        ModifierService modifierService = new ModifierService();
        ParameterService parameterService = new ParameterService();
        AnnotationService annotationService = new AnnotationService();
        JavaMethodVisitor methodVisitor = new JavaMethodVisitor(
                modifierService,
                parameterService,
                annotationService);

        AttributeHandler attributeHandler = new AttributeHandler();
        JavaAttributeVisitor attributeVisitor = new JavaAttributeVisitor(attributeHandler);

        JavaConstructorVisitor constructorVisitor = new JavaConstructorVisitor();

        JavaClassMemberService memberService = new JavaClassMemberService(
                methodVisitor,
                attributeVisitor,
                constructorVisitor);

        return new JavaClassVisitor(
                identityService,
                structureService,
                memberService);
    }

    private ClassInformation<JavaParser.StatementContext> parseClass(String code) {
        try {
            JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);

            JavaParser.ClassDeclarationContext classDeclarationContext = parser.compilationUnit()
                    .typeDeclaration(0)
                    .classDeclaration();

            return classVisitor.visitClass(classDeclarationContext);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    void shouldAnalyzeSimpleClass() {
        String code = """
                public class SimpleClass {
                    private int value;

                    public void simpleMethod() {
                        if (value > 0) {
                            System.out.println("Positive");
                        }
                    }
                }
                """;

        ClassInformation<JavaParser.StatementContext> classInfo = parseClass(code);
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .build();

        ClassComplexityResult result = analyzer.analyze();

        assertEquals("SimpleClass", result.getClassName());
        assertEquals(2, result.getTotalComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getOverallComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertEquals(1, result.getMethodResults().size());

        var methodResult = result.getMethodResults().get(0);
        assertEquals("simpleMethod", methodResult.getMethodName());
        assertEquals(2, methodResult.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, methodResult.getComplexityLevel());
        assertTrue(methodResult.isWithinLimits());
    }

    @Test
    void shouldAnalyzeComplexClass() {
        String code = """
                public class ComplexClass {
                    private int[] data;

                    public void complexMethod(int maxIterations, double threshold) {
                        try {
                            if (maxIterations > 0 && threshold > 0) {
                                for (int i = 0; i < maxIterations; i++) {
                                    while (i < data.length) {
                                        if (data[i] > threshold) {
                                            processData(data[i]);
                                        }
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            handleError(e);
                        }
                    }

                    private void processData(int value) {}
                    private void handleError(Exception e) {}
                }
                """;

        ClassInformation<JavaParser.StatementContext> classInfo = parseClass(code);
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .build();

        ClassComplexityResult result = analyzer.analyze();

        assertEquals("ComplexClass", result.getClassName());
        assertEquals(7, result.getTotalComplexityScore());
        assertEquals(ComplexityLevel.MEDIUM, result.getOverallComplexityLevel());
        assertFalse(result.isWithinLimits());

        var methodResult = result.getMethodResults().get(0);
        assertEquals("complexMethod", methodResult.getMethodName());
        assertEquals(7, methodResult.getComplexityScore());
        assertEquals(ComplexityLevel.MEDIUM, methodResult.getComplexityLevel());
        assertFalse(methodResult.isWithinLimits());
        assertNotNull(methodResult.getComplexityLocations());
    }

    @Test
    void shouldAnalyzeClassWithLogicalOperators() {
        String code = """
                public class LogicalClass {
                    private int x, y, z;

                    public void logicalMethod() {
                        if (x > 0 && y < 10 || z == 0) {
                            doSomething();
                        }
                        while (x > 0 && y < 100) {
                            process();
                        }
                    }

                    private void doSomething() {}
                    private void process() {}
                }
                """;

        ClassInformation<JavaParser.StatementContext> classInfo = parseClass(code);
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .build();

        ClassComplexityResult result = analyzer.analyze();

        assertEquals("LogicalClass", result.getClassName());
        assertEquals(6, result.getTotalComplexityScore());

        var methodResult = result.getMethodResults().get(0);
        assertEquals("logicalMethod", methodResult.getMethodName());
        assertEquals(6, methodResult.getComplexityScore());
        assertFalse(methodResult.isWithinLimits());
    }

    @Test
    void shouldAnalyzeClassWithSwitch() {
        String code = """
                public class SwitchClass {
                    public void switchMethod(int value) {
                        switch (value) {
                            case 1:
                                doOne();
                                break;
                            case 2:
                                if (value > 0) {
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

                    private void doOne() {}
                    private void doTwo() {}
                    private void doThree() {}
                    private void doDefault() {}
                }
                """;

        ClassInformation<JavaParser.StatementContext> classInfo = parseClass(code);
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .build();

        ClassComplexityResult result = analyzer.analyze();

        assertEquals("SwitchClass", result.getClassName());
        assertEquals(5, result.getTotalComplexityScore());

        var methodResult = result.getMethodResults().get(0);
        assertEquals("switchMethod", methodResult.getMethodName());
        assertEquals(5, methodResult.getComplexityScore());
    }

    @Test
    void shouldAnalyzeEmptyClass() {
        String code = """
                public class EmptyClass {
                }
                """;

        ClassInformation<JavaParser.StatementContext> classInfo = parseClass(code);
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .build();

        ClassComplexityResult result = analyzer.analyze();

        assertEquals("EmptyClass", result.getClassName());
        assertEquals(1, result.getTotalComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getOverallComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertTrue(result.getMethodResults().isEmpty());
    }
}