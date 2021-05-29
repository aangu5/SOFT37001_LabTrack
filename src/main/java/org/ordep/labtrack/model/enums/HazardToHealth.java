package org.ordep.labtrack.model.enums;

public enum HazardToHealth {
    BIOHAZARD("Biohazard"),
    HARMFUL("Harmful"),
    EXPLOSIVE("Explosive"),
    FLAMMABLE("Flammable"),
    CARCINOGEN("Carginogen"),
    IRRITANT("Irritant"),
    CORROSIVE("Corrosive"),
    MUTAGEN("Mutagen"),
    TOXIC("Toxic"),
    OXIDANT("Oxidant"),
    TERATOGEN("Teratogen");

    private final String displayName;

    HazardToHealth(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
