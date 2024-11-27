package edu.usb.argos.ASTProcessor.bestpractices;

import java.util.regex.Pattern;

public class HardcodedValueMatcher {
    private static HardcodedValueMatcher instance;

    private static final String HARD_CODED_REGEX =
            "\\s*(\"(\\\\.|[^\"])*\"|-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?|true|false|null|'.')\\s*";

    private final Pattern pattern;

    private HardcodedValueMatcher() {
        this.pattern = Pattern.compile(HARD_CODED_REGEX);
    }

    public static synchronized HardcodedValueMatcher getInstance() {
        if (instance == null) {
            instance = new HardcodedValueMatcher();
        }
        return instance;
    }

    public boolean isHardcoded(String value) {
        return pattern.matcher(value).matches();
    }
}
