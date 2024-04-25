package src.valueobjects;

public class Mitarbeiter {
    private String name;
    private String nummer;
    private String passwort;

    public Mitarbeiter(String name, String nummer, String passwort){
        this.name = name;
        this.nummer = nummer;
        this.passwort = passwort;
    }


    public String getName() {
        return name;
    }

    public String getNummer() {
        return nummer;
    }

    public String getPasswort() { return passwort; }
}