package edu.usb.argos.astprocessor.analyzer.codeSmells;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReport;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeAnalysisReportType;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IMethodLineAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.services.MethodTooLongAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.MethodLineAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.astprocessor.visitor.core.services.method.AnnotationService;
import edu.usb.argos.astprocessor.visitor.core.services.method.ModifierService;
import edu.usb.argos.astprocessor.visitor.core.services.method.ParameterService;
import edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
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

class MethodTooLongAnalyzerTest {

    private final int MAX_METHOD_LENGTH = 3;
    private CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass;
    private CodeSmellAnalysisByClass codeSmellAnalysisByClass;
    private MethodTooLongAnalyzer<JavaParser.StatementContext> methodTooLongAnalyzer;
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
        codeSmellAnalysisByClass = new CodeSmellAnalysisByClass("JavaPath.java");
        codeAnalysisReportHandlerByClass = new CodeAnalysisReportHandlerByClass(codeSmellAnalysisByClass);
        IMethodLineAnalyzer<JavaParser.StatementContext> methodLineAnalyzer = new MethodLineAnalyzer();
        methodTooLongAnalyzer = new MethodTooLongAnalyzer<>(MAX_METHOD_LENGTH, methodLineAnalyzer, codeAnalysisReportHandlerByClass);
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
    public void testForMethodToLong() {
        String code = """
                public class MyClass {
                    public int largeMethod(int[] numbers) {
                        int sum = 0;
                        for (int number : numbers) {
                            sum += number;
                        }
                
                        return sum;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> ctxMethods = parseMethods(code);
        assertNotNull(ctxMethods, "Method context should not be null");
        Optional<JavaParser.MethodDeclarationContext> ctx = Optional.ofNullable(ctxMethods.get(0));
        assertTrue(ctx.isPresent());
        MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx.get());
        methodTooLongAnalyzer.analyze(info);

        int numberOfReportsExpected = 1;
        assertEquals(numberOfReportsExpected, codeSmellAnalysisByClass.getCodeAnalysis().size());

        CodeAnalysisReport report = codeSmellAnalysisByClass.getCodeAnalysis().get(0);
        int startMethodLineExpected = 2;
        int endMethodLineExpected = 9;
        CodeAnalysisReportType reportTypeExpected = CodeAnalysisReportType.METHOD_TOO_LONG;
        String reportMessageExpected = "The method is too long, consider breaking it down into smaller, reusable methods.";

        assertEquals(startMethodLineExpected, report.getStartLine());
        assertEquals(endMethodLineExpected, report.getEndLine());
        assertEquals(reportTypeExpected, report.getType());
        assertEquals(reportMessageExpected, report.getMessage());
    }

    @Test
    public void testSecondMethodTooLong() {
        String code = """
                public class MyClass {
                    public void shortMethod() {
                        int a = 1;
                        int b = 2;
                    }
                
                    public int largeMethod(int[] numbers) {
                        int sum = 0;
                        for (int number : numbers) {
                            sum += number;
                        }
                        return sum;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> methods = parseMethods(code);
        assertEquals(2, methods.size(), "Should parse two methods");

        for (JavaParser.MethodDeclarationContext ctx : methods) {
            MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
            methodTooLongAnalyzer.analyze(info);
        }

        int numberOfReportsExpected = 1;
        assertEquals(numberOfReportsExpected, codeSmellAnalysisByClass.getCodeAnalysis().size());

        CodeAnalysisReport report = codeSmellAnalysisByClass.getCodeAnalysis().get(0);
        int startMethodLineExpected = 7;
        int endMethodLineExpected = 13;
        assertEquals(startMethodLineExpected, report.getStartLine());
        assertEquals(endMethodLineExpected, report.getEndLine());
    }

    @Test
    public void testNoMethodTooLong() {
        String code = """
                public class MyClass {
                    public void shortMethod1() {
                        int a = 1;
                        int b = 2;
                    }
                
                    public void shortMethod2() {
                        int x = 10;
                        int y = 20;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> methods = parseMethods(code);
        assertEquals(2, methods.size(), "Should parse two methods");

        for (JavaParser.MethodDeclarationContext ctx : methods) {
            MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
            methodTooLongAnalyzer.analyze(info);
        }

        int numberOfReportsExpected = 0;
        assertEquals(numberOfReportsExpected, codeSmellAnalysisByClass.getCodeAnalysis().size());
    }

    @Test
    public void testAllMethodsTooLong() {
        String code = """
                public class MyClass {
                    public void largeMethod1(int[] numbers) {
                        int sum = 0;
                        int sum1 = 0;
                        int sum2 = 0;
                        int sum3 = 0;
                        int sum4 = 0;
                    }
                
                    public void largeMethod2(int[] numbers) {
                        int sum = 0;
                        int sum1 = 0;
                        int sum2 = 0;
                        int sum3 = 0;
                        int sum4 = 0;
                        int sum5 = 0;
                    }
                }
                """;

        List<JavaParser.MethodDeclarationContext> methods = parseMethods(code);
        assertEquals(2, methods.size(), "Should parse two methods");

        for (JavaParser.MethodDeclarationContext ctx : methods) {
            MethodInformation<JavaParser.StatementContext> info = visitor.visitMethodDeclaration(ctx);
            methodTooLongAnalyzer.analyze(info);
        }

        int numberOfReportsExpected = 2;
        assertEquals(numberOfReportsExpected, codeSmellAnalysisByClass.getCodeAnalysis().size());
    }
}
