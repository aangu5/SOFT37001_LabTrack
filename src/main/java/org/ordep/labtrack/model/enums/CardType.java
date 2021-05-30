package org.ordep.labtrack.model.enums;

public enum CardType {
    BIOLOGICAL("Biological"),
    CHEMICAL("Chemical"),
    PHYSICAL("Physical");

    private final String displayName;

    CardType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
