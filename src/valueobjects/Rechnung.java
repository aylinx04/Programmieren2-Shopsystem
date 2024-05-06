package src.valueobjects;

public class Rechnung {
    private static double gesamtpreis;

    public static void gesamtpreisErhoehen(double preis) { gesamtpreis += preis; }

    public static void gesamtpreisVerringern(double preis) { gesamtpreis -= preis; }

    public double getGesamtpreis() { return gesamtpreis; }

}
