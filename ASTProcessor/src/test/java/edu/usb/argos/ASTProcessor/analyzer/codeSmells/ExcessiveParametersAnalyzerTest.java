package edu.usb.argos.ASTProcessor.analyzer.codeSmells;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells.CodeAnalysisReport;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells.CodeAnalysisReportType;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.ASTProcessor.analyzer.infrastructure.utils.ExcessiveParametersMethodAnalyzer;
import edu.usb.argos.ASTProcessor.analyzer.core.services.ExcessiveParametersAnalyzer;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.ASTProcessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcessiveParametersAnalyzerTest {
    private final int MAX_PARAMETERS = 3;
    private CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass;
    private ExcessiveParametersAnalyzer<JavaParser.StatementContext> excessiveParametersAnalyzer;
    private static JavaMethodVisitor visitor;
    private static ModifierService modifierService;
    private static ParameterService parameterService;
    private static AnnotationService annotationService;

    @BeforeAll
    static void setupTest() {
        modifierService = new ModifierService();
        parameterService = new ParameterService();
        annotationService = new AnnotationService();
        visitor = new JavaMethodVisitor(modifierService, parameterService, annotationService);
    }

    @BeforeEach
    void setupSingleTest() {
        CodeSmellAnalysisByClass codeSmellAnalysisByClass = new CodeSmellAnalysisByClass("JavaPath.java");
        codeAnalysisReportHandlerByClass = new CodeAnalysisReportHandlerByClass(codeSmellAnalysisByClass);
        IMethodLineAnalyzer<JavaParser.StatementContext> methodLineAnalyzer = new ExcessiveParametersMethodAnalyzer();
        excessiveParametersAnalyzer = new ExcessiveParametersAnalyzer<>(MAX_PARAMETERS, methodLineAnalyzer);
        excessiveParametersAnalyzer.setCodeAnalyzerReport(codeAnalysisReportHandlerByClass);
    }

    private List<JavaParser.MethodDeclarationContext> parseMethods(String code) {
        try {
            JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            JavaParser.CompilationUnitContext compilationUnit = parser.compilationUnit();

            List<JavaParser.MethodDeclarationContext> methodDeclarations = new ArrayList<>();

            for (JavaParser.TypeDeclarationContext typeDecl : compilationUnit.typeDeclaration()) {
                if (typeDecl.classDeclaration() != null) {
                    JavaParser.ClassBodyContext classBody = typeDecl.classDeclaration().classBody();
                    for (JavaParser.ClassBodyDeclarationContext bodyDecl : classBody.classBodyDeclaration()) {
                        JavaParser.MemberDeclarationContext memberDecl = bodyDecl.memberDeclaration();
                        if (memberDecl != null) {
                            if (memberDecl.methodDeclaration() != null) {
                                methodDeclarations.add(memberDecl.methodDeclaration());
                            } else if (memberDecl.genericMethodDeclaration() != null) {
                                methodDeclarations.add(memberDecl.genericMethodDeclaration().methodDeclaration());
                            }
                        }
                    }
                }
            }

            return methodDeclarations;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Java code: " + e.getMessage(), e);
        }
    }

    @Test
    public void testAnalyzeForExcessiveParameters() {
        String code = """
                public class TestClass {
                    public void methodWithManyParameters(int a, int b, int c, int d, int e) {
                        int a;
                        int b;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> ctxMethods = parseMethods(code);
        Optional<JavaParser.MethodDeclarationContext> ctx = Optional.ofNullable(ctxMethods.get(0));
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx.get());
        excessiveParametersAnalyzer.analyze(info);
        CodeAnalysisReport report = codeAnalysisReportHandlerByClass.getCodeSmellAnalysisByClass().getCodeAnalysis()
                .get(0);
        CodeAnalysisReportType reportTypeExpected = CodeAnalysisReportType.EXCESSIVE_PARAMETERS;
        String reportMessageExpected = "The method has too many parameters, making it difficult to understand and use.";

        assertEquals(1, codeAnalysisReportHandlerByClass.getCodeSmellAnalysisByClass().getCodeAnalysis().size());
        assertEquals(2, report.getStartLine());
        assertEquals(5, report.getEndLine());
        assertEquals(reportTypeExpected, report.getType());
        assertEquals(reportMessageExpected, report.getMessage());
    }

    @Test
    public void testAnalyzeForTwoMethodExcessiveParameters() {
        String code = """
                public class TestClass {
                    public void methodWith5Params(int numbers, string name, int a, int b, int f) {
                        int c;
                        int d;
                    }

                    public void methodWithFourParams(int number, double num, string name, int d) {
                        int a;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> ctxMethods = parseMethods(code);
        assertNotNull(ctxMethods);
        ctxMethods.forEach(ctx -> {
            MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
            excessiveParametersAnalyzer.analyze(info);
        });
        List<CodeAnalysisReport> reports = codeAnalysisReportHandlerByClass.getCodeSmellAnalysisByClass()
                .getCodeAnalysis();

        assertEquals(2, reports.size());
        assertEquals(2, reports.get(0).getStartLine());
        assertEquals(5, reports.get(0).getEndLine());
        assertEquals(7, reports.get(1).getStartLine());
        assertEquals(9, reports.get(1).getEndLine());
    }

    @Test
    public void testAnalyzeForOneMethodExcessiveParameters() {
        String code = """
                public class TestClass {
                    public void methodWithOneParameter(int number) {
                        int c;
                        int d;
                    }

                    public void methodWithFourParams(int number, double num, string name, int d) {
                        int a;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> ctxMethods = parseMethods(code);
        assertNotNull(ctxMethods);
        ctxMethods.forEach(ctx -> {
            MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
            excessiveParametersAnalyzer.analyze(info);
        });
        List<CodeAnalysisReport> reports = codeAnalysisReportHandlerByClass.getCodeSmellAnalysisByClass()
                .getCodeAnalysis();

        assertEquals(1, reports.size());
        assertEquals(7, reports.get(0).getStartLine());
        assertEquals(9, reports.get(0).getEndLine());
    }

    @Test
    public void testAnalyzeForTwoMethodExcessiveParameters_WithThreeMethods() {
        String code = """
                public class TestClass {
                    public void methodWithOneParameter(int number) {
                        int c;
                        int d;
                    }

                    public void methodWithFourParams(int number, double num, string name, int d) {
                        int a;
                    }
                    public void methodWith5Params(int numbers, string name, int a, int b, int f) {
                        int c;
                        int d;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> ctxMethods = parseMethods(code);
        assertNotNull(ctxMethods);
        ctxMethods.forEach(ctx -> {
            MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
            excessiveParametersAnalyzer.analyze(info);
        });
        List<CodeAnalysisReport> reports = codeAnalysisReportHandlerByClass.getCodeSmellAnalysisByClass()
                .getCodeAnalysis();

        assertEquals(2, reports.size());
        assertEquals(7, reports.get(0).getStartLine());
        assertEquals(9, reports.get(0).getEndLine());
        assertEquals(10, reports.get(1).getStartLine());
        assertEquals(13, reports.get(1).getEndLine());
    }
}