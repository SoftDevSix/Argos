package edu.usb.argos.ASTProcessor.application.analyzers;

import java.util.List;

public interface IDirectoryAnalyzer<TSourceCode, TAst> {
    List<TAst> analyzeDirectory(TSourceCode sourceCode);
}
