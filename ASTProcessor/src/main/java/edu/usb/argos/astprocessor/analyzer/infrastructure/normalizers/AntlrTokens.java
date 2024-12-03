package edu.usb.argos.astprocessor.analyzer.infrastructure.normalizers;

import java.util.List;

public class AntlrTokens {
    public static final String IDENTIFIER_TOKEN = "IDENTIFIER";
    public static final String LITERAL_TOKEN = "LITERAL";
    public static final List<String> LITERAL_TOKENS =
            List.of("DECIMAL_LITERAL", "HEX_LITERAL", "OCT_LITERAL",
                    "BINARY_LITERAL", "FLOAT_LITERAL");
}
