package edu.usb.argos.astprocessor.analyzer.core.entities.codeSmells;

import java.util.Objects;

public record OrderedPair<T extends Comparable<T>>(T firstElement, T secondElement) {

    public T getFirstOrdered() {
        return firstElement.compareTo(secondElement) <= 0
                ? firstElement
                : secondElement;
    }

    public T getSecondOrdered() {
        return firstElement.compareTo(secondElement) <= 0
                ? secondElement
                : firstElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedPair<?> that = (OrderedPair<?>) o;

        return getFirstOrdered().equals(that.getFirstOrdered()) &&
                getSecondOrdered().equals(that.getSecondOrdered());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstOrdered(), getSecondOrdered());
    }
}
