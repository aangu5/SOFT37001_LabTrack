package org.ordep.labtrack.model.enums;

public enum BioSafetyLevel {
    NONE("N/A"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4");

    private final String displayName;

    BioSafetyLevel(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
