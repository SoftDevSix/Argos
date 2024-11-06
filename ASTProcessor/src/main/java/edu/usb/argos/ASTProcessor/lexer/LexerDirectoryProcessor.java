package edu.usb.argos.ASTProcessor.lexer;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexerDirectoryProcessor {
    private final LexerFileProcessor fileProcessor;

    public LexerDirectoryProcessor() {
        this.fileProcessor = new LexerFileProcessor();
    }

    public CommonTokenStream getAllTokensFromAllDirectory(String directoryPath) throws IOException {
        List<Token> allTokens = new ArrayList<>();

        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        allTokens.addAll(fileProcessor.getTokensFromFile(path.toString()).getTokens());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return new CommonTokenStream(new ListTokenSource(allTokens));
    }

    public Map<String, CommonTokenStream> getTokensFromDirectoryByFile(String directoryPath) throws IOException {
        Map<String, CommonTokenStream> tokensByFile = new HashMap<>();

        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        CommonTokenStream tokens = fileProcessor.getTokensFromFile(path.toString());
                        tokensByFile.put(path.toString(), tokens);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return tokensByFile;
    }
}