package com.softdevsix.argos.exception;

public class RulesValidatorException extends RuntimeException {

    public RulesValidatorException(String message){
        super(message);
    }

    public static RulesValidatorException CyclomaticComplexityException() {
        return new RulesValidatorException("The cyclomatic complexity limit must be positive.");
    }

    public static RulesValidatorException NestingDepthException() {
        return new RulesValidatorException("The nesting depth limit must be positive.");
    }

    public static RulesValidatorException LineLengthException() {
        return new RulesValidatorException("The line length limit must be positive.");
    }
    public static RulesValidatorException CoverageThresholdException() {
        return new RulesValidatorException("The coverage threshold must be non-negative.");
    }
}
