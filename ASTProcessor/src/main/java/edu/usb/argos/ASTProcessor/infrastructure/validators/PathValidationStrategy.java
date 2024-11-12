package edu.usb.argos.ASTProcessor.infrastructure.validators;

import edu.usb.argos.ASTProcessor.application.exceptions.FileAnalyzerException;
import edu.usb.argos.ASTProcessor.application.validators.IValidationStrategy;

import java.nio.file.Path;

public class PathValidationStrategy implements IValidationStrategy<Path> {
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
            throw new FileAnalyzerException("File path can't be null");
        }
    }

    private void validateFileExtension(Path path) throws FileAnalyzerException {
        if (!path.toString().endsWith(".java")) {
            throw new FileAnalyzerException("File must be a Java source file (.java)");
        }
    }

    private void validateFileExistence(Path path) throws FileAnalyzerException {
        if (!path.toFile().exists()) {
            throw new FileAnalyzerException("File does not exist: " + path);
        }
    }

    private void validateIsFile(Path path) throws FileAnalyzerException {
        if (!path.toFile().isFile()) {
            throw new FileAnalyzerException("Path must point to a file: " + path);
        }
    }

    private void validateReadPermission(Path path) throws FileAnalyzerException {
        if (!path.toFile().canRead()) {
            throw new FileAnalyzerException("File can't be read: " + path);
        }
    }
}
