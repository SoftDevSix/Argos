package edu.usb.argos.astprocessor.reader.infraestructure.validation;

import edu.usb.argos.astprocessor.reader.domain.exceptions.NoSuchFileException;
import edu.usb.argos.astprocessor.reader.domain.interfaces.IPathValidator;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DirectoryPathValidator implements IPathValidator<Path> {

    @Override
    public void validatePath(Path path) throws NoSuchFileException {
        existsDirectoryPath(path);
        isDirectoryPath(path);
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