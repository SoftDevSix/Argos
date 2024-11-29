package edu.usb.argos.astprocessor.core.services.method;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.astprocessor.visitor.core.services.method.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ParameterServiceTest {

    private ParameterService parameterService;

    @Mock
    private JavaParser.FormalParameterContext formalParamCtx;
    @Mock
    private JavaParser.LastFormalParameterContext lastFormalParamCtx;
    @Mock
    private JavaParser.VariableDeclaratorIdContext varDeclId;
    @Mock
    private JavaParser.TypeTypeContext typeType;
    @Mock
    private JavaParser.VariableModifierContext varModifier1;
    @Mock
    private JavaParser.VariableModifierContext varModifier2;

    @BeforeEach
    void setUp() {
        parameterService = new ParameterService();
    }

    @Test
    void createRegularParameterValidParamReturnsParameterInfo() {
        when(formalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(formalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("paramName");
        when(typeType.getText()).thenReturn("String");
        when(formalParamCtx.variableModifier()).thenReturn(Arrays.asList(varModifier1));
        when(varModifier1.getText()).thenReturn("final");

        Optional<ParameterInformation> result = parameterService.createRegularParameter(formalParamCtx);

        assertTrue(result.isPresent());
        assertEquals("paramName", result.get().getName());
        assertEquals("String", result.get().getType());
        assertTrue(result.get().getModifiers().contains("final"));
        assertFalse(result.get().isVarArgs());
    }

    @Test
    void createRegularParameterNullVariableDeclaratorIdReturnsEmpty() {
        when(formalParamCtx.variableDeclaratorId()).thenReturn(null);
        when(formalParamCtx.typeType()).thenReturn(typeType);
        when(typeType.getText()).thenReturn("String");

        Optional<ParameterInformation> result = parameterService.createRegularParameter(formalParamCtx);

        assertTrue(result.isEmpty());
    }

    @Test
    void createRegularParameterNullTypeTypeReturnsEmpty() {
        when(formalParamCtx.typeType()).thenReturn(null);
        when(formalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(varDeclId.getText()).thenReturn("param");

        Optional<ParameterInformation> result = parameterService.createRegularParameter(formalParamCtx);

        assertTrue(result.isEmpty());
    }

    @Test
    void createVarArgsParameterNullParamReturnsEmpty() {
        Optional<ParameterInformation> result = parameterService.createVarArgsParameter(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void createVarArgsParameterValidParamReturnsParameterInfo() {
        when(lastFormalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(lastFormalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("args");
        when(typeType.getText()).thenReturn("String");
        List<JavaParser.VariableModifierContext> modifiers = Arrays.asList(varModifier1, varModifier2);
        when(lastFormalParamCtx.variableModifier()).thenReturn(modifiers);
        when(varModifier1.getText()).thenReturn("final");
        when(varModifier2.getText()).thenReturn("@NotNull");

        Optional<ParameterInformation> result = parameterService.createVarArgsParameter(lastFormalParamCtx);

        assertTrue(result.isPresent());
        assertEquals("args", result.get().getName());
        assertEquals("String", result.get().getType());
        assertTrue(result.get().isVarArgs());
        assertTrue(result.get().getModifiers().contains("final"));
        assertTrue(result.get().getModifiers().contains("@NotNull"));
    }

    @Test
    void createVarArgsParameterNullVariableDeclaratorIdReturnsEmpty() {
        when(lastFormalParamCtx.variableDeclaratorId()).thenReturn(null);
        when(lastFormalParamCtx.typeType()).thenReturn(typeType);
        when(typeType.getText()).thenReturn("String");

        Optional<ParameterInformation> result = parameterService.createVarArgsParameter(lastFormalParamCtx);

        assertTrue(result.isEmpty());
    }


    @Test
    void createVarArgsParameterNullTypeTypeReturnsEmpty() {
        when(lastFormalParamCtx.typeType()).thenReturn(null);
        when(lastFormalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(varDeclId.getText()).thenReturn("param");

        Optional<ParameterInformation> result = parameterService.createVarArgsParameter(lastFormalParamCtx);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateRegularParameterNullParamNoModifiers() {
        Optional<ParameterInformation> result = parameterService.createRegularParameter(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateRegularParameterNullModifierList() {
        when(formalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(formalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("param");
        when(typeType.getText()).thenReturn("String");
        when(formalParamCtx.variableModifier()).thenReturn(null);

        Optional<ParameterInformation> result = parameterService.createRegularParameter(formalParamCtx);

        assertTrue(result.isPresent());
        assertTrue(result.get().getModifiers().isEmpty());
    }

    @Test
    void testCreateVarArgsParameterMultipleModifiers() {
        when(lastFormalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(lastFormalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("args");
        when(typeType.getText()).thenReturn("String");

        List<JavaParser.VariableModifierContext> modifierContexts = Arrays.asList(varModifier1, varModifier2);
        when(lastFormalParamCtx.variableModifier()).thenReturn(modifierContexts);
        when(varModifier1.getText()).thenReturn("final");
        when(varModifier2.getText()).thenReturn("@NotNull");

        Optional<ParameterInformation> result = parameterService.createVarArgsParameter(lastFormalParamCtx);

        assertTrue(result.isPresent());
        List<String> modifiers = result.get().getModifiers();
        assertEquals(2, modifiers.size());
        assertTrue(modifiers.contains("final"));
        assertTrue(modifiers.contains("@NotNull"));
    }

    @Test
    void testCreateRegularParameterEmptyModifierList() {
        when(formalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(formalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("param");
        when(typeType.getText()).thenReturn("String");
        when(formalParamCtx.variableModifier()).thenReturn(Collections.emptyList());

        Optional<ParameterInformation> result = parameterService.createRegularParameter(formalParamCtx);

        assertTrue(result.isPresent());
        assertTrue(result.get().getModifiers().isEmpty());
    }

    @Test
    void testExtractVariableModifiersWithNullModifierButValidParam() {
        when(formalParamCtx.variableModifier()).thenReturn(null);
        when(formalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(formalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("param");
        when(typeType.getText()).thenReturn("String");

        Optional<ParameterInformation> result = parameterService.createRegularParameter(formalParamCtx);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getModifiers());
        assertTrue(result.get().getModifiers().isEmpty());
    }

    @Test
    void testExtractVarArgsModifiersWithNullModifierButValidParam() {
        when(lastFormalParamCtx.variableModifier()).thenReturn(null);
        when(lastFormalParamCtx.variableDeclaratorId()).thenReturn(varDeclId);
        when(lastFormalParamCtx.typeType()).thenReturn(typeType);
        when(varDeclId.getText()).thenReturn("args");
        when(typeType.getText()).thenReturn("String");

        Optional<ParameterInformation> result = parameterService.createVarArgsParameter(lastFormalParamCtx);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getModifiers());
        assertTrue(result.get().getModifiers().isEmpty());
    }
}
