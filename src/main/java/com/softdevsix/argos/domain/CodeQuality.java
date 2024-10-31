package com.softdevsix.argos.domain;

public class CodeQuality {

    private boolean maxLineLength = true;
    private int maxLineLengthLimit = 120; 
    private boolean noUnusedImports = true;

    public CodeQuality() {
    }

    public boolean isMaxLineLengthEnabled() { 
        return maxLineLength; 
    }

    public void setMaxLineLength(boolean maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public int getMaxLineLengthLimit() { 
        return maxLineLengthLimit; 
    }

    public void setMaxLineLengthLimit(int maxLineLengthLimit) { 
        this.maxLineLengthLimit = maxLineLengthLimit; 
    }

    public boolean isNoUnusedImportsEnabled() { 
        return noUnusedImports; 
    }

    public void setNoUnusedImports(boolean noUnusedImports) {
        this.noUnusedImports = noUnusedImports;
    }
}
