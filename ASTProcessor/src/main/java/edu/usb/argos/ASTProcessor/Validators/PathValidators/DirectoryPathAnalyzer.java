package edu.usb.argos.ASTProcessor.Validators.PathValidators;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class DirectoryPathAnalyzer implements IPathValidator {

    @Override
    public void validatePath(Path path) throws Exception {
        existsDirectoryPath(path);
        isDirectoryPath(path);
    }

    private void existsDirectoryPath(Path directoryPath) throws NoSuchFileException {
        if (!Files.exists(directoryPath)) {
            throw new NoSuchFileException("No found directory for: " + directoryPath);
        }
    }

    private void isDirectoryPath(Path directoryPath) throws IllegalArgumentException {
        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("The path provided is not a directory: " + directoryPath);
        }
    }

}
