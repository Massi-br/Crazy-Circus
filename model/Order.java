package crazy.model;

/**
 * Les ordres passés par le dompteur aux animaux.
 */
public enum Order {
    LO("Lo : G>D"),
    KI("Ki : G<D"),
    MA("Ma :  ^G"),
    NI("Ni :  ^D"),
    SO("So : <->");
    
    private String label;
    
    Order(String lbl) {
        label = lbl;
    }
    
    public String label() {
        return label;
    }
}
