package src.valueobjects;

public class Artikel {
    private String name;
    private int nummer;
    private double preis;
    private int bestand;

    public Artikel(String name, int nummer, double preis, int bestand){
        this.name = name;
        this.nummer = nummer;
        this.preis = preis;
        this.bestand = bestand;
    }

    public String getName() {
        return name;
    }

    public int getNummer() {
        return nummer;
    }

    public double getPreis() {
        return preis;
    }

    public int getBestand() { return bestand; }

    public void bestandErhoehen(int anzahl) {
        this.bestand += anzahl;
    }

    public void bestandVerringern(int anzahl){
        this.bestand -= anzahl;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    @Override
    public String toString() {
        return "Name= " + name +
                ", Nummer= " + nummer +
                ", Preis= " + preis + "€" +
                ", Bestand= " + bestand;
    }
}
