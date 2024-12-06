package edu.usb.argos.astprocessor.analyzer.core.interfaces;

public interface ISimilarityCalculator<T> {
    double computeSimilarity(T firstEntity, T secondEntity);
}
