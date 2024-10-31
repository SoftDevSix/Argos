package com.softdevsix.staticRules.rulesEntities;

public class CodeSmells {

    private boolean noDuplicatedCode = true;
    private boolean methodTooLong = true;
    private int maxMethodLength = 50; 
    private boolean excessiveParameters = true;
    private int maxParameters = 5; 
    private boolean magicNumbers = true;

    public CodeSmells() {
    }

    public boolean isNoDuplicatedCodeEnabled() { return noDuplicatedCode; }
    public void setNoDuplicatedCode(boolean noDuplicatedCode) { this.noDuplicatedCode = noDuplicatedCode; }

    public boolean isMethodTooLongEnabled() { return methodTooLong; }
    public void setMethodTooLong(boolean methodTooLong) { this.methodTooLong = methodTooLong; }

    public int getMaxMethodLength() { return maxMethodLength; }
    public void setMaxMethodLength(int maxMethodLength) { this.maxMethodLength = maxMethodLength; }

    public boolean isExcessiveParametersEnabled() { return excessiveParameters; }
    public void setExcessiveParameters(boolean excessiveParameters) { this.excessiveParameters = excessiveParameters; }

    public int getMaxParameters() { return maxParameters; }
    public void setMaxParameters(int maxParameters) { this.maxParameters = maxParameters; }

    public boolean isMagicNumbersEnabled() { return magicNumbers; }
    public void setMagicNumbers(boolean magicNumbers) { this.magicNumbers = magicNumbers; }

}
