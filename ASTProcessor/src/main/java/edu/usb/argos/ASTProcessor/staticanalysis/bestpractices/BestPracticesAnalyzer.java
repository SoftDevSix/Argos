package edu.usb.argos.ASTProcessor.staticanalysis.bestpractices;

import edu.usb.argos.ASTProcessor.antlr.JavaLexer;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import edu.usb.argos.ASTProcessor.staticanalysis.AnalysisType;
import edu.usb.argos.ASTProcessor.staticanalysis.analysisresult.AnalysisReport;
import edu.usb.argos.ASTProcessor.staticanalysis.analysisresult.AnalysisResult;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class BestPracticesAnalyzer {
    private final String FILE_PATH;
    private JavaLexer lexer;
    private JavaParser parser;
    private HardcodedValueDetector detector;
    private final String message;

    public BestPracticesAnalyzer(String filePath) {
        this.message = "Hardcoded Value: ";
        this.FILE_PATH = filePath;
        CharStream charStream = CharStreams.fromString(FILE_PATH);
        this.lexer = new JavaLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        this.parser = new JavaParser(tokenStream);

        JavaParser.CompilationUnitContext context = parser.compilationUnit();
        JavaParser.ClassDeclarationContext classCtx = context.typeDeclaration(0).classDeclaration();

        this.detector = new HardcodedValueDetector(classCtx);
    }

    public AnalysisResult analyze() {
        detector.detectHardcodedValues();

        AnalysisResult result = AnalysisResult.builder()
                .analysisType(AnalysisType.BEST_PRACTICES)
                .filePath(FILE_PATH)
                .build();

        var hardcodedValues = detector.getHardcodedValues();

        for (HardcodedDetection hardcodedValue : hardcodedValues) {
            result.addReport(new AnalysisReport(
                    hardcodedValue.getLineNumber(),
                    hardcodedValue.getLineNumber(),
                    message + hardcodedValue.getHardcodedValue()
            ));
        }

        return result;
    }
}
