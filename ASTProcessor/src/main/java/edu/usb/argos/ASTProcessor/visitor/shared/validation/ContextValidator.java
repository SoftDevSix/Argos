package edu.usb.argos.ASTProcessor.visitor.shared.validation;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

public class ContextValidator {

    public static <T, C extends ParserRuleContext> T validateAndExecute(
            ParserRuleContext ctx,
            Class<C> expectedType,
            Function<C, T> operation,
            T defaultValue) {

        if (expectedType.isInstance(ctx)) {
            return operation.apply(expectedType.cast(ctx));
        }
        return defaultValue;
    }
}
