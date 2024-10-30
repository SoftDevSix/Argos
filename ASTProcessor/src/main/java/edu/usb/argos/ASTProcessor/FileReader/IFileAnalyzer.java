package edu.usb.argos.ASTProcessor;

import main.java.edu.usb.argos.ASTProcessor.FileReader.FileAnalyzerException;

public interface IFileAnalyzer<T, K> {
    T read(K file) throws FileAnalyzerException; 
}
