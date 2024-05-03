package src.valueobjects;

public class Kunde {
    private String name;
    private int kundenNr;
    private String strasse;
    private String plz;
    private String wohnort;
    private String passwort;

    public Kunde(String name, int nr,  String passwort, String strasse, String plz, String wohnort) {
        this.name = name;
        kundenNr = nr;
        this.passwort = passwort;
        this.strasse = strasse;
        this.plz = plz;
        this.wohnort = wohnort;
    }

    public String getName() {
        return name;
    }

    public int getKundenNr() {
        return kundenNr;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getPlz() {
        return plz;
    }

    public String getWohnort() {
        return wohnort;
    }

    public String getPasswort() {return passwort;
    }
}
