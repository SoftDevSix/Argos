package edu.usb.argos.astprocessor.visitor.infraestructure.antlr.visitors.shared.validation;

import lombok.experimental.UtilityClass;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

@UtilityClass
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
