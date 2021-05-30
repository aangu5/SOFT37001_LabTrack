package org.ordep.labtrack.model.enums;

public enum Severity {
    VERY_LOW("Very Low"),
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    VERY_HIGH("Very High");

    private final String displayName;

    Severity(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
