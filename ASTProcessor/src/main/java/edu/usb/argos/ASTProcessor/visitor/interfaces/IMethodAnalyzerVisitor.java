package edu.usb.argos.ASTProcessor.visitor.interfaces;

import edu.usb.argos.ASTProcessor.visitor.models.method.MethodInfo;
import edu.usb.argos.ASTProcessor.visitor.models.method.ParameterInfo;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public interface IMethodAnalyzerVisitor {
    MethodInfo visitMethod(ParserRuleContext ctx);
    int getMethodLines(ParserRuleContext ctx);
    List<String> getMethodModifiers(ParserRuleContext ctx);
    String getReturnType(ParserRuleContext ctx);
    List<ParameterInfo> getParameters(ParserRuleContext ctx);
}
