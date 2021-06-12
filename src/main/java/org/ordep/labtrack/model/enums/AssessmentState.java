package org.ordep.labtrack.model.enums;

public enum AssessmentState {
    PENDING("Pending"),
    APPROVED("Approved"),
    DECLINED("Declined");

    private final String displayName;

    AssessmentState(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
