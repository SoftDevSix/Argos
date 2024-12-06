package edu.usb.argos.astprocessor.analyzer.core.services;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
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
import lombok.Builder;
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
    private final List<EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>>> methods;
    private final Map<UUID, EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>>> methodMap;

    @Getter
    private final CodeAnalysisReportHandlerByClass reportHandlerByClass;
    private final LshSelector<CodeIdentity<CodeSmellAnalysisByClass>> candidateSelector;
    private final IEntitySignatureBuilder<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>, ClassInformation<JavaParser.StatementContext>> entitySignatureBuilder;

    @Builder
    public NoRepeatedCodeAnalyzer(
            LshSelector<CodeIdentity<CodeSmellAnalysisByClass>> selector,
            CodeAnalysisReportHandlerByClass reportHandlerByClass,
            IEntitySignatureBuilder<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>, ClassInformation<JavaParser.StatementContext>> entitySignatureBuilder) {
        this.methodsAdded = new HashSet<>();
        this.methods = new ArrayList<>();
        this.methodMap = new HashMap<>();

        this.candidateSelector = selector;
        this.reportHandlerByClass = reportHandlerByClass;
        this.entitySignatureBuilder = entitySignatureBuilder;
    }

    @Override
    public void analyze(ClassInformation<JavaParser.StatementContext> classNode) {
        reportHandlerByClass.getCodeSmellAnalysisByClass().ifPresent(report -> {
            List<EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>>> classMethods = entitySignatureBuilder.buildMultipleFromResource(classNode, report);
            methods.addAll(classMethods);
            classMethods.forEach(method -> methodMap.put(method.getId(), method));
            Set<OrderedPair<UUID>> duplicatedMethods = findDuplicatedMethods();
            duplicatedMethods.forEach(this::handleDuplicatedMethodPair);
        });
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

        EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>> firstMethod = methodMap.get(pair.firstElement());
        EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>> secondMethod = methodMap.get(pair.secondElement());

        reportDuplicate(firstMethod, secondMethod);
        reportDuplicate(secondMethod, firstMethod);

        methodsAdded.add(unorderedPair);
    }

    private void reportDuplicate(EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>> fromMethod,
                                 EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>> toMethod) {
        CodeAnalysisReportHandlerByClass
                .of(fromMethod.getEntity().getOrigin())
                .addMethodWithNoDuplicatedCode(
                        fromMethod.getEntity().getCodeRange().startLine(),
                        fromMethod.getEntity().getCodeRange().endLine(),
                        toMethod.getEntity().getIdentifier()
                );
    }
}
