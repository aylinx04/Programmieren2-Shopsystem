package src.valueobjects;

public class Mitarbeiter {
    private String name;
    private int nummer;
    private String passwort;

    public Mitarbeiter(String name, int nummer, String passwort){
        this.name = name;
        this.nummer = nummer;
        this.passwort = passwort;
    }


    public String getName() {
        return name;
    }

    public int getNummer() {
        return nummer;
    }

    public String getPasswort() { return passwort; }
}