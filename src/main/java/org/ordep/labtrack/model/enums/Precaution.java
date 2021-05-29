package org.ordep.labtrack.model.enums;

public enum Precaution {
    CONTAINMENT_1("Containment 1"),
    CONTAINMENT_2("Containment 2"),
    CLASS_1_CABINET("Class 1 Cabinet"),
    CLASS_2_CABINET("Class 2 Cabinet"),
    FUME_CUPBOARD("Fume Cupboard"),
    LOCAL_EXHAUST("Local Exhaust");

    private final String displayName;

    Precaution(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
