package src.common.exceptions;

public class ArtikelExistiertBereitsException extends Exception{

    public ArtikelExistiertBereitsException(String name) {
        super("Artikel '" + name + "' existiert bereits!");
    }
}
