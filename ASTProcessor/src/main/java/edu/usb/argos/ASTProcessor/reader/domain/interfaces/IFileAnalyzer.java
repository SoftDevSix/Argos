package edu.usb.argos.ASTProcessor.reader.domain.interfaces;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileAnalyzerException;

import java.util.Optional;

public interface IFileAnalyzer<T, F> {
    Optional<F> readFile(T codePath) throws FileAnalyzerException;
}
