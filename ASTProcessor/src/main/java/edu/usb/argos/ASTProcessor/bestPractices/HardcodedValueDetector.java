package edu.usb.argos.ASTProcessor.bestPractices;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HardcodedValueDetector {
    private final HardcodedValueMatcher hardcodedValueMatcher;
    private final List<String> hardcodedValues;
    private JavaParser.ClassDeclarationContext classContext;

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
        String type = field.typeType().getText();

        String mods = modifiers.stream()
                .map(JavaParser.ModifierContext::getText)
                .collect(Collectors.joining(" "));

        for (JavaParser.VariableDeclaratorContext declarator :
                field.variableDeclarators().variableDeclarator()) {

            System.out.println("Field:");
            System.out.println("  Modifiers: " + mods);
            System.out.println("  Type: " + type);
            System.out.println("  Name: " + declarator.variableDeclaratorId().getText());

            if (declarator.variableInitializer() != null) {
                System.out.println("  Initial Value: " +
                        declarator.variableInitializer().getText());
            }
            System.out.println("  Line: " + declarator.getStart().getLine());
            System.out.println();
        }
    }

    private void analyzeConstructors(JavaParser.ClassBodyDeclarationContext member){
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().constructorDeclaration() != null) {

            System.out.println("\nConstructor Analysis:");
            JavaParser.ConstructorDeclarationContext constructor =
                    member.memberDeclaration().constructorDeclaration();
            detectHardcodedValuesInConstructors(constructor);
        }
    }

    private void detectHardcodedValuesInConstructors(JavaParser.ConstructorDeclarationContext constructor) {
        System.out.println("Constructor: " + constructor.identifier().getText());

        if (constructor.block() != null) {
            for (JavaParser.BlockStatementContext stmt : constructor.block().blockStatement()) {
                if (stmt.statement() != null && stmt.statement().expression() != null) {
                    System.out.println("  Assignment: " + stmt.statement().getText());
                    System.out.println("  Line: " + stmt.getStart().getLine());
                }
            }
        }
    }

    private void analyzeMethods(JavaParser.ClassBodyDeclarationContext member) {
        if (member.memberDeclaration() != null &&
                member.memberDeclaration().methodDeclaration() != null) {

            System.out.println("\nMethod Analysis:");
            JavaParser.MethodDeclarationContext method =
                    member.memberDeclaration().methodDeclaration();
            detectHardcodedValuesInMethods(method);
        }
    }

    private void detectHardcodedValuesInMethods(JavaParser.MethodDeclarationContext method) {
        System.out.println("Method: " + method.identifier().getText());
        System.out.println("Return Type: " + method.typeTypeOrVoid().getText());

        if (method.methodBody().block() != null) {
            for (JavaParser.BlockStatementContext stmt :
                    method.methodBody().block().blockStatement()) {

                if (stmt.localVariableDeclaration() != null) {
                    JavaParser.LocalVariableDeclarationContext varDecl =
                            stmt.localVariableDeclaration();

                    System.out.println("  Local Variable:");
                    System.out.println("    Type: " + varDecl.typeType().getText());

                    for (JavaParser.VariableDeclaratorContext declarator :
                            varDecl.variableDeclarators().variableDeclarator()) {

                        System.out.println("    Name: " +
                                declarator.variableDeclaratorId().getText());

                        if (declarator.variableInitializer() != null) {
                            System.out.println("    Initial Value: " +
                                    declarator.variableInitializer().getText());
                        }
                        System.out.println("    Line: " + declarator.getStart().getLine());
                    }
                }

                if (stmt.statement() != null && stmt.statement().expression() != null) {
                    System.out.println("  Expression: " +
                            stmt.statement().expression().getClass().getSimpleName());
                    System.out.println("  Line: " + stmt.getStart().getLine());
                }
            }
        }
    }
}
