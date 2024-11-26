package edu.usb.argos.ASTProcessor.visitor.core.interfaces.services;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;

import java.util.Optional;

public interface IParameterExtractor<T, U> {
    Optional<ParameterInformation> createRegularParameter(T param);
    Optional<ParameterInformation> createVarArgsParameter(U param);
}
