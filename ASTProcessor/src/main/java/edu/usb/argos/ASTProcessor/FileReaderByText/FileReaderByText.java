package main.java.edu.usb.argos.ASTProcessor.FileReaderByText;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import java.io.IOException; 
public class FileReaderByText implements IFileAnalyzer<String, ParseTree> {
    public ParseTree readFile(String content) throws Exception {
        try {
            return parseContent(content);
        } catch (Exception e) {
            throw new Exception("error to read file");
        } 
    }

    private ParseTree parseContent(String content) throws Exception {
        if (content == null || content.trim().isEmpty()) {
            throw new Exception("Error to parse content");
        }
        try {
            CharStream input = CharStreams.fromString(content);
            JavaLexer lexer = new JavaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);

            return parser.compilationUnit();
        } catch (Exception e) {
            throw new Exception("Error to parse content");
        }
    }
}
