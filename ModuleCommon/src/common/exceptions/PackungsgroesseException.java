package common.exceptions;

public class PackungsgroesseException extends Exception {

    public PackungsgroesseException() {
        super("Anzahl muss Vielfaches der Packungsgroesse sein!");
    }

}
