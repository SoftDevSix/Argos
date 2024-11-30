package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeRange;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.OrderedPair;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.cantidateSelectors.LSHCandidateSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.CodeMinHash;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class NoRepeatedCodeAnalyzer implements ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>> {

    private final CodeMinHash codeMinHash;
    private final IShingleGenerator<String> shingleGenerator;
    private final INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer;
    private final LSHCandidateSelector candidateSelector;
    private Optional<CodeAnalysisReportHandlerByClass> reportHandlerByClass;

    public NoRepeatedCodeAnalyzer(CodeMinHash codeMinHash, IShingleGenerator<String> shingleGenerator, INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer, LSHCandidateSelector candidateSelector) {
        this.codeMinHash = codeMinHash;
        this.shingleGenerator = shingleGenerator;
        this.methodNormalizer = methodNormalizer;
        this.candidateSelector = candidateSelector;
    }

    @Override
    public void setCodeAnalyzerReport(CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass) {
        reportHandlerByClass = Optional.of(codeAnalysisReportHandlerByClass);
    }

    @Override
    public void analyze(ClassInformation<JavaParser.StatementContext> classNode) {
        reportHandlerByClass.ifPresent(report -> {
            List<CodeIdentity<List<Integer>>> methods = findMethodsFromClass(classNode);
            Set<OrderedPair<UUID>> candidates = candidateSelector.findCandidatePairs(methods);
            Map<UUID, CodeIdentity<List<Integer>>> methodMap = new HashMap<>();
            for (CodeIdentity<List<Integer>> method : methods) {
                methodMap.put(method.getId(), method);
            }
            Set<OrderedPair<UUID>> duplicatedMethods = candidateSelector.filterCandidates(candidates, methodMap);
            duplicatedMethods.forEach(dup -> {
                CodeIdentity<List<Integer>> firstMethodDuplicated = methodMap.get(dup.firstElement());
                CodeIdentity<List<Integer>> secondMethodDuplicated = methodMap.get(dup.secondElement());
                report.addMethodWithNoDuplicatedCode(
                        firstMethodDuplicated.getCodeRange().startLine(),
                        firstMethodDuplicated.getCodeRange().endLine(),
                        secondMethodDuplicated.getIdentifier()
                );
                report.addMethodWithNoDuplicatedCode(
                        secondMethodDuplicated.getCodeRange().startLine(),
                        secondMethodDuplicated.getCodeRange().endLine(),
                        firstMethodDuplicated.getIdentifier()
                );
            });
        });
    }

    private List<CodeIdentity<List<Integer>>> findMethodsFromClass(ClassInformation<JavaParser.StatementContext> classNode) {
        List<MethodInformation<JavaParser.StatementContext>> methods = classNode.getMembers().getMethods();
        List<CodeIdentity<List<Integer>>> methodContexts = new ArrayList<>();

        for (MethodInformation<JavaParser.StatementContext> method : methods) {
            Optional<JavaParser.StatementContext> firstStatement = Optional.ofNullable(method.getStatements().get(0));
            if (firstStatement.isPresent()) {
                Optional<JavaParser.MethodDeclarationContext> methodNode = findEnclosingMethod(firstStatement.get());
                if (methodNode.isPresent()) {
                    List<String> tokens = methodNormalizer.normalize(methodNode.get());
                    List<List<String>> singles = shingleGenerator.generate(tokens);
                    List<String> flatSingles = shingleGenerator.flatSingles(singles);
                    List<Integer> signature = codeMinHash.computeMinHash(flatSingles);

                    CodeRange codeRange = new CodeRange(methodNode.get().getStart().getLine(), methodNode.get().getStop().getLine());

                    StringBuilder methodPath = new StringBuilder();
                    classNode.getIdentity().getPackageName().ifPresent(path -> methodPath.append(path).append("/"));
                    classNode.getIdentity().getName().ifPresent(path -> methodPath.append(path).append(".java").append("/"));
                    methodPath.append(method.getName());

                    methodContexts.add(CodeIdentity.<List<Integer>>builder()
                            .identifier(methodPath.toString())
                            .signature(signature)
                            .codeRange(codeRange)
                            .signature(signature)
                            .build());
                }
            }
        }

        return methodContexts;
    }

    private Optional<JavaParser.MethodDeclarationContext> findEnclosingMethod(ParserRuleContext context) {
        ParserRuleContext currentContext = context;

        while (currentContext != null) {
            if (currentContext instanceof JavaParser.MethodDeclarationContext) {
                return Optional.of((JavaParser.MethodDeclarationContext) currentContext);
            }

            currentContext = currentContext.getParent();
        }

        return Optional.empty();
    }
}
