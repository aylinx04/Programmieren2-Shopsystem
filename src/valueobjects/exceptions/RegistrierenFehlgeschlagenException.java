package src.valueobjects.exceptions;

public class RegistrierenFehlgeschlagenException extends Exception {

    public RegistrierenFehlgeschlagenException() {
        super("Passwörter stimmen nicht überein!");
    }

}
