package src.ui;

import src.domain.ShopVerwaltungen;
import src.valueobjects.Artikel;
import src.valueobjects.Warenkorb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BenutzerOberflaeche {

    private static ShopVerwaltungen SV = new ShopVerwaltungen();
    private static Warenkorb WK = new Warenkorb();
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) {
        run();
    }

    public static void run() {
        String input = "";

        do {
            gibMenueAus();
            try {
                input = liesEingabe();
                verarbeiteEingabe(input);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } while (!input.equals("q"));
    }

    private static void gibMenueAus() {
        System.out.println("\nBefehle:                      ");
        System.out.println("Registrieren:                '0'");
        System.out.println("Einloggen als Kunde:         '1'");
        System.out.println("Einloggen als Mitarbeiter:   '2'");
        System.out.println("Artikelliste anzeigen:       '3'");
        System.out.println("--------------------------------");
        System.out.println("Beenden:                     'q'");
        System.out.print("> ");
        System.out.flush();
    }

    private static String liesEingabe() throws IOException {
        return in.readLine();
    }

    private static void verarbeiteEingabe(String line) throws IOException {
        String name;
        String passwort;
        String passwort2;
        String strasse;
        String plz;
        String wohnort;

        switch (line) {
            case "0":
                System.out.print("Name  > ");
                name = liesEingabe();
                System.out.print("Passwort  > ");
                passwort = liesEingabe();
                System.out.print("Passwort wiederholen  > ");
                passwort2 = liesEingabe();
                System.out.print("Straße  > ");
                strasse = liesEingabe();
                System.out.print("Postleitzahl  > ");
                plz = liesEingabe();
                System.out.print("Wohnort  > ");
                wohnort = liesEingabe();
                registrieren(passwort, passwort2, name, strasse, plz, wohnort);
                break;
            case "1":
                System.out.print("Name  > ");
                name = liesEingabe();
                System.out.print("Passwort  > ");
                passwort = liesEingabe();
                einloggenKunde(name, passwort);
                break;
            case "2":
                System.out.print("Name  > ");
                name = liesEingabe();
                System.out.print("Passwort  > ");
                passwort = liesEingabe();
                einloggenMitarbeiter(name, passwort);
                break;
            case "3":
                artikelListeAnzeigen();
                break;
            case "q":
                System.out.println("Auf Wiedersehen!");
                break;
            default:
                System.err.println("Ungültige Eingabe!");
        }
    }

    private static void registrieren(String passwort, String passwort2, String name, String strasse, String plz, String wohnort) throws IOException {
        boolean erfolg = SV.checkPasswort(passwort, passwort2);
        if (erfolg) {
            SV.kundeAnlegen(name, passwort, strasse, plz, wohnort);
            System.out.println("Erfolgreich registriert!");
        }else{
            System.err.println("Passwörter stimmen nicht überein");
        }
    }

    private static void MAanlegen(String passwort, String passwort2, String name) throws IOException {
        boolean erfolg = SV.checkPasswort(passwort, passwort2);
        if (erfolg) {
            SV.mitarbeiterAnlegen(name, passwort);
            System.out.println("Erfolgreich registriert als Mitarbeiter!");
        } else {
            System.err.println("Passwörter stimmen nicht überein");
        }
    }

    private static void hinzufuegenWarenkorb(String artikelname, int anzahl) {
        if (SV.checkObEsDenArtikelGibt(artikelname)) {
            Artikel a = SV.holeArtikel(artikelname);
            boolean erfolg = SV.checkBestand(anzahl, a);
            if (erfolg) {
                a.bestandVerringern(anzahl);
                Artikel wkArtikel = new Artikel(a.getName(), a.getNummer(), a.getPreis(), anzahl);
                WK.artikelHinzufuegen(wkArtikel);
                System.out.println("Artikel hinzugefügt: " + wkArtikel);
            } else {
                System.err.println("Bestand nicht vorhanden!");
            }
        } else {
            System.err.println("Artikel nicht gefunden!");
        }
    }


    private static void einloggenKunde(String name, String passwort) throws IOException {
        boolean erfolg = SV.checkLoginKunde(name, passwort);
        if (erfolg) {
            System.out.println("Erfolgreich angemeldet!");
            String optionK = "";
            do {
                System.out.println("\nKunden Optionen:                        ");
                System.out.println("Artikelliste ansehen:                  '1'");
                System.out.println("Warenkorb ansehen:                     '2'");
                System.out.println("------------------------------------------");
                System.out.println("Ausloggen:                             'q'");
                System.out.print("> ");
                System.out.flush();
                optionK = liesEingabe();
                switch (optionK) {
                    case "1":
                        artikelListeAnzeigen();
                        break;
                    case "2":
                        warenkorbAnzeigen();
                        break;
                    case "q":
                        System.out.println("Ausgeloggt");
                        break;
                    default:
                        System.err.println("Ungültige Eingabe!");
                }
            } while (!optionK.equals("q")) ;
        } else {
            System.err.println("Falsche Eingabe!");
        }
    }

    private static void einloggenMitarbeiter(String name, String passwort) throws IOException, NumberFormatException {
        boolean erfolg = SV.checkLoginMitarbeiter(name, passwort);
        if (erfolg) {
            System.out.println("Erfolgreich angemeldet!");
            String optionM = "";
            do {
                System.out.println("\nMitarbeiter Optionen:                   ");
                System.out.println("Artikelliste anzeigen:                 '1'");
                System.out.println("Artikel hinzufügen:                    '2'");
                System.out.println("Bestand erhöhen:                       '3'");
                System.out.println("Mitarbeiter hinzufügen:                '4'");
                System.out.println("------------------------------------------");
                System.out.println("Ausloggen:                             'q'");
                System.out.print("> ");
                System.out.flush();
                optionM = liesEingabe();
                String artikelname;
                double preis;
                int bestand;
                int anzahl;
                String neuerName;
                String passwort1;
                String passwort2;

                switch (optionM) {
                    case "1":
                        artikelListeAnzeigen();
                        break;
                    case "2":
                        System.out.print("Artikelname  > ");
                        artikelname = liesEingabe();
                        System.out.print("Preis  > ");
                        preis = Double.parseDouble(liesEingabe());
                        System.out.print("Bestand  > ");
                        bestand = Integer.parseInt(liesEingabe()); //Fehlerbehandlung notwendig
                        SV.artikelAnlegen(artikelname, preis, bestand);
                        System.out.println("Neuer Artikel angelegt: " + SV.getArtikelListe().get(SV.getArtikelListe().size() - 1));
                        break;
                    case "3":
                        System.out.print("Artikelname  > ");
                        artikelname = liesEingabe();
                        Artikel artikel = SV.holeArtikel(artikelname);
                        System.out.print("Anzahl  > ");
                        anzahl = Integer.parseInt(liesEingabe()); //Fehlerbehandlung notwendig
                        artikel.bestandErhoehen(anzahl);
                        System.out.println("Bestand erhöht");
                        break;
                    case "4":
                        System.out.print("Name  > ");
                        neuerName = liesEingabe();
                        System.out.print("Passwort  > ");
                        passwort1 = liesEingabe();
                        System.out.print("Passwort wiederholen  > ");
                        passwort2 = liesEingabe();
                        MAanlegen(passwort1, passwort2, neuerName);
                        break;
                    case "q":
                        System.out.println("Ausgeloggt");
                        break;
                    default:
                        System.err.println("Ungültige Eingabe!");
                }
            } while (!optionM.equals("q"));
        } else {
            System.err.println("Falsche Eingabe!");
        }
    }

    private static void artikelListeAnzeigen () throws IOException{
        List<Artikel> artikelListe = SV.getArtikelListe();

        for (int i = 0; i < artikelListe.size(); i++) {
            Artikel a = artikelListe.get(i);
            System.out.println("Name: " + a.getName() + " Artikelnummer: " + a.getNummer() + " Preis: " + a.getPreis() + "€");
        }
        String optionA = "";
        do {
            System.out.println("\nSortieren nach:                          ");
            System.out.println("Von A-Z:                                '1'");
            System.out.println("Von Z-A:                                '2'");
            System.out.println("Artikelnummer aufsteigend               '3'");
            System.out.println("Artikelnummer absteigend:               '4'");
            System.out.println("Preis aufsteigend:                      '5'");
            System.out.println("Preis absteigend:                       '6'");
            System.out.println("Bestand absteigend:                     '7'");
            System.out.println("Bestand absteigend:                     '8'");
            System.out.println("-------------------------------------------");
            System.out.println("Beenden:                                'q'");
            System.out.print("> ");
            System.out.flush();
            optionA = liesEingabe();

            switch (optionA) {
                case "1":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getName));
                    artikelListe.forEach(System.out::println);
                    break;
                case "2":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getName));
                    Collections.reverse(artikelListe);
                    artikelListe.forEach(System.out::println);
                    break;
                case "3":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getNummer));
                    artikelListe.forEach(System.out::println);
                    break;
                case "4":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getNummer));
                    Collections.reverse(artikelListe);
                    artikelListe.forEach(System.out::println);
                    break;
                case "5":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getPreis));
                    artikelListe.forEach(System.out::println);
                    break;
                case "6":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getPreis));
                    Collections.reverse(artikelListe);
                    artikelListe.forEach(System.out::println);
                    break;
                case "7":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getBestand));
                    artikelListe.forEach(System.out::println);
                    break;
                case "8":
                    Collections.sort(artikelListe, Comparator.comparing(Artikel::getBestand));
                    Collections.reverse(artikelListe);
                    artikelListe.forEach(System.out::println);
                    break;
                case "q":
                    break;
                default:
                    System.err.println("Ungültige Eingabe!");
            }
        } while (!optionA.equals("q"));
    }

    private static void warenkorbAnzeigen() throws IOException, NumberFormatException {
        List<Artikel> warenkorb = WK.getWarenkorb();

        for (int i = 0; i < warenkorb.size(); i++) {
            Artikel b = warenkorb.get(i);
            System.out.println("Dein Warenkorb: " + b.toString());
        }
        String optionW = "";
        do {
            System.out.println("\nOptionen in deinem Warenkorb:           ");
            System.out.println("Artikel hinzufügen:                    '1'");
            System.out.println("Artikel entfernen:                     '2'");
            System.out.println("Alles im Warenkorb kaufen:             '3'");
            System.out.println("------------------------------------------");
            System.out.println("Beenden:                               'q'");
            System.out.print("> ");
            System.out.flush();
            optionW = liesEingabe();
            String artikelname;
            int anzahl;
            switch (optionW) {
                case "1":
                    System.out.print("Artikelname  > ");
                    artikelname = liesEingabe();
                    System.out.print("Anzahl  > ");
                    anzahl = Integer.parseInt(liesEingabe()); //Fehlerbehandlung notwendig
                    hinzufuegenWarenkorb(artikelname, anzahl);
                    break;
                case "2":
                    //Platzhalter
                    System.out.println("Artikel entfernt");
                    break;
                case "3":

                    break;
                case "q":
                    break;
                default:
                    System.err.println("Ungültige Eingabe!");
            }
        } while (!optionW.equals("q"));
    }
}
