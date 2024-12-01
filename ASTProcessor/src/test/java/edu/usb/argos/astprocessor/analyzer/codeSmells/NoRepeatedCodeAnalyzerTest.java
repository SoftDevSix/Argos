package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ISimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.core.services.NoRepeatedCodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers.AntlrMethodNormalizer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.JaccardSimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.LshSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.MinHashingHandler;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.TokenShingleGenerator;
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
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoRepeatedCodeAnalyzerTest {

    private static IFileAnalyzer<String, ParserRuleContext> reader;
    private static JavaClassVisitor classVisitor;

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

    private NoRepeatedCodeAnalyzer buildAnalyzerFromRules(LshConfiguration lshConfiguration) {
        IPlainTextHasher textHasher = new SHATextHasher();
        MinHashingHandler codeMinHash = new MinHashingHandler(lshConfiguration.getMinHashConfiguration(), textHasher);
        IShingleGenerator<String> shingleGenerator = new TokenShingleGenerator(lshConfiguration.getShinglesFrequency());
        INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer = new AntlrMethodNormalizer();
        ISimilarityCalculator<List<Integer>> similarityCalculator = new JaccardSimilarityCalculator(lshConfiguration.getMinHashConfiguration());
        LshSelector<CodeIdentity> candidateSelector = new LshSelector<>(lshConfiguration, similarityCalculator);
        CodeAnalysisReportHandlerByClass reportHandlerByClass = new CodeAnalysisReportHandlerByClass();

        return new NoRepeatedCodeAnalyzer(codeMinHash, shingleGenerator, methodNormalizer, candidateSelector, reportHandlerByClass);
    }

    @Test
    public void testRepeatedCodeInAClass() {
        String code = """
                public class RepeatedExample {
                
                    public int sum(int a, int b) {
                        int result = a + b;
                
                        return result;
                    }
                
                    public int multiplication(int a, int b) {
                        int result = a * b;
                
                        return result;
                    }
                
                    public int sumMultiplication(int a, int b) {
                        int result = a + b;
                        result = result * 2;
                
                        return result;
                    }
                
                    public int sumDivision(int a, int b) {
                        int result = a + b;
                        result = result / 2;
                
                        return result;
                    }
                }
                """;

        ClassInformation<JavaParser.StatementContext> classInformation = parseClassFromPlainText(code);
        MinHashConfiguration minHashConfiguration = MinHashConfiguration
                .builder()
                .seed(7)
                .numberOfHashFunctions(4)
                .prime(16777619)
                .build();
        LshConfiguration lshConfig = LshConfiguration.builder()
                .shinglesFrequency(3)
                .minHashConfiguration(minHashConfiguration)
                .numberOfBands(2)
                .similarityThreshold(0.5)
                .build();

        NoRepeatedCodeAnalyzer repeatedCodeAnalyzer = buildAnalyzerFromRules(lshConfig);
        CodeSmellAnalysisByClass codeSmellAnalysisByClass = new CodeSmellAnalysisByClass("JavaPathTest.java");

        repeatedCodeAnalyzer.getReportHandlerByClass().setCodeSmellAnalysisByClass(codeSmellAnalysisByClass);
        repeatedCodeAnalyzer.analyze(classInformation);

        assertEquals(12, codeSmellAnalysisByClass.getCodeAnalysis().size());
    }

    @Test
    public void testRepeatedCodeInMultipleClasses() {
        List<String> code = List.of(
                """
                        public class RepeatedExampleOne {
                            public int sumMultiplication(int a, int b) {
                                int result = a + b;
                                result = result * 2;
                        
                                return result;
                            }
                        
                            public int sumDivision(int a, int b) {
                                int result = a + b;
                                result = result / 2;
                        
                                return result;
                            }
                        }
                        """,
                """
                        public class RepeatedExampleTwo {
                            public int sum(int a, int b) {
                                int result = a + b;
                        
                                return result;
                            }
                        
                            public int multiplication(int a, int b) {
                                int result = a * b;
                        
                                return result;
                            }
                        }
                        """
        );

        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);

        LshConfiguration lshConfig = LshConfiguration.builder()
                .shinglesFrequency(3)
                .minHashConfiguration(MinHashConfiguration
                        .builder()
                        .seed(7)
                        .numberOfHashFunctions(4)
                        .prime(16777619)
                        .build())
                .numberOfBands(2)
                .similarityThreshold(0.5)
                .build();

        NoRepeatedCodeAnalyzer repeatedCodeAnalyzer = buildAnalyzerFromRules(lshConfig);

        List<CodeSmellAnalysisByClass> reports = new ArrayList<>();

        for (ClassInformation<JavaParser.StatementContext> classInfo : classes) {
            CodeSmellAnalysisByClass classReport = new CodeSmellAnalysisByClass("ClassPath.java");
            repeatedCodeAnalyzer.getReportHandlerByClass().setCodeSmellAnalysisByClass(classReport);
            repeatedCodeAnalyzer.analyze(classInfo);
            reports.add(classReport);
        }

        int totalNumberOfReports = reports.stream()
                .mapToInt(rep -> rep.getCodeAnalysis().size())
                .sum();
        assertEquals(12, totalNumberOfReports);
    }
}
