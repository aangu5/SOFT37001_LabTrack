package org.ordep.labtrack.model.enums;

public enum Likelihood {
    VERY_LOW("Very Low"),
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    VERY_HIGH("Very High");

    private final String displayName;

    Likelihood(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
