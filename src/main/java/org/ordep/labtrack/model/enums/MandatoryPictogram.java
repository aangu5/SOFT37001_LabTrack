package org.ordep.labtrack.model.enums;

public enum MandatoryPictogram {

    MHS01("Wear eye protection"),
    MHS02("Wear face mask"),
    MHS03("Wear face shield"),
    MHS04("Wear foot protection"),
    MHS05("Wear hand protection"),
    MHS06("Wear head protection"),
    MHS07("Wear hearing protection"),
    MHS08("Wear high visibility clothing"),
    MHS09("Wear laboratory coat"),
    MHS10("Wear protective clothing"),
    MHS11("Wear respiratory protection"),
    MHS12("Wear safety harness"),
    MHS13("Wear hairnets"),
    MHS14("Sound horn"),
    MHS15("Wear seatbelt"),
    MHS16("Pedestrian route"),
    MHS17("Use litter bin"),
    MHS18("Wash your hands"),
    MHS19("General mandatory"),
    MHS20("Keep locked"),
    MHS21("Switch off after use"),
    MHS22("Use adjustable guard"),
    MHS23("Use guards"),
    MHS24("Lift correctly"),
    MHS25("Stack correctly"),
    MHS26("Keep off scaffolding"),
    MHS27("Read instruction manual/booklet");

    private final String displayName;

    MandatoryPictogram(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
