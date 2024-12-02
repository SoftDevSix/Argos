package edu.usb.argos.astprocessor.visitor.core.services.method;

import edu.usb.argos.astprocessor.antlr.JavaParser;
import edu.usb.argos.astprocessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.astprocessor.visitor.core.interfaces.services.IParameterExtractor;
import lombok.Generated;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
@Generated
public class ParameterService implements
        IParameterExtractor<JavaParser.FormalParameterContext, JavaParser.LastFormalParameterContext> {

    @Override
    public Optional<ParameterInformation> createRegularParameter(JavaParser.FormalParameterContext param) {
        if (param == null) {
            return Optional.empty();
        }

        Optional<String> name = extractParameterName(param);
        Optional<String> type = extractParameterType(param);

        if (name.isEmpty() || type.isEmpty()) {
            return Optional.empty();
        }

        ParameterInformation paramInfo = ParameterInformation.builder()
                .name(name.get())
                .type(type.get())
                .modifiers(extractVariableModifiers(param))
                .isVarArgs(false)
                .build();
        return Optional.of(paramInfo);
    }

    @Override
    public Optional<ParameterInformation> createVarArgsParameter(JavaParser.LastFormalParameterContext param) {
        if (param == null) {
            return Optional.empty();
        }

        Optional<String> name = extractVarArgsName(param);
        Optional<String> type = extractVarArgsType(param);

        if (name.isEmpty() || type.isEmpty()) {
            return Optional.empty();
        }

        ParameterInformation paramInfo = ParameterInformation.builder()
                .name(name.get())
                .type(type.get())
                .modifiers(extractVarArgsModifiers(param))
                .isVarArgs(true)
                .build();
        return Optional.of(paramInfo);
    }

    private Optional<String> extractParameterName(JavaParser.FormalParameterContext param) {
        if (param.variableDeclaratorId() == null) {
            return Optional.empty();
        }
        return Optional.of(param.variableDeclaratorId().getText());
    }

    private Optional<String> extractParameterType(JavaParser.FormalParameterContext param) {
        if (param.typeType() == null) {
            return Optional.empty();
        }
        return Optional.of(param.typeType().getText());
    }

    private List<String> extractVariableModifiers(JavaParser.FormalParameterContext param) {
        if (param == null || param.variableModifier() == null) {
            return Collections.emptyList();
        }
        return extractModifiersFromList(param.variableModifier());
    }

    private Optional<String> extractVarArgsName(JavaParser.LastFormalParameterContext param) {
        if (param.variableDeclaratorId() == null) {
            return Optional.empty();
        }
        return Optional.of(param.variableDeclaratorId().getText());
    }

    private Optional<String> extractVarArgsType(JavaParser.LastFormalParameterContext param) {
        if (param.typeType() == null) {
            return Optional.empty();
        }
        return Optional.of(param.typeType().getText());
    }

    private List<String> extractVarArgsModifiers(JavaParser.LastFormalParameterContext param) {
        if (param == null || param.variableModifier() == null) {
            return Collections.emptyList();
        }
        return extractModifiersFromList(param.variableModifier());
    }

    private List<String> extractModifiersFromList(List<JavaParser.VariableModifierContext> modifiers) {
        List<String> extractedModifiers = new ArrayList<>();
        for (JavaParser.VariableModifierContext mod : modifiers) {
            extractedModifiers.add(mod.getText());
        }
        return Collections.unmodifiableList(extractedModifiers);
    }
}
