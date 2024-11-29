package edu.usb.argos.ASTProcessor.complexity.services;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityResult;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityLevel;
import edu.usb.argos.ASTProcessor.complexity.core.services.analyzers.ClassCyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.analyzers.MethodCyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.analyzers.ConstructorCyclomaticComplexityAnalyzer;
import edu.usb.argos.ASTProcessor.complexity.core.services.rules.ComplexityRulesManager;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters.ClassAdapter;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters.MethodAdapter;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.adapters.ConstructorAdapter;
import edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers.JavaStatementAnalyzer;
import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.*;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ClassCyclomaticComplexityAnalyzerTest {
    private ComplexityRulesManager rulesManager;
    private JavaStatementAnalyzer statementAnalyzer;

    @BeforeEach
    void setUp() {
        rulesManager = new ComplexityRulesManager();
        statementAnalyzer = new JavaStatementAnalyzer();
    }

    private ClassInformation<JavaParser.StatementContext> createTestClassInfo(String className, List<MethodInformation<JavaParser.StatementContext>> methods, List<ConstructorInformation<JavaParser.StatementContext>> constructors) {
        ClassIdentity identity = ClassIdentity.builder()
                .name(Optional.of(className))
                .packageName(Optional.empty())
                .modifiers(Collections.emptyList())
                .annotations(Collections.emptyList())
                .build();
        ClassStructure structure = ClassStructure.builder()
                .superClass(Optional.empty())
                .interfaces(Collections.emptyList())
                .build();
        ClassMembers<JavaParser.StatementContext> members = ClassMembers.<JavaParser.StatementContext>builder()
                .methods(methods)
                .attributes(Collections.emptyList())
                .constructors(constructors)
                .build();
        return ClassInformation.<JavaParser.StatementContext>builder()
                .identity(identity)
                .structure(structure)
                .members(members)
                .build();
    }


    @Test
    void shouldAnalyzeSimpleClass() {
        List<MethodInformation<JavaParser.StatementContext>> methods = Collections.singletonList(
                MethodInformation.<JavaParser.StatementContext>builder()
                        .name("simpleMethod")
                        .returnType("void")
                        .modifiers(List.of("public"))
                        .parameters(Collections.emptyList())
                        .statements(Collections.emptyList())
                        .throwsExceptions(Collections.emptyList())
                        .annotations(Collections.emptyList())
                        .isVarArgs(false)
                        .build()
        );

        ClassInformation<JavaParser.StatementContext> classInfo = createTestClassInfo("SimpleClass", methods, Collections.emptyList());
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        MethodCyclomaticComplexityAnalyzer methodAnalyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(new MethodAdapter(classInfo.getMembers().getMethods().get(0)))
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ClassCyclomaticComplexityAnalyzer analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .methodAnalyzer(methodAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("SimpleClass", result.getElementName());
        assertEquals(2, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
    }

    @Test
    void shouldAnalyzeComplexClass() {
        List<ConstructorInformation<JavaParser.StatementContext>> constructors = Collections.singletonList(
                ConstructorInformation.<JavaParser.StatementContext>builder()
                        .name("ComplexClass")
                        .modifiers(List.of("public"))
                        .parameters(Collections.emptyList())
                        .bodyStatements(Collections.emptyList())
                        .build()
        );

        List<MethodInformation<JavaParser.StatementContext>> methods = Collections.singletonList(
                MethodInformation.<JavaParser.StatementContext>builder()
                        .name("complexMethod")
                        .returnType("void")
                        .modifiers(List.of("public"))
                        .parameters(Collections.emptyList())
                        .statements(Collections.emptyList())
                        .throwsExceptions(Collections.emptyList())
                        .annotations(Collections.emptyList())
                        .isVarArgs(false)
                        .build()
        );

        ClassInformation<JavaParser.StatementContext> classInfo = createTestClassInfo("ComplexClass", methods, constructors);
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        ConstructorCyclomaticComplexityAnalyzer constructorAnalyzer = ConstructorCyclomaticComplexityAnalyzer.builder()
                .target(new ConstructorAdapter(classInfo.getMembers().getConstructors().get(0)))
                .rulesManager(rulesManager)
                .statementAnalyzer(statementAnalyzer)
                .build();

        MethodCyclomaticComplexityAnalyzer methodAnalyzer = MethodCyclomaticComplexityAnalyzer.builder()
                .target(new MethodAdapter(classInfo.getMembers().getMethods().get(0)))
                .rulesManager(rulesManager)
                .statementNodeAnalyzer(statementAnalyzer)
                .build();

        ClassCyclomaticComplexityAnalyzer analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .methodAnalyzer(methodAnalyzer)
                .constructorAnalyzer(constructorAnalyzer)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("ComplexClass", result.getElementName());
        assertEquals(3, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
    }

    @Test
    void shouldAnalyzeEmptyClass() {
        ClassInformation<JavaParser.StatementContext> classInfo = createTestClassInfo("EmptyClass", Collections.emptyList(), Collections.emptyList());
        ClassAdapter classAdapter = new ClassAdapter(classInfo);

        ClassCyclomaticComplexityAnalyzer analyzer = ClassCyclomaticComplexityAnalyzer.builder()
                .target(classAdapter)
                .rulesManager(rulesManager)
                .build();

        ComplexityResult result = analyzer.analyze();

        assertEquals("EmptyClass", result.getElementName());
        assertEquals(1, result.getComplexityScore());
        assertEquals(ComplexityLevel.LOW, result.getComplexityLevel());
        assertTrue(result.isWithinLimits());
        assertTrue(result.getComplexityLocations().isEmpty());
    }
}
