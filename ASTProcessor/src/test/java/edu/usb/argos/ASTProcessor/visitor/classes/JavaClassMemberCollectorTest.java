package edu.usb.argos.ASTProcessor.visitor.classes;

import edu.usb.argos.ASTProcessor.visitor.core.entities.classes.ConstructorInfo;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.AttributeInfo;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.MethodInfo;
import edu.usb.argos.ASTProcessor.visitor.core.services.collectors.classes.JavaClassMemberCollector;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.classes.JavaConstructorVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaAttributeVisitor;
import edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.visitors.method.JavaMethodVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.antlr.v4.runtime.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import edu.usb.argos.ASTProcessor.antlr.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JavaClassMemberCollectorTest {
    @Mock
    private JavaMethodVisitor methodVisitor;
    @Mock
    private JavaAttributeVisitor attributeVisitor;
    @Mock
    private JavaConstructorVisitor constructorVisitor;

    private JavaClassMemberCollector memberCollector;
    private JavaParser.CompilationUnitContext compilationUnit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        String testClass =
                "public class TestClass {\n" +
                        "    private String field1;\n" +
                        "    public Integer field2;\n" +
                        "    public TestClass() {}\n" +
                        "    public TestClass(String field1) { this.field1 = field1; }\n" +
                        "    public void method1() {}\n" +
                        "    private String method2() { return \"\"; }\n" +
                        "}";

        CharStream input = CharStreams.fromString(testClass);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        compilationUnit = parser.compilationUnit();

        memberCollector = new JavaClassMemberCollector(methodVisitor, attributeVisitor, constructorVisitor);
    }

    @Test
    void getClassMethods_ShouldReturnCorrectMethods() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        MethodInfo method1 = new MethodInfo(
                "method1", "void", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);
        MethodInfo method2 = new MethodInfo(
                "method2", "String", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);

        when(methodVisitor.visitMethod(any())).thenReturn(method1).thenReturn(method2);

        List<MethodInfo> methods = memberCollector.getClassMethods(ctx);

        assertEquals(2, methods.size());
        assertEquals("method1", methods.get(0).getName());
        assertEquals("method2", methods.get(1).getName());
    }

    @Test
    void getClassAttributes_ShouldReturnCorrectAttributes() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        JavaParser.ClassBodyContext bodyCtx = ctx.classBody();

        List<AttributeInfo> expectedAttributes = Arrays.asList(
                new AttributeInfo("field1", "String", Arrays.asList("private")),
                new AttributeInfo("field2", "Integer", Arrays.asList("public"))
        );

        when(attributeVisitor.visitClassBody(bodyCtx)).thenReturn(expectedAttributes);

        List<AttributeInfo> attributes = memberCollector.getClassAttributes(ctx);

        assertEquals(2, attributes.size());
        assertEquals("field1", attributes.get(0).getName());
        assertEquals("field2", attributes.get(1).getName());
    }

    @Test
    void getClassMethods_ShouldReturnCorrectMethods_withModifiers() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        MethodInfo method1 = new MethodInfo(
                "method1", "void", Arrays.asList("public"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);
        MethodInfo method2 = new MethodInfo(
                "method2", "String", Arrays.asList("private"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);

        when(methodVisitor.visitMethod(any())).thenReturn(method1).thenReturn(method2);

        List<MethodInfo> methods = memberCollector.getClassMethods(ctx);

        assertEquals(2, methods.size());

        assertEquals("method1", methods.get(0).getName());
        assertEquals("void", methods.get(0).getReturnType());
        assertEquals(Arrays.asList("public"), methods.get(0).getModifiers());

        assertEquals("method2", methods.get(1).getName());
        assertEquals("String", methods.get(1).getReturnType());
        assertEquals(Arrays.asList("private"), methods.get(1).getModifiers());
    }

    @Test
    void getClassAttributes_ShouldReturnCorrectAttributes_withModifiers() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();
        JavaParser.ClassBodyContext bodyCtx = ctx.classBody();

        List<AttributeInfo> expectedAttributes = Arrays.asList(
                new AttributeInfo("field1", "String", Arrays.asList("private", "final")),
                new AttributeInfo("field2", "Integer", Arrays.asList("public"))
        );

        when(attributeVisitor.visitClassBody(bodyCtx)).thenReturn(expectedAttributes);

        List<AttributeInfo> attributes = memberCollector.getClassAttributes(ctx);

        assertEquals(2, attributes.size());

        assertEquals("field1", attributes.get(0).getName());
        assertEquals("String", attributes.get(0).getType());
        assertEquals(Arrays.asList("private", "final"), attributes.get(0).getModifiers());

        assertEquals("field2", attributes.get(1).getName());
        assertEquals("Integer", attributes.get(1).getType());
        assertEquals(Arrays.asList("public"), attributes.get(1).getModifiers());
    }

    @Test
    void getClassConstructors_ShouldReturnCorrectConstructors() {
        JavaParser.ClassDeclarationContext ctx = compilationUnit.typeDeclaration(0).classDeclaration();

        ConstructorInfo constructor1 = new ConstructorInfo("TestClass", Arrays.asList("public"), new ArrayList<>());
        ConstructorInfo constructor2 = new ConstructorInfo("TestClass", Arrays.asList("public"), Arrays.asList("String"));

        when(constructorVisitor.visitConstructors(any())).thenReturn(Arrays.asList(constructor1, constructor2));

        List<ConstructorInfo> constructors = memberCollector.getClassConstructors(ctx);

        assertEquals(2, constructors.size());

        assertEquals("TestClass", constructors.get(0).getName());
        assertEquals(Arrays.asList("public"), constructors.get(0).getModifiers());
        assertTrue(constructors.get(0).getParameters().isEmpty());

        assertEquals("TestClass", constructors.get(1).getName());
        assertEquals(Arrays.asList("public"), constructors.get(1).getModifiers());
        assertEquals(Arrays.asList("String"), constructors.get(1).getParameters());
    }
}

