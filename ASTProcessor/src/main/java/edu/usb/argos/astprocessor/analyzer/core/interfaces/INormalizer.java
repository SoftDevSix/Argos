package edu.usb.argos.astprocessor.analyzer.core.interfaces;

import java.util.List;

public interface INormalizer<S> {
    List<String> normalize(S node);
}
