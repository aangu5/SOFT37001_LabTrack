package org.ordep.labtrack.model.enums;

public enum ProtectiveEquipment {

    LABORATORY_COAT("Laboratory Coat"),
    SAFETY_GOOGLES("Safety Goggles"),
    FACE_SHIELD_MASK("Face Shield/Mask"),
    LATEX_GLOVES("Latex Gloves"),
    NITRILE_GLOVES("Nitrile Gloves"),
    RESPIRATOR("Respirator");

    private final String displayName;

    ProtectiveEquipment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
