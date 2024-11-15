package edu.usb.argos.ASTProcessor;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

    public static void main(String[] args) {

        CharStream input = CharStreams.fromString("""
                class Person {
                    private String name;
                    private int age:
                
                    public Person (String name, int age) {
                        this.name = name;
                        this.age = age;
                
                        for (int i = 0; i < 12; i++) {
                            if (age == i) {
                                this.age = 90;
                                this.name = "invalid";
                            }
                        }
                    }
                
                    public void findOnDatabase() {
                        // todo: look for person data in database
                        String databaseName = "somename";
                        int databaseAge = 123;
                        name = databaseName;
                        age = databaseAge;
                    }
                
                    public Person (String name) {
                        this.name = name;
                    }
                
                    public Person () {
                        findOnDatabase();
                    }
                }
                """);

        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);

        JavaParser.CompilationUnitContext compilationUnitContext = parser.compilationUnit();

        if (!compilationUnitContext.typeDeclaration().isEmpty()) {
            JavaParser.TypeDeclarationContext typeDecl = compilationUnitContext.typeDeclaration().get(0);
            if (typeDecl.classDeclaration() != null) {
                JavaParser.ClassBodyContext classBodyContext = typeDecl.classDeclaration().classBody();
                new JavaConstructorVisitor().visitConstructors(classBodyContext)
                        .forEach(c -> {
                            System.out.println(c);
                            c.getBodyStatements().forEach(b -> System.out.println(b.getText()));
                            System.out.println();
                        });
            }
        }
    }

}
