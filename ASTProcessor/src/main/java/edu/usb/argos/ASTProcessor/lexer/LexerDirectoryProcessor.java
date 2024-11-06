package edu.usb.argos.ASTProcessor.lexer;

import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class LexerDirectoryProcessor {
    private final LexerFileProcessor fileProcessor;

    public LexerDirectoryProcessor() {
        this.fileProcessor = new LexerFileProcessor();
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