package main.java.edu.usb.argos.ASTProcessor.FileReaderByText.FileReaderHandlers;

public class FileReaderException extends RuntimeException {
    public FileReaderException(String message) {
        super(message);
    }

    public FileReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
