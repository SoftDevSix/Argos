package edu.usb.argos.ASTProcessor.infrastructure.config;

import edu.usb.argos.ASTProcessor.application.logging.IAppLogger;
import edu.usb.argos.ASTProcessor.infrastructure.utils.SLF4JAppLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    @Bean
    public IAppLogger appLogger() {
        return new SLF4JAppLogger(IAppLogger.class);
    }

}
