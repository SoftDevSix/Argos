package edu.usb.argos.astprocessor.analyzer.core.interfaces;

import java.util.List;

public interface IShingleGenerator<T> {
    List<List<T>> generate(List<T> elements, int frequency);
}
