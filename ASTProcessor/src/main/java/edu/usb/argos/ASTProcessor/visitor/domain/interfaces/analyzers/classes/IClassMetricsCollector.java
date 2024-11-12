package edu.usb.argos.ASTProcessor.visitor.domain.interfaces.analyzers.classes;

public interface IClassMetricsCollector <T> {

    int getTotalLines(T ctx);
    int getCommentLines(T ctx);
    double getCommentRatio(T ctx);
    int getNumberOfMethods(T ctx);
    int getNumberOfAttributes(T ctx);

}
