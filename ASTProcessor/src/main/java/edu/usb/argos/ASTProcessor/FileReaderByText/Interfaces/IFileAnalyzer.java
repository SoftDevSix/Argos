package main.java.edu.usb.argos.ASTProcessor.FileReaderByText.Interfaces;

import main.java.edu.usb.argos.ASTProcessor.FileReaderByText.FileReaderHandlers.FileReaderException;

public interface IFileAnalyzer<T,K> {
    K readFile(T code) throws FileReaderException;
}
