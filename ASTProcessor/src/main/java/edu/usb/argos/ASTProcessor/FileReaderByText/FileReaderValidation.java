package main.java.edu.usb.argos.ASTProcessor.FileReaderByText;

public class FileReaderValidation implements IFileReaderTextValidation {

    @Override
    public void validateFileReaderByText(String content) throws FileReaderException {
        validateNullInput(content);
        validateEmptyInput(content);
    }

    private void validateNullInput(String content) throws FileReaderException {
        if (content == null) {
            throw new FileReaderException("error to parse null input");
        }
    }

    private void validateEmptyInput(String content) throws FileReaderException {
        if (content.trim().isEmpty()) {
            throw new FileReaderException("error to parse empty input");
        }
    }
}
