package edu.usb.argos.ASTProcessor.lexer;

import org.antlr.v4.runtime.CommonTokenStream;
import edu.usb.argos.ASTProcessor.lexer.errorHandler.exceptions.LexerProcessingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LexerDirectoryProcessor {
    private final LexerFileProcessor fileProcessor;
    private static final String JAVA_EXTENSION = ".java";

    public LexerDirectoryProcessor() {
        this.fileProcessor = new LexerFileProcessor();
    }

    public Map<String, CommonTokenStream> getTokensFromDirectoryByFile(String directoryPath) throws IOException {
        Map<String, CommonTokenStream> tokensByFile = new HashMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(JAVA_EXTENSION))
                    .forEach(path -> processFile(path, tokensByFile));
        }

        return tokensByFile;
    }

    private void processFile(Path path, Map<String, CommonTokenStream> tokensByFile) {
        try {
            CommonTokenStream tokens = fileProcessor.getTokensFromFile(path.toString());
            tokensByFile.put(path.toString(), tokens);
        } catch (IOException e) {
            throw new LexerProcessingException("Error processing file: " + path, e);
        }
    }
}
