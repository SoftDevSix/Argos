package edu.usb.argos.astprocessor.analyzer.infrastructure.utils;

import edu.usb.argos.astprocessor.analyzer.core.interfaces.IStatementLineAnalyzer;
import edu.usb.argos.astprocessor.antlr.JavaParser;

public class StatementLineAnalyzer implements IStatementLineAnalyzer<JavaParser.StatementContext> {

    @Override
    public int getStatementStartLine(JavaParser.StatementContext statement) {
        if (statement == null || statement.getStart() == null) {
            return 0;
        }

        return statement.getStart().getLine();
    }

    @Override
    public int getStatementEndLine(JavaParser.StatementContext statement) {
        if (statement == null || statement.getStop() == null) {
            return 0;
        }

        return statement.getStop().getLine();
    }
}
