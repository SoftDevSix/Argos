package edu.usb.argos.ASTProcessor.infrastructure.utils;

import edu.usb.argos.ASTProcessor.application.logging.IAppLogger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class SLF4JAppLogger implements IAppLogger {

    private final Logger logger;

    public SLF4JAppLogger(Class<?> sourceClass) {
        this.logger = LoggerFactory.getLogger(sourceClass);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
