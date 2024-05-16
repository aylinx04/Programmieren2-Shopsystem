package src.domain.exceptions;

public class ArtikelNichtGefundenException extends Exception {

    public ArtikelNichtGefundenException(String name) {
        super("Artikel '" + name + "' nicht gefunden!");
    }
}
