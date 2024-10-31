package edu.usb.argos.ASTProcessor.reader.interfaces;

import java.nio.file.Path;

public interface IPathValidator {

    public void validatePath(Path path) throws Exception;

}
