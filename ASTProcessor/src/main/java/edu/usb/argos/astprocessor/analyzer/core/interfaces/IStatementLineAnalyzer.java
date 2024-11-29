package edu.usb.argos.astprocessor.analyzer.core.interfaces;

public interface IStatementLineAnalyzer<S> {
    int getStatementStartLine(S statement);

    int getStatementEndLine(S statement);
}
