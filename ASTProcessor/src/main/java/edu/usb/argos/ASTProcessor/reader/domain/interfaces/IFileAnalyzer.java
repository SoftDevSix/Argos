package edu.usb.argos.ASTProcessor.reader.domain.interfaces;

import edu.usb.argos.ASTProcessor.reader.domain.exceptions.FileReaderException;

public interface IFileAnalyzer<T, K> {
    K readFile(T code) throws FileReaderException;
}
