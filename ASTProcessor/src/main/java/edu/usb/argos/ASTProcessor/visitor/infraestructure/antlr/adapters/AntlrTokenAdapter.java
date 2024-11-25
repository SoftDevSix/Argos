package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Token;
import lombok.Value;
import org.antlr.v4.runtime.CommonTokenStream;

@Value
public class AntlrTokenAdapter implements Token<CommonTokenStream> {
    CommonTokenStream token;

    @Override
    public CommonTokenStream getTokenStream() {
        return token;
    }
}
