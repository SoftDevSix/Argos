package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.INoDuplicateCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.AstWalkerAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.CodeSmellsBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.classAnalyzers.LshNoDuplicatedCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.reader.application.services.FileReaderByText;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileAnalyzer;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.classes.IClassMemberService;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassIdentityService;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassMemberService;
import edu.usb.argos.astprocessor.visitor.core.services.classes.JavaClassStructureService;
import edu.usb.argos.astprocessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.astprocessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.astprocessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaClassVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AstWalkerAnalyzerTest {

    private CodeAnalysisReportHandlerByClass reportHandlerByClass;
    private static IFileAnalyzer<String, ParserRuleContext> reader;
    private static JavaClassVisitor classVisitor;
    private static List<String> code;
    private AstWalkerAnalyzer astWalker;

    @BeforeAll
    public static void setupAll() {
        reader = new FileReaderByText();
        JavaMethodVisitor methodVisitor = new JavaMethodVisitor(
                new ModifierService(),
                new ParameterService(),
                new AnnotationService()
        );
        AttributeHandler attributeHandler = new AttributeHandler();
        JavaAttributeVisitor attributeVisitor = new JavaAttributeVisitor(attributeHandler);
        JavaConstructorVisitor constructorVisitor = new JavaConstructorVisitor();

        JavaClassIdentityService identityService = new JavaClassIdentityService();
        JavaClassStructureService structureService = new JavaClassStructureService();
        IClassMemberService<ParserRuleContext, JavaParser.StatementContext> memberService = new JavaClassMemberService(methodVisitor, attributeVisitor, constructorVisitor);

        classVisitor = new JavaClassVisitor(identityService, structureService, memberService);

        code = List.of(
                """
                        public class SimpleMathOperations {
                            public int sum(int a, int b) {
                                if (a < 0 || b < 0) {
                                    throw new IllegalArgumentException("Values must be non-negative");
                                }
                                int result = a + b;
                                System.out.println("Sum result: " + result);
                                return result;
                            }
                        }
                        """,
                """
                        public class MathOperations {
                            public int sumMultiplication(int a, int b) {
                                int sumResult = a + b;
                                int result = sumResult * 3;
                                System.out.println("SumMultiplication result: " + result);
                                return result;
                            }
                        
                            public int sumDivision(int a, int b) {
                                int sumResult = a + b;
                                int result = (int) Math.ceil(sumResult / 2.0);
                                System.out.println("SumDivision result: " + result);
                                return result;
                            }
                        }
                        """
        );
    }

    @BeforeEach
    public void setupEachTest() {
        reportHandlerByClass = new CodeAnalysisReportHandlerByClass();
    }

    private ClassInformation<JavaParser.StatementContext> parseClassFromPlainText(String code) {
        Optional<ParserRuleContext> classContext = reader.readFile(code);
        assertTrue(classContext.isPresent());

        return classVisitor.visitClass(classContext.get());
    }

    private List<ClassInformation<JavaParser.StatementContext>> parseClassesFromPlainTest(List<String> code) {
        return code.stream()
                .map(this::parseClassFromPlainText)
                .toList();
    }

    private IMethodTooLongAnalyzerBuilder<JavaParser.StatementContext> buildMethodTooAnalyzerLongFromRules() {
        return new AntlrMethodTooLongAnalyzerBuilder();
    }

    private IExcessiveParametersAnalyzerBuilder<JavaParser.StatementContext> buildAntlrExcessiveParametersAnalyzerBuilder() {
        return new AntlrExcessiveParametersAnalyzerBuilder();
    }

    private INoDuplicateCodeAnalyzerBuilder<JavaParser.StatementContext> buildDuplicatedAnalyzer() {
        LshConfiguration configuration = LshConfiguration.builder()
                .shinglesFrequency(3)
                .minHashConfiguration(MinHashConfiguration
                        .builder()
                        .seed(7)
                        .numberOfHashFunctions(15)
                        .prime(16777619)
                        .build())
                .numberOfBands(5)
                .similarityThreshold(0.4)
                .build();

        return new LshNoDuplicatedCodeAnalyzerBuilder(configuration);
    }

    private CodeSmellsBuilder<JavaParser.StatementContext> buildCodeSmellBuilderFromRules(CodeSmellsRules codeSmellsRules) {
        return CodeSmellsBuilder.<JavaParser.StatementContext>builder()
                .codeSmellsRules(codeSmellsRules)
                .noDuplicatedCodeAnalyzerBuilder(buildDuplicatedAnalyzer())
                .methodTooLongAnalyzerBuilder(buildMethodTooAnalyzerLongFromRules())
                .parametersAnalyzerBuilder(buildAntlrExcessiveParametersAnalyzerBuilder())
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    @Test
    public void testAnalyzeForAstWalkerWithOnlyExcessiveParametersAnalyzer() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .excessiveParameters(true)
                .noDuplicatedCode(false)
                .methodTooLong(false)
                .maxParameters(1)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = buildCodeSmellBuilderFromRules(codeSmellsRules);
        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);
        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();

        astWalker = new AstWalkerAnalyzer(analyzerMap);
        astWalker.walkAnalyzers(classes);

        int expectedTotalOfReports = 3;
        assertEquals(expectedTotalOfReports, astWalker.getReports().stream().mapToInt(r -> r.getCodeAnalysis().size()).sum());
    }

    @Test
    public void testAnalyzeForAstWalkerWithOnlyMethodTooLongAnalyzer() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .excessiveParameters(false)
                .noDuplicatedCode(false)
                .methodTooLong(true)
                .maxMethodLength(3)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = buildCodeSmellBuilderFromRules(codeSmellsRules);
        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);
        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();

        astWalker = new AstWalkerAnalyzer(analyzerMap);
        astWalker.walkAnalyzers(classes);

        int expectedTotalOfReports = 3;
        assertEquals(expectedTotalOfReports, astWalker.getReports().stream().mapToInt(r -> r.getCodeAnalysis().size()).sum());
    }

    @Test
    public void testAnalyzeForAstWalkerWithOnlyNoDuplicatedCodeAnalyzer() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .excessiveParameters(false)
                .noDuplicatedCode(true)
                .methodTooLong(false)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = buildCodeSmellBuilderFromRules(codeSmellsRules);
        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);
        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();

        astWalker = new AstWalkerAnalyzer(analyzerMap);
        astWalker.walkAnalyzers(classes);

        int expectedTotalOfReports = 6;
        assertEquals(expectedTotalOfReports, astWalker.getReports().stream().mapToInt(r -> r.getCodeAnalysis().size()).sum());
    }

    @Test
    public void testAnalyzeForAstWalkerWithAllAnalyzers() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .excessiveParameters(true)
                .methodTooLong(true)
                .noDuplicatedCode(true)
                .maxMethodLength(3)
                .maxParameters(1)
                .build();


        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = buildCodeSmellBuilderFromRules(codeSmellsRules);
        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);
        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();

        astWalker = new AstWalkerAnalyzer(analyzerMap);
        astWalker.walkAnalyzers(classes);

        int expectedTotalOfReports = 12;
        assertEquals(expectedTotalOfReports, astWalker.getReports().stream().mapToInt(r -> r.getCodeAnalysis().size()).sum());
    }
}
