package edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.INormalizer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.antlr.JavaLexer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import static edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers.AntlrTokens.IDENTIFIER_TOKEN;
import static edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers.AntlrTokens.LITERAL_TOKEN;
import static edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers.AntlrTokens.LITERAL_TOKENS;

public class AntlrMethodNormalizer implements INormalizer<JavaParser.MethodDeclarationContext> {

    @Override
    public List<String> normalize(JavaParser.MethodDeclarationContext methodContext) {
        Optional<JavaParser.BlockContext> methodBlock = Optional.ofNullable(methodContext.methodBody().block());

        return methodBlock.map(block ->
                block.blockStatement()
                        .stream()
                        .map(this::normalizeBlockStatement)
                        .flatMap(Collection::stream)
                        .toList()
        ).orElse(List.of());
    }

    private List<String> normalizeBlockStatement(ParserRuleContext context) {
        List<String> tokens = new ArrayList<>();
        extractTokens(context, tokens);

        return tokens;
    }

    private void extractTokens(ParseTree tree, List<String> tokens) {
        if (tree instanceof TerminalNode treeNode) {
            Token token = treeNode.getSymbol();
            processToken(token, tokens);
        }

        if (tree instanceof ParserRuleContext) {
            for (int i = 0; i < tree.getChildCount(); i++) {
                extractTokens(tree.getChild(i), tokens);
            }
        }
    }

    private void processToken(Token token, List<String> tokens) {
        String tokenText = token.getText();
        Optional<String> tokenType = Optional.ofNullable(JavaLexer.VOCABULARY.getSymbolicName(token.getType()));

        if (tokenType.isPresent()) {
            processToken(tokenType.get(), tokens);
        } else {
            tokens.add(tokenText);
        }
    }

    private void processToken(String tokenType, List<String> tokens) {
        if (tokenType.equals(IDENTIFIER_TOKEN)) {
            tokens.add(IDENTIFIER_TOKEN);
        } else if (LITERAL_TOKENS.contains(tokenType)) {
            tokens.add(LITERAL_TOKEN);
        } else {
            tokens.add(tokenType);
        }
    }
}
