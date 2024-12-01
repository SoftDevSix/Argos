package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeRange;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.EntityWithSignature;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.OrderedPair;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.Pair;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.LshSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.MinHashingHandler;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
public class NoRepeatedCodeAnalyzer implements ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>> {

    private final List<EntityWithSignature<CodeIdentity, List<Integer>>> methods;
    private final Map<UUID, EntityWithSignature<CodeIdentity, List<Integer>>> methodMap;
    private final Set<Pair<UUID>> methodsAdded;

    private final MinHashingHandler minHashingHandler;
    private final IShingleGenerator<String> shingleGenerator;
    private final INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer;
    private final LshSelector<CodeIdentity> candidateSelector;
    private CodeAnalysisReportHandlerByClass reportHandlerByClass;

    public NoRepeatedCodeAnalyzer(MinHashingHandler minHashingHandler, IShingleGenerator<String> shingleGenerator, INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer, LshSelector<CodeIdentity> selector, CodeAnalysisReportHandlerByClass reportHandlerByClass) {
        this.methods = new ArrayList<>();
        this.methodsAdded = new HashSet<>();
        this.methodMap = new HashMap<>();

        this.reportHandlerByClass = reportHandlerByClass;
        this.minHashingHandler = minHashingHandler;
        this.shingleGenerator = shingleGenerator;
        this.methodNormalizer = methodNormalizer;
        this.candidateSelector = selector;
    }

    @Override
    public void analyze(ClassInformation<JavaParser.StatementContext> classNode) {
        List<EntityWithSignature<CodeIdentity, List<Integer>>> classMethods = findMethodsFromClass(classNode);
        methods.addAll(classMethods);
        Set<OrderedPair<UUID>> candidates = candidateSelector.findCandidatePairs(methods);
        for (EntityWithSignature<CodeIdentity, List<Integer>> method : classMethods) {
            methodMap.put(method.getId(), method);
        }
        Set<OrderedPair<UUID>> duplicatedMethods = candidateSelector.filterCandidates(candidates, methodMap);
        duplicatedMethods.forEach(dup -> {
            Pair<UUID> pair = new Pair<>(dup.firstElement(), dup.secondElement());

            if (!methodsAdded.contains(pair)) {
                EntityWithSignature<CodeIdentity, List<Integer>> firstMethodDuplicated = methodMap.get(dup.firstElement());
                EntityWithSignature<CodeIdentity, List<Integer>> secondMethodDuplicated = methodMap.get(dup.secondElement());
                reportHandlerByClass.addMethodWithNoDuplicatedCode(
                        firstMethodDuplicated.getEntity().getCodeRange().startLine(),
                        firstMethodDuplicated.getEntity().getCodeRange().endLine(),
                        secondMethodDuplicated.getEntity().getIdentifier()
                );
                reportHandlerByClass.addMethodWithNoDuplicatedCode(
                        secondMethodDuplicated.getEntity().getCodeRange().startLine(),
                        secondMethodDuplicated.getEntity().getCodeRange().endLine(),
                        firstMethodDuplicated.getEntity().getIdentifier()
                );
                methodsAdded.add(pair);
            }
        });
    }

    private List<EntityWithSignature<CodeIdentity, List<Integer>>> findMethodsFromClass(ClassInformation<JavaParser.StatementContext> classNode) {
        List<MethodInformation<JavaParser.StatementContext>> methods = classNode.getMembers().getMethods();
        List<EntityWithSignature<CodeIdentity, List<Integer>>> methodContexts = new ArrayList<>();

        for (MethodInformation<JavaParser.StatementContext> method : methods) {
            Optional<JavaParser.StatementContext> firstStatement = Optional.ofNullable(method.getStatements().get(0));
            if (firstStatement.isPresent()) {
                Optional<JavaParser.MethodDeclarationContext> methodNode = findEnclosingMethod(firstStatement.get());
                if (methodNode.isPresent()) {
                    List<String> tokens = methodNormalizer.normalize(methodNode.get());
                    List<List<String>> singles = shingleGenerator.generate(tokens);
                    List<String> flatSingles = shingleGenerator.flatSingles(singles);
                    List<Integer> signature = minHashingHandler.computeMinHash(flatSingles);

                    CodeRange codeRange = new CodeRange(methodNode.get().getStart().getLine(), methodNode.get().getStop().getLine());

                    StringBuilder methodPath = new StringBuilder();
                    classNode.getIdentity().getPackageName().ifPresent(path -> methodPath.append(path).append("/"));
                    classNode.getIdentity().getName().ifPresent(path -> methodPath.append(path).append(".java").append("/"));
                    methodPath.append(method.getName());

                    CodeIdentity codeIdentity = CodeIdentity
                            .builder()
                            .identifier(methodPath.toString())
                            .codeRange(codeRange)
                            .build();

                    methodContexts.add(EntityWithSignature.<CodeIdentity, List<Integer>>builder()
                            .signature(signature)
                            .entity(codeIdentity)
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
