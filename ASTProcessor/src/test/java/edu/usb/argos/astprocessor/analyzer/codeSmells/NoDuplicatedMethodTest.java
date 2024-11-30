package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.core.services.NoRepeatedCodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.cantidateSelectors.LSHCandidateSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.CodeMinHashConfig;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.LSHConfig;
import edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers.AntlrMethodNormalizer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.CodeMinHash;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.TokenShingleGenerator;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoDuplicatedMethodTest {

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

    @Test
    public void testDuplicatedCode() {
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

        Optional<ParserRuleContext> classContext = reader.readFile(code);
        assertTrue(classContext.isPresent());
        ClassInformation<JavaParser.StatementContext> classInformation = classVisitor.visitClass(classContext.get());

        CodeSmellAnalysisByClass codeSmellAnalysisByClass = new CodeSmellAnalysisByClass("JavaPathTest.java");
        CodeAnalysisReportHandlerByClass reportHandlerByClass = new CodeAnalysisReportHandlerByClass(codeSmellAnalysisByClass);

        IPlainTextHasher textHasher = new SHATextHasher();
        CodeMinHashConfig codeMinHashConfig = CodeMinHashConfig
                .builder()
                .numHashFunctions(4)
                .prime(16777619)
                .build();
        LSHConfig lshConfig = LSHConfig.builder()
                .shinglesFrequency(3)
                .prime(16777619)
                .numBands(2)
                .numHashFunctions(4)
                .similarityThreshold(0.5)
                .build();

        CodeMinHash codeMinHash = new CodeMinHash(codeMinHashConfig, textHasher);
        IShingleGenerator<String> shingleGenerator = new TokenShingleGenerator(lshConfig.getShinglesFrequency());
        INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer = new AntlrMethodNormalizer();
        LSHCandidateSelector candidateSelector = new LSHCandidateSelector(lshConfig, codeMinHash);
        NoRepeatedCodeAnalyzer repeatedCodeAnalyzer = new NoRepeatedCodeAnalyzer(codeMinHash, shingleGenerator, methodNormalizer, candidateSelector);
        repeatedCodeAnalyzer.setCodeAnalyzerReport(reportHandlerByClass);
        repeatedCodeAnalyzer.analyze(classInformation);

        assertEquals(12, reportHandlerByClass.codeSmellAnalysisByClass().getCodeAnalysis().size());
    }
}
