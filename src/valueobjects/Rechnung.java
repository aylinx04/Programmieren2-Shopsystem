package src.valueobjects;

import src.domain.ShopVerwaltungen;

import java.time.LocalDate;


public class Rechnung {
    LocalDate datum = LocalDate.now();
    private static double gesamtpreis;
    Kunde kunde = ShopVerwaltungen.getEingeloggt();

    public Rechnung() {
        gesamtpreis = 0.0;
    }

    public static void gesamtpreisErhoehen(double preis) { gesamtpreis += preis; }

    public static void gesamtpreisVerringern(double preis) { gesamtpreis -= preis; }

    public double getGesamtpreis() { return gesamtpreis; }

    public void gesamtpreisAufNull() { gesamtpreis = 0.0; }

    public String toString() {
        return datum + "\nKunde: " + kunde + "\nGesamtpreis " + gesamtpreis + "€";
    }

}
