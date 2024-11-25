package edu.usb.argos.ASTProcessor.analyzer.core.services;

import edu.usb.argos.ASTProcessor.analyzer.core.entities.handlers.CodeAnalysisReportHandlerByClass;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.ICodeSmellNodeAnalyzer;
import edu.usb.argos.ASTProcessor.analyzer.core.entities.interfaces.IStatementHasher;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NoDuplicatedCodeAnalyzer<S> implements ICodeSmellNodeAnalyzer<MethodInformation<S>> {

    private final IStatementHasher<S> statementHasher;
    private final HashMap<String, List<Statement<S>>> statementMap;
    private Optional<CodeAnalysisReportHandlerByClass> reportHandlerByClass;

    public NoDuplicatedCodeAnalyzer(IStatementHasher<S> statementHasher, HashMap<String, List<Statement<S>>> statementMap) {
        this.statementMap = statementMap;
        this.statementHasher = statementHasher;
    }

    @Override
    public void analyze(MethodInformation<S> method) {
        for (Statement<S> statement : method.getStatements()) {
            String statementHash = statementHasher.hashStatement(statement.getNode());
            handleDuplicateCode(statementHash, statement);
            statementMap.computeIfAbsent(statementHash, k -> new ArrayList<>()).add(statement);
        }
    }

    private void handleDuplicateCode(String statementHash, Statement<S> statement) {
        if (reportHandlerByClass.isPresent() && statementMap.containsKey(statementHash)) {
            boolean isTheFistDuplicate = statementMap.get(statementHash).size() == 1;
//                todo: create method line analyzer
//                todo: found line for the new statement found
            reportHandlerByClass.get().addMethodWithNoDuplicatedCode(1, 1);
            if (isTheFistDuplicate) {
//                todo: found lines for the current statement in the map
                reportHandlerByClass.get().addMethodWithNoDuplicatedCode(1, 1);
            }
        }
    }

    @Override
    public void setCodeAnalyzerReport(CodeAnalysisReportHandlerByClass codeAnalysisReportHandlerByClass) {
        reportHandlerByClass = Optional.of(codeAnalysisReportHandlerByClass);
    }
}
