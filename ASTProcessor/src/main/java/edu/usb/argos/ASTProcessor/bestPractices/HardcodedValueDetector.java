package edu.usb.argos.ASTProcessor.bestPractices;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class HardcodedValueDetector {
    private final HardcodedValueMatcher hardcodedValueMatcher;
    private final List<String> hardcodedValues;
    private final JavaParser.ClassDeclarationContext classContext;

    public HardcodedValueDetector(JavaParser.ClassDeclarationContext classContext) {
        this.classContext = classContext;
        this.hardcodedValueMatcher = HardcodedValueMatcher.getInstance();
        this.hardcodedValues = new ArrayList<>();
    }

    public void detectHardcodedValues() {
        for (JavaParser.ClassBodyDeclarationContext member : classContext.classBody().classBodyDeclaration()) {
            analyzeAttributes(member);
            analyzeConstructors(member);
            analyzeMethods(member);
        }
    }

    public void analyzeAttributes(JavaParser.ClassBodyDeclarationContext member){
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().fieldDeclaration() != null) {
            JavaParser.FieldDeclarationContext field = member.memberDeclaration().fieldDeclaration();
            detectHardcodedValueInAttribute(field, member.modifier());
        }
    }

    private void detectHardcodedValueInAttribute(JavaParser.FieldDeclarationContext field,
                                                  List<JavaParser.ModifierContext> modifiers) {
        Stream<String> mods = modifiers.stream()
                .map(JavaParser.ModifierContext::getText);

        for (JavaParser.VariableDeclaratorContext declarator :
                field.variableDeclarators().variableDeclarator()) {

            if (declarator.variableInitializer() != null) {
                String attributeValue = declarator.variableInitializer().getText();
                if(mods.noneMatch(mod -> mod.equals("final")) &&
                        hardcodedValueMatcher.isHardcoded(attributeValue)){
                    System.out.println("Hardcoded value at line " + declarator.getStart().getLine() + " " + attributeValue);
                    hardcodedValues.add(attributeValue);
                }
            }
        }
    }

    private void analyzeConstructors(JavaParser.ClassBodyDeclarationContext member){
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().constructorDeclaration() != null) {
            JavaParser.ConstructorDeclarationContext constructor =
                    member.memberDeclaration().constructorDeclaration();
            detectHardcodedValuesInConstructors(constructor);
        }
    }

    private void detectHardcodedValuesInConstructors(JavaParser.ConstructorDeclarationContext constructor) {
        if (constructor.block() != null) {
            for (JavaParser.BlockStatementContext stmt : constructor.block().blockStatement()) {
                if (stmt.statement() != null && stmt.statement().expression() != null) {
                    for (JavaParser.ExpressionContext expressionContext : stmt.statement().expression()){
                        var assignmentTree = expressionContext.getChild(2);
                        if(assignmentTree != null){
                            String assignmentValue = assignmentTree.getText();
                            if(
                                    hardcodedValueMatcher.isHardcoded(assignmentValue)
                            ){
                                System.out.println("Hardcoded value at line " + stmt.getStart().getLine() + " " + assignmentValue);
                                hardcodedValues.add(assignmentValue);
                            }
                        }

                        var methodCall = expressionContext.methodCall();

                        if(expressionContext.methodCall() != null && !methodCall.arguments().isEmpty()){
                            String methodArgumentValue =
                                    expressionContext.methodCall().arguments().getChild(1).getText();
                            String[] arguments = methodArgumentValue.split("\\s*,\\s*");

                            for (String argument : arguments) {
                                if(hardcodedValueMatcher.isHardcoded(argument)){
                                    System.out.println("Hardcoded value at line " + stmt.getStart().getLine() + " " + argument);
                                    hardcodedValues.add(argument);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void analyzeMethods(JavaParser.ClassBodyDeclarationContext member) {
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().methodDeclaration() != null) {
            JavaParser.MethodDeclarationContext method =
                    member.memberDeclaration().methodDeclaration();
            detectHardcodedValuesInMethods(method);
        }
    }

    private void detectHardcodedValuesInMethods(JavaParser.MethodDeclarationContext method) {
        if (method.methodBody().block() != null) {
            for (JavaParser.BlockStatementContext stmt :
                    method.methodBody().block().blockStatement()) {

                if(stmt.statement() != null) {
                    for (JavaParser.ExpressionContext expressionContext : stmt.statement().expression()){
                        var methodCall = expressionContext.methodCall();

                        if(expressionContext.methodCall() != null && !methodCall.arguments().isEmpty()) {
                            JavaParser.ArgumentsContext methodArguments = expressionContext.methodCall().arguments();
                            String methodArgumentValue = methodArguments.getChild(1).getText();
                            String[] arguments = methodArgumentValue.split("\\s*,\\s*");

                            for (String argument : arguments) {
                                if(hardcodedValueMatcher.isHardcoded(argument)){
                                    System.out.println("Hardcoded value at line " + stmt.getStart().getLine() + " " + argument);
                                    hardcodedValues.add(argument);
                                }
                            }
                        }
                    }
                }

                if (stmt.localVariableDeclaration() != null) {
                    JavaParser.LocalVariableDeclarationContext varDeclaration =
                            stmt.localVariableDeclaration();

                    for (JavaParser.VariableDeclaratorContext declarator :
                            varDeclaration.variableDeclarators().variableDeclarator()) {
                        if (declarator.variableInitializer() != null) {
                            String variableValue = declarator.variableInitializer().getText();
                            if(getHardcodedValueMatcher().isHardcoded(variableValue)){
                                System.out.println("Hardcoded value at line " + declarator.getStart().getLine() + " " + variableValue);
                                hardcodedValues.add(variableValue);
                            }
                        }
                    }
                }
            }
        }
    }
}
