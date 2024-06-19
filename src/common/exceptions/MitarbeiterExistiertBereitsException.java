package src.common.exceptions;

public class MitarbeiterExistiertBereitsException extends Exception {

    public MitarbeiterExistiertBereitsException(String name) {
        super("Mitarbeiter '" + name + "' existiert bereits!");
    }
}
