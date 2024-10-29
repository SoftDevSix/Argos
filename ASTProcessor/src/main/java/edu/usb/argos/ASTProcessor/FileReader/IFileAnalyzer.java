package edu.usb.argos.ASTProcessor;

public interface IFileAnalyzer<T, K> {
    T read(K file);
}
