package edu.usb.argos.ASTProcessor.visitor.core.entities.method;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Statement;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MethodInformation<S> {
    String name;
    String returnType;
    List<String> modifiers;
    List<ParameterInformation> parameters;
    List<Statement<S>> statements;
    List<String> throwsExceptions;
    List<String> annotations;
    boolean isVarArgs;
}
