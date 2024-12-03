package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.services.AstWalkerAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.CodeSmellsBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.classAnalyzers.LshNoDuplicatedCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.MethodLineAnalyzer;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class AstWalkerAnalyzerTest {
    private CodeAnalysisReportHandlerByClass reportHandlerByClass;
    private static IFileAnalyzer<String, ParserRuleContext> reader;
    private static JavaClassVisitor classVisitor;
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

    private AntlrMethodTooLongAnalyzerBuilder buildMethodTooAnalyzerLongFromRules(CodeSmellsRules codeSmellsRules) {
        IMethodLineAnalyzer<JavaParser.StatementContext> methodLineAnalyzer = new MethodLineAnalyzer();

        return AntlrMethodTooLongAnalyzerBuilder.builder()
                .maxMethodLength(codeSmellsRules.getMaxMethodLength())
                .methodLineAnalyzer(methodLineAnalyzer)
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    private AntlrExcessiveParametersAnalyzerBuilder buildAntlrExcessiveParametersAnalyzerBuilder(CodeSmellsRules codeSmellsRules) {
        return AntlrExcessiveParametersAnalyzerBuilder.builder()
                .maxParameters(codeSmellsRules.getMaxParameters())
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    private LshNoDuplicatedCodeAnalyzerBuilder buildDuplicatedAnalyzer() {
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

        return LshNoDuplicatedCodeAnalyzerBuilder.builder()
                .lshConfiguration(configuration)
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    @Test
    public void testAnalyzeForAstWalker() {
        List<String> code = List.of(
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

        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .id(12)
                .excessiveParameters(true)
                .magicNumbers(false)
                .methodTooLong(true)
                .noDuplicatedCode(false)
                .maxMethodLength(3)
                .maxParameters(2)
                .build();

        CodeSmellsBuilder codeSmellsBuilder = CodeSmellsBuilder.builder()
                .codeSmellsRules(codeSmellsRules)
                .noDuplicatedCodeAnalyzerBuilder(buildDuplicatedAnalyzer())
                .methodTooLongAnalyzerBuilder(buildMethodTooAnalyzerLongFromRules(codeSmellsRules))
                .parametersAnalyzerBuilder(buildAntlrExcessiveParametersAnalyzerBuilder(codeSmellsRules))
                .build();

        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);
        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();

        astWalker = new AstWalkerAnalyzer(analyzerMap);
        astWalker.walkAnalyzers(classes);
    }
}
