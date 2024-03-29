package org.ordep.labtrack.model.enums;

public enum SignalWord {
    NONE("None"),
    WARNING("Warning"),
    DANGER("Danger");

    private final String displayName;

    SignalWord(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
