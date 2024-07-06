package common;

public class Kunde {
    private String name;
    private int nummer;
    private String passwort;
    private String strasse;
    private String plz;
    private String wohnort;

    public Kunde(String name, int nummer,  String passwort, String strasse, String plz, String wohnort) {
        this.name = name;
        this.nummer = nummer;
        this.passwort = passwort;
        this.strasse = strasse;
        this.plz = plz;
        this.wohnort = wohnort;
    }

    public String getName() {
        return name;
    }

    public int getNummer() {return nummer;}

    public String getPasswort() {return passwort; }

    public String getStrasse() { return strasse; }

    public String getPlz() { return plz; }

    public String getWohnort() { return wohnort; }

    public String toString() {
        return  name + ";" + strasse + ";" + plz + ";" + wohnort;
    }
}
