package com.softdevsix.staticRules.rulesEntities;

public class BestPractices {

    private boolean noHardcodedValues = true;

    public BestPractices() {
    }

    public boolean isNoHardcodedValuesEnabled() { return noHardcodedValues; }
    public void setNoHardcodedValues(boolean noHardcodedValues) { this.noHardcodedValues = noHardcodedValues; }
}
