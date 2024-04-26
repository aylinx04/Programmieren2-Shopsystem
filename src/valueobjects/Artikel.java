package src.valueobjects;

public class Artikel {
    private String name;
    private int nummer;
    private String preis;
    private int bestand;

    public Artikel(String name, int nummer, String preis){
        this.name = name;
        this.nummer = nummer;
        this.preis = preis;
    }

    public Artikel() {

    }

    public String getName() {
        return name;
    }

    public int getNummer() {
        return nummer;
    }

    public int getBestand() { return bestand; }
    public String getPreis() {
        return preis;
    }
    public void bestandErhoehen(int bestand) {
        this.bestand += bestand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public void setPreis(String preis) {
        this.preis = preis;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    @Override
    public String toString() {
        return "Name= " + name +
                ", Nummer= " + nummer +
                ", Preis= " + preis +
                ", Bestand= " + bestand;
    }
}
