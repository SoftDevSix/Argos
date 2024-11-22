package edu.usb.argos.astprocessor.lexer.directoryprocessor;

import java.io.IOException;
import java.util.Map;

public interface IDirectoryProcessor<T> {
    Map<String, T> getTokensFromDirectoryByFile(String directoryPath) throws IOException;
}
