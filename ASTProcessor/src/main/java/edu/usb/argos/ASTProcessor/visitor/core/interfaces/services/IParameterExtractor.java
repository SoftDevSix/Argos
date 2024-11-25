package edu.usb.argos.ASTProcessor.visitor.core.interfaces.services;

import edu.usb.argos.ASTProcessor.visitor.core.entities.method.ParameterInformation;

public interface IParameterExtractor<T, U> {
    ParameterInformation createRegularParameter(T param);
    ParameterInformation createVarArgsParameter(U param);
}
