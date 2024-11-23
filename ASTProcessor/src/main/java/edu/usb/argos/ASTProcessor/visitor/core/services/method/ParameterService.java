package edu.usb.argos.ASTProcessor.visitor.core.services.method;

import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;
import edu.usb.argos.ASTProcessor.visitor.core.interfaces.services.IParameterExtractor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
public class ParameterService implements
        IParameterExtractor<JavaParser.FormalParameterContext, JavaParser.LastFormalParameterContext> {

    @Override
    public ParameterInformation createRegularParameter(JavaParser.FormalParameterContext param) {
        return ParameterInformation.builder()
                .name(extractParameterName(param))
                .type(extractParameterType(param))
                .modifiers(extractVariableModifiers(param))
                .isVarArgs(false)
                .build();
    }

    @Override
    public ParameterInformation createVarArgsParameter(JavaParser.LastFormalParameterContext param) {
        return ParameterInformation.builder()
                .name(extractVarArgsName(param))
                .type(extractVarArgsType(param))
                .modifiers(extractVarArgsModifiers(param))
                .isVarArgs(true)
                .build();
    }

    private String extractParameterName(JavaParser.FormalParameterContext param) {
        return Optional.ofNullable(param)
                .map(JavaParser.FormalParameterContext::variableDeclaratorId)
                .map(JavaParser.VariableDeclaratorIdContext::getText)
                .orElse("");
    }

    private String extractParameterType(JavaParser.FormalParameterContext param) {
        return Optional.ofNullable(param)
                .map(JavaParser.FormalParameterContext::typeType)
                .map(JavaParser.TypeTypeContext::getText)
                .orElse("");
    }

    private List<String> extractVariableModifiers(JavaParser.FormalParameterContext param) {
        List<String> modifiers = new ArrayList<>();
        if (param != null && param.variableModifier() != null) {
            for (JavaParser.VariableModifierContext mod : param.variableModifier()) {
                modifiers.add(mod.getText());
            }
        }
        return Collections.unmodifiableList(modifiers);
    }

    private String extractVarArgsName(JavaParser.LastFormalParameterContext param) {
        return Optional.ofNullable(param)
                .map(JavaParser.LastFormalParameterContext::variableDeclaratorId)
                .map(JavaParser.VariableDeclaratorIdContext::getText)
                .orElse("");
    }

    private String extractVarArgsType(JavaParser.LastFormalParameterContext param) {
        return Optional.ofNullable(param)
                .map(JavaParser.LastFormalParameterContext::typeType)
                .map(JavaParser.TypeTypeContext::getText)
                .orElse("");
    }

    private List<String> extractVarArgsModifiers(JavaParser.LastFormalParameterContext param) {
        List<String> modifiers = new ArrayList<>();
        if (param != null && param.variableModifier() != null) {
            for (JavaParser.VariableModifierContext mod : param.variableModifier()) {
                modifiers.add(mod.getText());
            }
        }
        return Collections.unmodifiableList(modifiers);
    }
}
