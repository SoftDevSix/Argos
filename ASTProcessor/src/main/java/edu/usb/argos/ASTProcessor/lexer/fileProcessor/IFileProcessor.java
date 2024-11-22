package edu.usb.argos.ASTProcessor.lexer.fileProcessor;

import java.io.IOException;

public interface IFileProcessor<T> {
    T getTokensFromFile(String filePath) throws IOException;
}
