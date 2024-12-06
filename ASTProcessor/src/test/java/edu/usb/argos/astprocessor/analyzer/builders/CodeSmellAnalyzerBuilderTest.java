package edu.usb.argos.astprocessor.analyzer.builders;

import edu.usb.argos.astprocessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.IMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.builders.INoDuplicateCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.core.services.ExcessiveParametersAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.services.MethodTooLongAnalyzer;
import edu.usb.argos.astprocessor.analyzer.core.services.NoRepeatedCodeAnalyzer;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.CodeSmellsBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.classAnalyzers.LshNoDuplicatedCodeAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrExcessiveParametersAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.builders.methodAnalyzers.AntlrMethodTooLongAnalyzerBuilder;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.LshConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.config.algorithms.MinHashConfiguration;
import edu.usb.argos.astprocessor.analyzer.infrastructure.dtos.rules.CodeSmellsRules;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeSmellAnalyzerBuilderTest {

    private CodeAnalysisReportHandlerByClass reportHandlerByClass;

    @BeforeEach
    public void setupEachTest() {
        reportHandlerByClass = new CodeAnalysisReportHandlerByClass();
    }

    private IMethodTooLongAnalyzerBuilder<JavaParser.StatementContext> buildMethodTooAnalyzerLongFromRules() {
        return new AntlrMethodTooLongAnalyzerBuilder();
    }

    private IExcessiveParametersAnalyzerBuilder<JavaParser.StatementContext> buildAntlrExcessiveParametersAnalyzerBuilder() {
        return new AntlrExcessiveParametersAnalyzerBuilder();
    }

    private INoDuplicateCodeAnalyzerBuilder<JavaParser.StatementContext> buildDuplicatedAnalyzer() {
        LshConfiguration configuration = LshConfiguration.builder()
                .shinglesFrequency(3)
                .minHashConfiguration(MinHashConfiguration
                        .builder()
                        .seed(7)
                        .numberOfHashFunctions(15)
                        .prime(16777619)
                        .build())
                .numberOfBands(5)
                .similarityThreshold(0.4)
                .build();

        return new LshNoDuplicatedCodeAnalyzerBuilder(configuration);
    }

    @Test
    public void testOnlyMethodTooLongAnalyzerBuilt() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .methodTooLong(true)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = new CodeSmellsBuilder<>(
                codeSmellsRules,
                reportHandlerByClass,
                buildMethodTooAnalyzerLongFromRules(),
                buildAntlrExcessiveParametersAnalyzerBuilder(),
                buildDuplicatedAnalyzer()
        );

        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();
        Optional<List<ICodeSmellNodeAnalyzer<?>>> analyzer = Optional.ofNullable(analyzerMap.get(MethodInformation.class));
        assertTrue(analyzer.isPresent());
        assertFalse(analyzer.get().isEmpty());

        ICodeSmellNodeAnalyzer<?> methodAnalyzer = analyzer.get().get(0);
        assertInstanceOf(MethodTooLongAnalyzer.class, methodAnalyzer);
    }

    @Test
    public void testOnlyExcessiveParametersAnalyzerBuilt() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .excessiveParameters(true)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = new CodeSmellsBuilder<>(
                codeSmellsRules,
                reportHandlerByClass,
                buildMethodTooAnalyzerLongFromRules(),
                buildAntlrExcessiveParametersAnalyzerBuilder(),
                buildDuplicatedAnalyzer()
        );

        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();
        Optional<List<ICodeSmellNodeAnalyzer<?>>> analyzer = Optional.ofNullable(analyzerMap.get(MethodInformation.class));
        assertTrue(analyzer.isPresent());
        assertFalse(analyzer.get().isEmpty());

        ICodeSmellNodeAnalyzer<?> methodAnalyzer = analyzer.get().get(0);
        assertInstanceOf(ExcessiveParametersAnalyzer.class, methodAnalyzer);
    }

    @Test
    public void testOnlyNoDuplicatedCodeAnalyzerBuilt() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .noDuplicatedCode(true)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = new CodeSmellsBuilder<>(
                codeSmellsRules,
                reportHandlerByClass,
                buildMethodTooAnalyzerLongFromRules(),
                buildAntlrExcessiveParametersAnalyzerBuilder(),
                buildDuplicatedAnalyzer()
        );

        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();
        Optional<List<ICodeSmellNodeAnalyzer<?>>> analyzer = Optional.ofNullable(analyzerMap.get(ClassInformation.class));
        assertTrue(analyzer.isPresent());
        assertFalse(analyzer.get().isEmpty());

        ICodeSmellNodeAnalyzer<?> methodAnalyzer = analyzer.get().get(0);
        assertInstanceOf(NoRepeatedCodeAnalyzer.class, methodAnalyzer);
    }

    @Test
    public void testWithAllCodeSmellsAnalyzers() {
        CodeSmellsRules codeSmellsRules = CodeSmellsRules.builder()
                .excessiveParameters(true)
                .methodTooLong(true)
                .noDuplicatedCode(true)
                .build();

        CodeSmellsBuilder<JavaParser.StatementContext> codeSmellsBuilder = new CodeSmellsBuilder<>(
                codeSmellsRules,
                reportHandlerByClass,
                buildMethodTooAnalyzerLongFromRules(),
                buildAntlrExcessiveParametersAnalyzerBuilder(),
                buildDuplicatedAnalyzer()
        );

        HashMap<Class<?>, List<ICodeSmellNodeAnalyzer<?>>> analyzerMap = codeSmellsBuilder.getAnalyzers();

        int expectedTypesOfAnalyzers = 2;
        assertEquals(expectedTypesOfAnalyzers, analyzerMap.size());

        int expectedTotalOfAnalyzers = 3;
        assertEquals(expectedTotalOfAnalyzers, analyzerMap.values()
                .stream()
                .mapToInt(List::size)
                .sum());
    }
}
