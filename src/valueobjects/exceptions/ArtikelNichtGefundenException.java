package src.valueobjects.exceptions;

public class ArtikelNichtGefundenException extends Exception {

    public ArtikelNichtGefundenException(String name) {
        super("Artikel '" + name + "' nicht gefunden!");
    }
}
