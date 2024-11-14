package edu.usb.argos.ASTProcessor.visitor.infraestructure.antlr.adapters;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.nodes.Token;
import org.antlr.v4.runtime.CommonTokenStream;

public class AntlrTokenAdapter implements Token<CommonTokenStream> {
    private final CommonTokenStream token;

    public AntlrTokenAdapter(CommonTokenStream token) {
        this.token = token;
    }

    @Override
    public CommonTokenStream getTokenStream() {
        return token;
    }
}
