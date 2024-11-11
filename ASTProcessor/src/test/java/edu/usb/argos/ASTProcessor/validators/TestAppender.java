package edu.usb.argos.ASTProcessor.validators;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.ArrayList;
import java.util.List;

public class TestAppender extends AppenderBase<ILoggingEvent> {
    private final List<String> messages = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent eventObject) {
        messages.add(eventObject.getFormattedMessage());
    }

    public List<String> getMessages() {
        return messages;
    }
}
