package edu.usb.argos.ASTProcessor.reader.infrastructure.utils;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.NoSuchFileException;
import edu.usb.argos.ASTProcessor.reader.infrastructure.validators.DirectoryPathValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@AllArgsConstructor
public class SourceTreeAnalyzer {

    private final String JAVA_EXTENSION = ".java";
    private final DirectoryPathValidator pathValidator;

    public List<Path> getJavaFiles(Path directoryPath) {
        return getFilesByExtension(directoryPath, JAVA_EXTENSION);
    }

    private List<Path> getFilesByExtension(Path directoryPath, String extension) {
        List<Path> filePaths = new ArrayList<>();
        Stream<Path> stream = null;

        try {
            pathValidator.validatePath(directoryPath);
            stream = Files.walk(directoryPath);
            filePaths = stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.toString().endsWith(extension))
                    .collect(Collectors.toList());
        } catch (NoSuchFileException exception) {
            log.error(exception.getMessage());
        } catch (IOException exception) {
            String errorMessage = "Error getting " + extension + " files in path";
            log.error(errorMessage);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return filePaths;
    }

}