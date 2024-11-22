package edu.usb.argos.ASTProcessor.analyzer.core.services;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import java.util.Optional;
import java.util.function.Consumer;

@AllArgsConstructor
public class MethodTooLongAnalyzer implements ICodeSmellNodeAnalyzer<MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream>> {

    private final int MAX_METHOD_LENGTH;
    private Optional<CodeAnalysisReportHandlerByClass> analysisReportHandlerByClass;

    @Override
    public void analyze(MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream> method) {
        analysisReportHandlerByClass.ifPresent(handleMethodToLongAnalysis(method));
    }

    @Override
    public void setCodeAnalyzerReport(CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass) {
        this.analysisReportHandlerByClass = Optional.of(codeAnalysisReportHandlerByClass);
    }

    public Consumer<CodeAnalysisReportHandlerByClass> handleMethodToLongAnalysis(MethodInformation<JavaParser.StatementContext, JavaParser.ExpressionContext, CommonTokenStream> method) {
        return (report) -> {
            CommonTokenStream tokenStream = method.getTokens().getTokenStream();
            Token startToken = tokenStream.get(0);
            Token stopToken = tokenStream.get(tokenStream.size() - 1);

            int startLine = startToken.getLine() + 1;
            int endLine = stopToken.getLine();
            int methodLength = endLine - startLine;

            if (methodLength > MAX_METHOD_LENGTH) {
                report.addMethodWithMethodTooLong(startLine, endLine);
            }
        };
    }
}
