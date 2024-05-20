package src.domain.exceptions;

public class BestandNichtVorhandenException extends Exception {

    public BestandNichtVorhandenException() {
        super("Bestand nicht vorhanden!");
    }
}
