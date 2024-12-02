package edu.usb.argos.astprocessor.reader.infraestructure.validation;

import edu.usb.argos.astprocessor.reader.domain.exceptions.FileAnalyzerException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IFileValidationStrategy;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
public class PathValidationStrategy implements IFileValidationStrategy<Path> {
    @Override
    public void validate(Path input) throws FileAnalyzerException {
        validateNullPath(input);
        validateFileExtension(input);
        validateFileExistence(input);
        validateIsFile(input);
        validateReadPermission(input);
    }

    private void validateNullPath(Path path) throws FileAnalyzerException {
        if (path == null) {
            log.error("File path is null");
            throw new FileAnalyzerException("File path can't be null");
        }
    }

    private void validateFileExtension(Path path) throws FileAnalyzerException {
        if (!path.toString().endsWith(".java")) {
            log.error("Invalid file extension for path: {}", path);
            throw new FileAnalyzerException("File must be a Java source file (.java)");
        }
    }

    private void validateFileExistence(Path path) throws FileAnalyzerException {
        if (!path.toFile().exists()) {
            log.error("File does not exist: {}", path);
            throw new FileAnalyzerException("File does not exist: " + path);
        }
    }

    private void validateIsFile(Path path) throws FileAnalyzerException {
        if (!path.toFile().isFile()) {
            log.error("Path is not a file: {}", path);
            throw new FileAnalyzerException("Path must point to a file: " + path);
        }
    }

    private void validateReadPermission(Path path) throws FileAnalyzerException {
        if (!path.toFile().canRead()) {
            log.error("File cannot be read: {}", path);
            throw new FileAnalyzerException("File can't be read: " + path);
        }
    }
}
