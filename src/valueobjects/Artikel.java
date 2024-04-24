package src.valueobjects;

public class Artikel {
    private String name;
    private int nummer;
    private int bestand;

    public Artikel(String name, int nummer){
        this.name = name;
        this.nummer = nummer;
    }

    public String getName() {
        return name;
    }

    public int getNummer() {
        return nummer;
    }

    public int getBestand() { return bestand; }

    public void bestandErhoehen(int bestand) {
        this.bestand += bestand;
    }
}
