package edu.usb.argos.astprocessor.analyzer.core.interfaces;

public interface IPlainTextHasher {
    String hash(String text);
    byte[] hashAsBytes(String text);
}
