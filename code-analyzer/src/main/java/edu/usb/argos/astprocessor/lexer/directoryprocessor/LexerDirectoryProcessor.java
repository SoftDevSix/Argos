package edu.usb.argos.astprocessor.lexer.directoryprocessor;

import edu.usb.argos.astprocessor.lexer.errorhandler.exceptions.LexerFileProcessingException;
import edu.usb.argos.astprocessor.lexer.fileprocessor.IFileProcessor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LexerDirectoryProcessor implements IDirectoryProcessor<CommonTokenStream> {
    private final IFileProcessor<CommonTokenStream> fileProcessor;
    private static final String JAVA_EXTENSION = ".java";
    private static final Logger logger = LoggerFactory.getLogger(LexerDirectoryProcessor.class);

    public LexerDirectoryProcessor(IFileProcessor<CommonTokenStream> fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Override
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
            String errorMessage = "Error processing file: " + path + ", error: " + e.getMessage();
            logger.error(errorMessage);
            throw new LexerFileProcessingException(errorMessage, e);
        }
    }
}
