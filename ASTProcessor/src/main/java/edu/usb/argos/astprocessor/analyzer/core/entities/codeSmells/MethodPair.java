package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import lombok.ToString;

import java.util.Objects;

@ToString
public class MethodPair {
    public final String firstMethod;
    public final String secondMethod;

    public MethodPair(String firstMethod, String secondMethod) {
        this.firstMethod = firstMethod.compareTo(secondMethod) <= 0 ? firstMethod : secondMethod;
        this.secondMethod = firstMethod.compareTo(secondMethod) <= 0 ? secondMethod : firstMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodPair methodPair = (MethodPair) o;
        return firstMethod.equals(methodPair.firstMethod) &&
                secondMethod.equals(methodPair.secondMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstMethod, secondMethod);
    }
}