package org.ordep.labtrack.model.enums;

public enum FrequencyOfTask {
    ONE_OFF("One off"),
    ANNUALLY("Annually"),
    QUARTERLY("Quarterly"),
    MONTHLY("Monthly"),
    WEEKLY("Weekly"),
    DAILY("Daily"),
    HOURLY("Hourly");

    private final String displayName;

    FrequencyOfTask(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
