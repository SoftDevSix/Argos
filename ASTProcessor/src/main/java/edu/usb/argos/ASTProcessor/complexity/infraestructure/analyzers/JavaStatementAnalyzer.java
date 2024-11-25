package edu.usb.argos.ASTProcessor.complexity.infraestructure.analyzers;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.complexity.core.entities.ComplexityLocation;
import edu.usb.argos.ASTProcessor.complexity.core.enums.ComplexityType;
import edu.usb.argos.ASTProcessor.complexity.core.interfaces.analyzers.NodeAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class JavaStatementAnalyzer implements NodeAnalyzer<JavaParser.StatementContext> {
    @Override
    public int analyzeNode(JavaParser.StatementContext node) {
        if (node == null) return 0;

        int complexity = 0;

        int controlComplexity = analyzeControlStructures(node);
        complexity += controlComplexity;

        int nestedComplexity = analyzeNestedStatements(node);
        complexity += nestedComplexity;

        int logicalComplexity = analyzeLogicalOperators(node);
        complexity += logicalComplexity;

        return complexity;
    }

    private int analyzeControlStructures(JavaParser.StatementContext node) {
        int complexity = 0;

        if (node.IF() != null) {
            complexity++;

            if (node.ELSE() != null && node.statement(1) != null && node.statement(1).IF() != null) {
                complexity++;
            }
        }

        if (node.FOR() != null) {
            complexity++;
        }
        if (node.WHILE() != null && node.DO() == null) {
            complexity++;
        }
        if (node.DO() != null) {
            complexity++;
        }

        if (node.SWITCH() != null) {
            int switchComplexity = (int) node.switchBlockStatementGroup().stream()
                    .flatMap(group -> group.switchLabel().stream())
                    .filter(label -> label.CASE() != null)
                    .count();
            complexity += switchComplexity;
        }

        if (node.TRY() != null) {
            int catches = node.catchClause().size();
            complexity += catches;
        }

        return complexity;
    }

    private int analyzeNestedStatements(JavaParser.StatementContext node) {
        int complexity = 0;

        if (node.block() != null) {
            for (JavaParser.BlockStatementContext blockStmt : node.block().blockStatement()) {
                if (blockStmt.statement() != null) {
                    int blockComplexity = analyzeNode(blockStmt.statement());
                    complexity += blockComplexity;
                }
            }
        }

        if (node.SWITCH() != null) {
            for (JavaParser.SwitchBlockStatementGroupContext group : node.switchBlockStatementGroup()) {
                for (JavaParser.BlockStatementContext blockStmt : group.blockStatement()) {
                    if (blockStmt.statement() != null) {
                        int switchBlockComplexity = analyzeNode(blockStmt.statement());
                        complexity += switchBlockComplexity;
                    }
                }
            }
        }

        for (JavaParser.StatementContext stmt : node.statement()) {
            if (!(node.ELSE() != null && stmt.IF() != null)) {
                int stmtComplexity = analyzeNode(stmt);
                complexity += stmtComplexity;
            }
        }

        return complexity;
    }

    private int analyzeLogicalOperators(JavaParser.StatementContext node) {
        int complexity = 0;

        if (node.parExpression() != null && node.parExpression().expression() != null) {
            String conditionText = node.parExpression().expression().getText();
            int parComplexity = countLogicalOperators(conditionText);
            complexity += parComplexity;
        }

        if (node.DO() != null && node.parExpression() != null) {
            String doWhileCondition = node.parExpression().expression().getText();
            int doWhileComplexity = countLogicalOperators(doWhileCondition);
            complexity += doWhileComplexity;
        }

        if (node.expression() != null) {
            for (JavaParser.ExpressionContext expr : node.expression()) {
                String exprText = expr.getText();
                int exprComplexity = countLogicalOperators(exprText);
                complexity += exprComplexity;
            }
        }

        return complexity;
    }

    private int countLogicalOperators(String text) {
        int count = 0;

        int andCount = text.split("&&").length - 1;
        count += andCount;

        int orCount = text.split("\\|\\|").length - 1;
        count += orCount;

        return count;
    }

    @Override
    public List<ComplexityLocation> getComplexityLocation(JavaParser.StatementContext node) {
        List<ComplexityLocation> locations = new ArrayList<>();
        if (node == null) return locations;

        collectControlStructureLocations(node, locations);
        collectLogicalOperatorLocations(node, locations);
        collectNestedLocations(node, locations);

        return locations;
    }

    private void collectLogicalOperatorLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.parExpression() != null && node.parExpression().expression() != null) {
            collectOperatorsFromExpression(node.parExpression().expression(),
                    node.parExpression().getStart().getLine(),
                    locations);
        }

        if (node.DO() != null && node.parExpression() != null) {
            collectOperatorsFromExpression(node.parExpression().expression(),
                    node.parExpression().getStart().getLine(),
                    locations);
        }

        if (node.expression() != null) {
            for (JavaParser.ExpressionContext expr : node.expression()) {
                collectOperatorsFromExpression(expr, expr.getStart().getLine(), locations);
            }
        }
    }

    private void collectOperatorsFromExpression(JavaParser.ExpressionContext expr, int lineNumber,
                                                List<ComplexityLocation> locations) {
        String text = expr.getText();

        int lastIndex = 0;
        while ((lastIndex = text.indexOf("&&", lastIndex)) != -1) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(lineNumber)
                    .complexityType(ComplexityType.LOGICAL_AND)
                    .description("Logical AND operator")
                    .contextInfo("in expression: " + text)
                    .build());
            lastIndex += 2;
        }

        lastIndex = 0;
        while ((lastIndex = text.indexOf("||", lastIndex)) != -1) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(lineNumber)
                    .complexityType(ComplexityType.LOGICAL_OR)
                    .description("Logical OR operator")
                    .contextInfo("in expression: " + text)
                    .build());
            lastIndex += 2;
        }
    }

    private void collectNestedLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.block() != null) {
            for (JavaParser.BlockStatementContext blockStmt : node.block().blockStatement()) {
                if (blockStmt.statement() != null) {
                    locations.addAll(getComplexityLocation(blockStmt.statement()));
                }
            }
        }

        if (node.SWITCH() != null) {
            for (JavaParser.SwitchBlockStatementGroupContext group : node.switchBlockStatementGroup()) {
                for (JavaParser.BlockStatementContext blockStmt : group.blockStatement()) {
                    if (blockStmt.statement() != null) {
                        locations.addAll(getComplexityLocation(blockStmt.statement()));
                    }
                }
            }
        }

        for (JavaParser.StatementContext stmt : node.statement()) {
            if (!(node.ELSE() != null && stmt.IF() != null)) {
                locations.addAll(getComplexityLocation(stmt));
            }
        }
    }

    private void collectControlStructureLocations(JavaParser.StatementContext node, List<ComplexityLocation> locations) {
        if (node.IF() != null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.IF_STATEMENT)
                    .description("Conditional branch")
                    .contextInfo("if condition: " + node.parExpression().getText())
                    .build());

            if (node.ELSE() != null && node.statement(1) != null && node.statement(1).IF() != null) {
                locations.add(ComplexityLocation.builder()
                        .lineNumber(node.statement(1).getStart().getLine())
                        .complexityType(ComplexityType.IF_STATEMENT)
                        .description("Else-if branch")
                        .contextInfo("else-if condition: " + node.statement(1).parExpression().getText())
                        .build());
            }
        }

        if (node.FOR() != null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.LOOP)
                    .description("For loop")
                    .contextInfo("for loop with control: " + node.forControl().getText())
                    .build());
        }

        if (node.WHILE() != null && node.DO() == null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.LOOP)
                    .description("While loop")
                    .contextInfo("while condition: " + node.parExpression().getText())
                    .build());
        }

        if (node.DO() != null) {
            locations.add(ComplexityLocation.builder()
                    .lineNumber(node.getStart().getLine())
                    .complexityType(ComplexityType.LOOP)
                    .description("Do-while loop")
                    .contextInfo("do-while condition: " + node.parExpression().getText())
                    .build());
        }

        if (node.SWITCH() != null) {
            for (JavaParser.SwitchBlockStatementGroupContext group : node.switchBlockStatementGroup()) {
                for (JavaParser.SwitchLabelContext label : group.switchLabel()) {
                    if (label.CASE() != null) {
                        locations.add(ComplexityLocation.builder()
                                .lineNumber(label.getStart().getLine())
                                .complexityType(ComplexityType.SWITCH_CASE)
                                .description("Switch case")
                                .contextInfo("case: " + label.getText())
                                .build());
                    }
                }
            }
        }

        if (node.TRY() != null) {
            for (JavaParser.CatchClauseContext catchClause : node.catchClause()) {
                locations.add(ComplexityLocation.builder()
                        .lineNumber(catchClause.getStart().getLine())
                        .complexityType(ComplexityType.CATCH_BLOCK)
                        .description("Exception handling")
                        .contextInfo("catch block for: " + catchClause.catchType().getText())
                        .build());
            }
        }
    }
}
