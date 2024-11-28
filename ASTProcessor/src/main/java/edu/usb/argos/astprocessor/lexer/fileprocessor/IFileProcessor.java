package edu.usb.argos.astprocessor.lexer.fileprocessor;

import java.io.IOException;

public interface IFileProcessor<T> {
    T getTokensFromFile(String filePath) throws IOException;
}
