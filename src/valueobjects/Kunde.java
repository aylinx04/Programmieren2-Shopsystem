package src.valueobjects;

public class Kunde {
    private String name;
    private int kundenNr;
    private String strasse;
    private String plz;
    private String wohnort;

    public Kunde(String name, int nr) {
        this.name = name;
        kundenNr = nr;
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
}
