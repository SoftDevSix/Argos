package edu.usb.argos.astprocessor.analyzer.infrastructure.builders.classAnalyzers;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IEntitySignatureBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IPlainTextHasher;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ISimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.INoDuplicateCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.NoRepeatedCodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers.AntlrMethodNormalizer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.SHATextHasher;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.JaccardSimilarityCalculator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.LshSelector;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.MinHashingHandler;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.TokenShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.entitySingnatureBuilders.MethodSignatureBuilder;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class LshNoDuplicatedCodeAnalyzerBuilder implements INoDuplicateCodeAnalyzerBuilder<JavaParser.StatementContext> {

    private final LshConfiguration lshConfiguration;

    @Override
    public ICodeSmellNodeAnalyzer<ClassInformation<JavaParser.StatementContext>> buildAnalyzer(CodeSmellsRules codeSmellsRules, CodeAnalysisReportHandlerByClass reportHandlerByClass) {
        return NoRepeatedCodeAnalyzer.builder()
                .selector(buildLshSelector(lshConfiguration))
                .entitySignatureBuilder(buildEntitySignatureBuilder(lshConfiguration))
                .reportHandlerByClass(reportHandlerByClass)
                .build();
    }

    private LshSelector<CodeIdentity> buildLshSelector(LshConfiguration lshConfiguration) {
        ISimilarityCalculator<List<Integer>> similarityCalculator = new JaccardSimilarityCalculator(lshConfiguration.getMinHashConfiguration());
        return new LshSelector<>(lshConfiguration, similarityCalculator);
    }

    private IEntitySignatureBuilder<CodeIdentity, List<Integer>, ClassInformation<JavaParser.StatementContext>> buildEntitySignatureBuilder(LshConfiguration lshConfiguration) {
        IPlainTextHasher textHasher = new SHATextHasher();
        MinHashingHandler codeMinHash = new MinHashingHandler(lshConfiguration.getMinHashConfiguration(), textHasher);
        IShingleGenerator<String> shingleGenerator = new TokenShingleGenerator(lshConfiguration.getShinglesFrequency());
        INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer = new AntlrMethodNormalizer();

        return new MethodSignatureBuilder(codeMinHash, shingleGenerator, methodNormalizer);
    }
}
