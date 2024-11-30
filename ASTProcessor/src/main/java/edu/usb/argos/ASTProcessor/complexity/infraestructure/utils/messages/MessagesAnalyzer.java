package edu.usb.argos.ASTProcessor.complexity.infraestructure.utils.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessagesAnalyzer {
    @UtilityClass
    public class Descriptions {
        public final String IF = "Conditional branch";
        public final String ELSE_IF = "Else-if branch";
        public final String FOR_LOOP = "For loop";
        public final String WHILE_LOOP = "While loop";
        public final String DO_WHILE = "Do-while loop";
        public final String SWITCH_CASE = "Switch case";
        public final String EXCEPTION = "Exception handling";
        public final String LOGICAL_AND = "Logical AND operator";
        public final String LOGICAL_OR = "Logical OR operator";
    }

    @UtilityClass
    public class ContextFormats {
        public final String IF = "if condition: %s";
        public final String ELSE_IF = "else-if condition: %s";
        public final String FOR = "for loop with control: %s";
        public final String WHILE = "while condition: %s";
        public final String DO_WHILE = "do-while condition: %s";
        public final String CASE = "case: %s";
        public final String CATCH = "catch block for: %s";
        public final String EXPRESSION = "in expression: %s";
    }

    @UtilityClass
    public class Operators {
        public final String AND = "&&";
        public final String OR = "||";
    }
}
