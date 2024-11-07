package edu.usb.argos.ASTProcessor.application.logging;

public interface IAppLogger {

    void info(String message);

    void debug(String message);

    void error(String message, Throwable throwable);

}
