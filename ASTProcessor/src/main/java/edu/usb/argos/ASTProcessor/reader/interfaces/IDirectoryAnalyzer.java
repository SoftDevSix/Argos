package edu.usb.argos.ASTProcessor.reader.interfaces;

import java.util.List;

public interface IDirectoryAnalyzer<TSourceCode, TAst> {
    List<TAst> analyzeDirectory(TSourceCode sourceCode);
}
