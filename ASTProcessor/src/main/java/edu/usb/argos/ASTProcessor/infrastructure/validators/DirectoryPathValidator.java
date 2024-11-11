package edu.usb.argos.ASTProcessor.infrastructure.validators;

import edu.usb.argos.ASTProcessor.application.exceptions.NoSuchFileException;
import edu.usb.argos.ASTProcessor.application.validators.IPathValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class DirectoryPathValidator implements IPathValidator {

    @Override
    public boolean isValidPath(Path path) {
        boolean isValidDirectoryPath = true;

        try {
            existsDirectoryPath(path);
            isDirectoryPath(path);
        } catch (NoSuchFileException exception) {
            log.error(exception.getMessage());
            isValidDirectoryPath = false;
        }

        return isValidDirectoryPath;
    }

    private void existsDirectoryPath(Path directoryPath) throws NoSuchFileException {
        if (!Files.exists(directoryPath)) {
            throw new NoSuchFileException("No found directory for: " + directoryPath);
        }
    }

    private void isDirectoryPath(Path directoryPath) throws NoSuchFileException {
        if (!Files.isDirectory(directoryPath)) {
            throw new NoSuchFileException("The path provided is not a directory: " + directoryPath);
        }
    }

}
