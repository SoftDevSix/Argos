package edu.usb.argos.ASTProcessor.analyzer.codeSmells;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.services.MethodTooLongAnalyzer;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.ExpressionCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.ModifierCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.StatementCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaClassVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.AttributeHandler;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassIdentityCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassStructureCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodTooLongAnalyzerTest {

    private static CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass;
    private static MethodTooLongAnalyzer methodTooLongAnalyzer;

    @BeforeEach
    public void setupForEachTest() {
        CodeSmellAnalysisByClass codeSmellAnalysisByClass = new CodeSmellAnalysisByClass("some_path.java");
        codeAnalysisReportHandlerByClass = new CodeAnalysisReportHandlerByClass(codeSmellAnalysisByClass);

        methodTooLongAnalyzer = new MethodTooLongAnalyzer(3, Optional.of(codeAnalysisReportHandlerByClass));
    }

    private Optional<ClassInformation> getClassFromText(String classBody) {
        CharStream charStream = CharStreams.fromString(classBody);
        JavaLexer lexer = new JavaLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokenStream);

        JavaMethodVisitor methodVisitor = new JavaMethodVisitor(
                tokenStream,
                new ExpressionCollector(),
                new StatementCollector(),
                new ModifierCollector()
        );

        AttributeHandler attributeHandler = new AttributeHandler();
        JavaAttributeVisitor attributeVisitor = new JavaAttributeVisitor(attributeHandler);

        JavaConstructorVisitor constructorVisitor = new JavaConstructorVisitor();

        JavaClassIdentityCollector identityCollector = new JavaClassIdentityCollector();
        JavaClassStructureCollector structureCollector = new JavaClassStructureCollector();
        JavaClassMemberCollector memberCollector = new JavaClassMemberCollector(
                methodVisitor,
                attributeVisitor,
                constructorVisitor
        );

        JavaClassVisitor visitor = new JavaClassVisitor(identityCollector, structureCollector, memberCollector);

        JavaParser.CompilationUnitContext context = parser.compilationUnit();
        JavaParser.ClassDeclarationContext classCtx = context.typeDeclaration(0).classDeclaration();

        ClassInformation classInformation = visitor.visitClass(classCtx);

        return Optional.of(classInformation);
    }

    @Test
    public void testForMethodToLong() {
        Optional<ClassInformation> classBody = getClassFromText("""
                public class MyClass {
                    public int calculateSum(int[] numbers) {
                        int sum = 0;
                        for (int number : numbers) {
                            sum += number;
                        }
                        return sum;
                    }
                
                    public String reverseString(String input) {
                        StringBuilder reversed = new StringBuilder();
                        for (int i = input.length() - 1; i >= 0; i--) {
                            reversed.append(input.charAt(i));
                        }
                        return reversed.toString();
                    }
                
                    public boolean isEven(int number) {
                        return number % 2 == 0;
                    }
                }
                """);

        assertTrue(classBody.isPresent());

        for (MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream> method : classBody.get().getMembers().getMethods()) {
            methodTooLongAnalyzer.analyze(method);
        }
        codeAnalysisReportHandlerByClass.getCodeSmellAnalysisByClass().getCodeAnalysis().forEach(System.out::println);
    }


}
