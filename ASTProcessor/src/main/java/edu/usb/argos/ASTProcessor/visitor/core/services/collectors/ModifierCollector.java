package edu.usb.argos.ASTProcessor.visitor.core.services.collectors;

import edu.usb.argos.ASTProcessor.visitor.core.interfaces.collectors.IModifierCollector;
import org.antlr.v4.runtime.ParserRuleContext;
import edu.usb.argos.ASTProcessor.antlr.JavaParser;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ModifierCollector implements IModifierCollector<ParserRuleContext> {
    private final ModifierExtractor modifierExtractor;
    private final ParentContextFinder parentFinder;

    public ModifierCollector() {
        this.modifierExtractor = new ModifierExtractor();
        this.parentFinder = new ParentContextFinder();
    }

    @Override
    public List<String> collectModifiers(ParserRuleContext ctx) {
        if (!(ctx instanceof JavaParser.MethodDeclarationContext)) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(parentFinder.findClassBodyDeclarationContext(ctx))
                .map(modifierExtractor::extractModifiers)
                .orElse(Collections.emptyList());
    }
}
