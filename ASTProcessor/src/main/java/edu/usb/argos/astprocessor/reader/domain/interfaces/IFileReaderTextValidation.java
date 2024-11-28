package edu.usb.argos.astprocessor.reader.domain.interfaces;

import edu.usb.argos.astprocessor.reader.domain.exceptions.FileReaderException;

public interface IFileReaderTextValidation {
    void validateFileReaderByText(String content) throws FileReaderException;
}
