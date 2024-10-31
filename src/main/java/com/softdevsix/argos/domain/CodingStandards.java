package com.softdevsix.argos.domain;

public class CodingStandards {

    private boolean camelCaseNaming = true;
    private boolean pascalCaseForClasses = true;
    private boolean bracesOnSameLine = true;

    public CodingStandards() {
    }

    public boolean isCamelCaseNamingEnabled() { return camelCaseNaming; }
    public void setCamelCaseNaming(boolean camelCaseNaming) { this.camelCaseNaming = camelCaseNaming; }

    public boolean isPascalCaseForClassesEnabled() { return pascalCaseForClasses; }
    public void setPascalCaseForClasses(boolean pascalCaseForClasses) { this.pascalCaseForClasses = pascalCaseForClasses; }

    public boolean isBracesOnSameLineEnabled() { return bracesOnSameLine; }
    public void setBracesOnSameLine(boolean bracesOnSameLine) { this.bracesOnSameLine = bracesOnSameLine; }
}
