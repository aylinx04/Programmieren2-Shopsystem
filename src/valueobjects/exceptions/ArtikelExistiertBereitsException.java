package src.valueobjects.exceptions;

public class ArtikelExistiertBereitsException extends Exception{

    public ArtikelExistiertBereitsException(String name) {
        super("Artikel '" + name + "' existiert bereits!");
    }
}
