package src.valueobjects.exceptions;

public class BestandNichtVorhandenException extends Exception {

    public BestandNichtVorhandenException() {
        super("Bestand nicht vorhanden!");
    }
}
