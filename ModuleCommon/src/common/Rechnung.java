package common;


import java.time.LocalDate;


public class Rechnung {
    LocalDate datum = LocalDate.now();
    private double gesamtpreis;
    Kunde kunde;

    public Rechnung(Kunde kunde) {
        this.kunde = kunde;
        gesamtpreis = 0.0;
    }

    public void gesamtpreisErhoehen(double preis) { gesamtpreis += preis; }

    public String toString() {
        return datum + "\nKunde: " + kunde + "\nGesamtpreis: " + gesamtpreis + "€";
    }

    public LocalDate getDatum() { return datum; }

    public double getGesamtpreis() { return gesamtpreis; }

    public Kunde getKunde() { return kunde; }
}
