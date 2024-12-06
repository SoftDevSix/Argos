package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import java.util.Objects;

public record Pair<T extends Comparable<T>>(T first, T second) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?> pair = (Pair<?>) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
