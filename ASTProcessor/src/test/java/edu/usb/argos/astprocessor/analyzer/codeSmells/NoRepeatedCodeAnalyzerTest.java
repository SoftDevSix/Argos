package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IEntitySignatureBuilder;
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
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.entitySingnatureBuilders.MethodSignatureBuilder;
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
        LshSelector<CodeIdentity<CodeSmellAnalysisByClass>> candidateSelector = new LshSelector<>(lshConfiguration, similarityCalculator);
        CodeAnalysisReportHandlerByClass reportHandlerByClass = new CodeAnalysisReportHandlerByClass();

        IEntitySignatureBuilder<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>, ClassInformation<JavaParser.StatementContext>> entitySignatureBuilder = new MethodSignatureBuilder(codeMinHash, shingleGenerator, methodNormalizer);

        return new NoRepeatedCodeAnalyzer(candidateSelector, reportHandlerByClass, entitySignatureBuilder);
    }

    private static int getNumberOfReportsFound(CodeSmellAnalysisByClass codeSmellAnalysisByClass, List<String> expectedRangeAndMessageReports) {
        int numberOfReportsFound = 0;
        for (var report : codeSmellAnalysisByClass.getCodeAnalysis()) {
            String rangeAndMessageReport = "CodeRange: " + report.getStartLine() + " - " + report.getEndLine() + ", Message: " + report.getMessage();

            if (expectedRangeAndMessageReports.contains(rangeAndMessageReport)) {
                numberOfReportsFound++;
            }
        }
        return numberOfReportsFound;
    }

    @Test
    public void testRepeatedCodeInAClass() {
        String code = """
                public class MathOperations {
                    public int sum(int a, int b) {
                        if (a < 0 || b < 0) {
                            throw new IllegalArgumentException("Values must be non-negative");
                        }
                        int result = a + b;
                        System.out.println("Sum result: " + result);
                        return result;
                    }
                
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
                """;

        ClassInformation<JavaParser.StatementContext> classInformation = parseClassFromPlainText(code);
        LshConfiguration lshConfig = LshConfiguration.builder()
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

        NoRepeatedCodeAnalyzer repeatedCodeAnalyzer = buildAnalyzerFromRules(lshConfig);
        CodeSmellAnalysisByClass codeSmellAnalysisByClass = new CodeSmellAnalysisByClass("JavaPathTest.java");

        repeatedCodeAnalyzer.getReportHandlerByClass().setCodeSmellAnalysisByClass(codeSmellAnalysisByClass);
        repeatedCodeAnalyzer.analyze(classInformation);
        assertEquals(6, codeSmellAnalysisByClass.getCodeAnalysis().size());

        List<String> expectedRangeAndMessageReports = List.of(
                "CodeRange: 2 - 9, Message: Similar code was detected in file MathOperations.java/sumDivision, consider abstracting it or reusing functions.",
                "CodeRange: 18 - 23, Message: Similar code was detected in file MathOperations.java/sum, consider abstracting it or reusing functions.",
                "CodeRange: 11 - 16, Message: Similar code was detected in file MathOperations.java/sumDivision, consider abstracting it or reusing functions.",
                "CodeRange: 18 - 23, Message: Similar code was detected in file MathOperations.java/sumMultiplication, consider abstracting it or reusing functions.",
                "CodeRange: 2 - 9, Message: Similar code was detected in file MathOperations.java/sumMultiplication, consider abstracting it or reusing functions.",
                "CodeRange: 11 - 16, Message: Similar code was detected in file MathOperations.java/sum, consider abstracting it or reusing functions."
        );

        int numberOfReportsFound = getNumberOfReportsFound(codeSmellAnalysisByClass, expectedRangeAndMessageReports);
        assertEquals(numberOfReportsFound, expectedRangeAndMessageReports.size());
    }

    @Test
    public void testRepeatedCodeInMultipleClasses() {

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

        List<ClassInformation<JavaParser.StatementContext>> classes = parseClassesFromPlainTest(code);

        LshConfiguration lshConfig = LshConfiguration.builder()
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
        assertEquals(6, totalNumberOfReports);

        List<String> expectedRangeAndMessageReports = List.of(
                "CodeRange: 2 - 9, Message: Similar code was detected in file MathOperations.java/sumDivision, consider abstracting it or reusing functions.",
                "CodeRange: 9 - 14, Message: Similar code was detected in file SimpleMathOperations.java/sum, consider abstracting it or reusing functions.",
                "CodeRange: 2 - 9, Message: Similar code was detected in file MathOperations.java/sumMultiplication, consider abstracting it or reusing functions.",
                "CodeRange: 2 - 7, Message: Similar code was detected in file SimpleMathOperations.java/sum, consider abstracting it or reusing functions.",
                "CodeRange: 2 - 7, Message: Similar code was detected in file MathOperations.java/sumDivision, consider abstracting it or reusing functions.",
                "CodeRange: 9 - 14, Message: Similar code was detected in file MathOperations.java/sumMultiplication, consider abstracting it or reusing functions."
        );

        int numberOfReportsFound = 0;
        for (CodeSmellAnalysisByClass report : reports) {
            numberOfReportsFound += getNumberOfReportsFound(report, expectedRangeAndMessageReports);
        }

        assertEquals(numberOfReportsFound, expectedRangeAndMessageReports.size());
    }

    @Test
    public void testRepeatedCodeInMultipleClassesWithLargeMethods() {
        List<String> code = List.of(
                """
                         public class UserManagement {
                             public String getUserFullName(String firstName, String lastName) {
                                 StringBuilder fullName = new StringBuilder();
                                 if (firstName != null && lastName != null) {
                                     fullName.append(firstName.trim()).append(" ").append(lastName.trim());
                                 } else {
                                     fullName.append("Unknown User");
                                 }
                                 return fullName.toString();
                             }
                        
                             public String getUserInitials(String firstName, String lastName) {
                                 String initials = "";
                                 if (firstName != null && lastName != null) {
                                     initials = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
                                 }
                                 return initials;
                             }
                        
                             public boolean isUserAdult(int age) {
                                 return age >= 18;
                             }
                         }
                        """,
                """
                        public class AdminManagement {
                        
                            public String createAdminFullName(String givenName, String familyName) {
                                StringBuilder fullName = new StringBuilder();
                                if (givenName != null && familyName != null) {
                                    fullName.append(givenName.trim()).append(" ").append(familyName.trim());
                                } else {
                                    fullName.append("No found Admin");
                                    System.out.println("The admin name was not found");
                                }
                        
                                return fullName.toString();
                            }
                        
                            public String generateAdminCode(String givenName, String familyName) {
                                String code = "NA";
                                if (givenName != null && familyName != null) {
                                    code = givenName.substring(0, 1).toUpperCase() + familyName.substring(0, 2).toUpperCase();
                                    System.out.println(code);
                                }
                        
                                if (givenName.length() > 5) {
                                    System.out.println("The admin code is valid");
                                }
                        
                                return code;
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
                        .numberOfHashFunctions(15)
                        .prime(16777619)
                        .build())
                .numberOfBands(5)
                .similarityThreshold(0.4)
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
        assertEquals(6, totalNumberOfReports);
    }
}
