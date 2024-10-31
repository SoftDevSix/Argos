package com.softdevsix.argos.domain;

public class BestPractices {

    private boolean noHardcodedValues = true;

    public BestPractices() {
    }

    public boolean isNoHardcodedValuesEnabled() { return noHardcodedValues; }
    public void setNoHardcodedValues(boolean noHardcodedValues) { this.noHardcodedValues = noHardcodedValues; }
}
