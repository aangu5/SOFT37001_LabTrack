package org.ordep.labtrack.model.enums;

public enum PictogramType {
    GHS01("Explosive"),
    GHS02("Flammable"),
    GHS03("Oxidising"),
    GHS04("Gas"),
    GHS05("Corrosive"),
    GHS06("Toxic"),
    GHS07("Harmful"),
    GHS08("Health Risk"),
    GHS09("Environment");

    private final String displayName;

    PictogramType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
