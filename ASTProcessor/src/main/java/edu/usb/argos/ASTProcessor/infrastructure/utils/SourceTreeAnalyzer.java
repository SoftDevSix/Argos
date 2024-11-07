package edu.usb.argos.ASTProcessor.infrastructure.utils;

import edu.usb.argos.ASTProcessor.application.logging.IAppLogger;
import edu.usb.argos.ASTProcessor.infrastructure.validators.DirectoryPathValidator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SourceTreeAnalyzer {

    private final String JAVA_EXTENSION = ".java";
    private final DirectoryPathValidator pathValidator;
    private final IAppLogger logger;

    public SourceTreeAnalyzer(DirectoryPathValidator pathValidator, IAppLogger logger) {
        this.pathValidator = pathValidator;
        this.logger = logger;
    }

    public List<Path> getJavaFiles(Path directoryPath) throws Exception {
        return getFilesByExtension(directoryPath, JAVA_EXTENSION);
    }

    private List<Path> getFilesByExtension(Path directoryPath, String extension) {
        List<Path> filePaths = new ArrayList<>();
        boolean isValidPathDirectory = pathValidator.isValidPath(directoryPath);
        if (!isValidPathDirectory) {
            return filePaths;
        }

        try (Stream<Path> stream = Files.walk(directoryPath)) {
            filePaths = stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.toString().endsWith(extension))
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            String errorMessage = "Error getting " + extension + " files in path";
            logger.error(errorMessage, exception);
        }

        return filePaths;
    }

}
