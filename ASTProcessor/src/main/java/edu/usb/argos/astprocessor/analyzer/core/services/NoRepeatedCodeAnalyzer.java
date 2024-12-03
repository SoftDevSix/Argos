package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.EntityWithSignature;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.OrderedPair;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.Pair;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IEntitySignatureBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.LshSelector;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NoRepeatedCodeAnalyzer implements ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>> {

    private final Set<Pair<UUID>> methodsAdded;
    private final List<EntityWithSignature<CodeIdentity, List<Integer>>> methods;
    private final Map<UUID, EntityWithSignature<CodeIdentity, List<Integer>>> methodMap;

    @Getter
    private final CodeAnalysisReportHandlerByClass reportHandlerByClass;
    private final LshSelector<CodeIdentity> candidateSelector;
    private final IEntitySignatureBuilder<CodeIdentity, List<Integer>, ClassInformation<JavaParser.StatementContext>> entitySignatureBuilder;

    public NoRepeatedCodeAnalyzer(
            LshSelector<CodeIdentity> selector,
            CodeAnalysisReportHandlerByClass reportHandlerByClass,
            IEntitySignatureBuilder<CodeIdentity, List<Integer>, ClassInformation<JavaParser.StatementContext>> entitySignatureBuilder) {
        this.methodsAdded = new HashSet<>();
        this.methods = new ArrayList<>();
        this.methodMap = new HashMap<>();

        this.candidateSelector = selector;
        this.reportHandlerByClass = reportHandlerByClass;
        this.entitySignatureBuilder = entitySignatureBuilder;
    }

    @Override
    public void analyze(ClassInformation<JavaParser.StatementContext> classNode) {
        List<EntityWithSignature<CodeIdentity, List<Integer>>> classMethods = entitySignatureBuilder.buildMultipleFromResource(classNode);
        methods.addAll(classMethods);
        classMethods.forEach(method -> methodMap.put(method.getId(), method));
        Set<OrderedPair<UUID>> duplicatedMethods = findDuplicatedMethods();
        duplicatedMethods.forEach(this::handleDuplicatedMethodPair);
    }

    private Set<OrderedPair<UUID>> findDuplicatedMethods() {
        Set<OrderedPair<UUID>> candidates = candidateSelector.findCandidatePairs(methods);
        return candidateSelector.filterCandidates(candidates, methodMap);
    }

    private void handleDuplicatedMethodPair(OrderedPair<UUID> pair) {
        Pair<UUID> unorderedPair = new Pair<>(pair.firstElement(), pair.secondElement());
        if (methodsAdded.contains(unorderedPair)) {
            return;
        }

        EntityWithSignature<CodeIdentity, List<Integer>> firstMethod = methodMap.get(pair.firstElement());
        EntityWithSignature<CodeIdentity, List<Integer>> secondMethod = methodMap.get(pair.secondElement());

        reportDuplicate(firstMethod, secondMethod);
        reportDuplicate(secondMethod, firstMethod);

        methodsAdded.add(unorderedPair);
    }

    private void reportDuplicate(EntityWithSignature<CodeIdentity, List<Integer>> fromMethod,
                                 EntityWithSignature<CodeIdentity, List<Integer>> toMethod) {
        reportHandlerByClass.addMethodWithNoDuplicatedCode(
                fromMethod.getEntity().getCodeRange().startLine(),
                fromMethod.getEntity().getCodeRange().endLine(),
                toMethod.getEntity().getIdentifier()
        );
    }
}
