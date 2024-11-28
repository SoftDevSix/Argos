package edu.usb.argos.astprocessor.reader.domain.interfaces;

import java.util.List;

public interface IDirectoryAnalyzer<T, F> {
    List<F> analyzeDirectory(T sourceCode);
}
