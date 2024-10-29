package edu.usb.argos.ASTProcessor.Validators.PathValidators;

import java.nio.file.Path;

public interface IPathValidator {

    public void validatePath(Path path) throws Exception;

}
