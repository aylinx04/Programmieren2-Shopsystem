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
}
