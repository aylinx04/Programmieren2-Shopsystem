package common;

public class Massengutartikel extends Artikel {
    private int packungsgroesse;

    public Massengutartikel(String name, int nummer, double preis, int bestand, int packungsgroesse) {
        super(name, nummer, preis, bestand);
        this.packungsgroesse = packungsgroesse;
    }

    public int getPackungsgroesse() {
        return packungsgroesse;
    }

    @Override
    public String toString() {
        return "Name= " + super.getName() +
                ", Nummer= " + super.getNummer() +
                ", Preis= " + super.getPreis() + "€" +
                ", Bestand= " + super.getBestand() +
                ", Packungsgroesse= " + packungsgroesse;
    }
}

