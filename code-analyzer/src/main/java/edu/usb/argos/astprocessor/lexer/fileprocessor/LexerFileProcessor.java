package edu.usb.argos.astprocessor.lexer.fileprocessor;

import edu.usb.argos.astprocessor.antlr.JavaLexer;
import edu.usb.argos.astprocessor.lexer.errorhandler.ArgosErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LexerFileProcessor implements IFileProcessor<CommonTokenStream> {
    private final ArgosErrorListener errorListener;

    public LexerFileProcessor() {
        this.errorListener = new ArgosErrorListener();
    }

    public CommonTokenStream getTokensFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String content = new String(Files.readAllBytes(path));

        JavaLexer lexer = new JavaLexer(CharStreams.fromString(content));

        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        return tokens;
    }
}
