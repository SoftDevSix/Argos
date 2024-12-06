package edu.usb.argos.astprocessor.analyzer.infrastructure.utils.entitySingnatureBuilders;

import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeIdentity;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeRange;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.CodeSmellAnalysisByClass;
import edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells.EntityWithSignature;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IEntitySignatureBuilder;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import edu.usb.argos.astprocessor.analyzer.core.interfaces.IShingleGenerator;
import edu.usb.argos.astprocessor.analyzer.infrastructure.utils.algorithms.lsh.MinHashingHandler;
import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.classes.ClassInformation;
import edu.usb.argos.astprocessor.visitor.core.entities.method.MethodInformation;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Optional;

public class MethodSignatureBuilder implements IEntitySignatureBuilder<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>, ClassInformation<JavaParser.StatementContext>> {

    private final MinHashingHandler minHashingHandler;
    private final IShingleGenerator<String> shingleGenerator;
    private final INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer;

    private ClassInformation<JavaParser.StatementContext> currentClass;
    private CodeSmellAnalysisByClass currentAnalysisByClass;

    public MethodSignatureBuilder(MinHashingHandler minHashingHandler, IShingleGenerator<String> shingleGenerator, INormalizer<JavaParser.MethodDeclarationContext> methodNormalizer) {
        this.minHashingHandler = minHashingHandler;
        this.shingleGenerator = shingleGenerator;
        this.methodNormalizer = methodNormalizer;
    }

    @Override
    public List<EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>>> buildMultipleFromResource(ClassInformation<JavaParser.StatementContext> classNode, CodeSmellAnalysisByClass analysisByClassOrigin) {
        currentClass = classNode;
        currentAnalysisByClass = analysisByClassOrigin;

        return classNode.getMembers()
                .getMethods()
                .stream()
                .map(this::extractSignatureForMethod)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>>> extractSignatureForMethod(MethodInformation<JavaParser.StatementContext> method) {
        return method.getStatements().stream()
                .findFirst()
                .flatMap(this::findEnclosingMethod)
                .map(methodNode -> buildSingleFromNodes(methodNode, method));
    }

    private EntityWithSignature<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>> buildSingleFromNodes(
            JavaParser.MethodDeclarationContext methodNode,
            MethodInformation<JavaParser.StatementContext> method) {

        List<String> normalizedTokens = methodNormalizer.normalize(methodNode);
        List<Integer> signature = computeSignature(normalizedTokens);
        CodeIdentity<CodeSmellAnalysisByClass> codeIdentity = buildCodeIdentity(currentClass, methodNode, method);

        return EntityWithSignature.<CodeIdentity<CodeSmellAnalysisByClass>, List<Integer>>builder()
                .signature(signature)
                .entity(codeIdentity)
                .build();
    }

    private List<Integer> computeSignature(List<String> tokens) {
        List<List<String>> shingles = shingleGenerator.generate(tokens);
        return minHashingHandler.computeMinHash(shingleGenerator.flatSingles(shingles));
    }

    private CodeIdentity<CodeSmellAnalysisByClass> buildCodeIdentity(ClassInformation<JavaParser.StatementContext> classNode,
                                           JavaParser.MethodDeclarationContext methodNode,
                                           MethodInformation<JavaParser.StatementContext> method) {
        String methodPath = buildMethodPath(classNode, method.getName());
        CodeRange codeRange = new CodeRange(methodNode.getStart().getLine(), methodNode.getStop().getLine());

        return CodeIdentity.<CodeSmellAnalysisByClass>builder()
                .identifier(methodPath)
                .codeRange(codeRange)
                .origin(currentAnalysisByClass)
                .build();
    }

    private String buildMethodPath(ClassInformation<JavaParser.StatementContext> classNode, String methodName) {
        StringBuilder methodPath = new StringBuilder();

        classNode.getIdentity().getPackageName()
                .ifPresent(packageName -> methodPath.append(packageName).append("/"));
        classNode.getIdentity().getName()
                .ifPresent(className -> methodPath.append(className).append(".java/"));
        methodPath.append(methodName);

        return methodPath.toString();
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
