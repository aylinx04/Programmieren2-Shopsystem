package src.ui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.ArtikelExistiertBereitsException;
import src.domain.exceptions.ArtikelNichtGefundenException;
import src.domain.exceptions.BestandNichtVorhandenException;
import src.valueobjects.Artikel;
import src.valueobjects.Ereignis;
import src.valueobjects.Rechnung;
import src.valueobjects.Warenkorb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BenutzerOberflaeche {

    private ShopVerwaltungen SV;
    private BufferedReader in;

    public BenutzerOberflaeche(String datei) throws IOException {
        SV = new ShopVerwaltungen(datei);
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        try {
            BenutzerOberflaeche cui = new BenutzerOberflaeche("Shop");
            cui.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() {
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

    private void gibMenueAus() {
        System.out.println("\nBefehle:                      ");
        System.out.println("Registrieren:                '0'");
        System.out.println("Einloggen:                   '1'");
        System.out.println("Artikelliste anzeigen:       '2'");
        System.out.println("--------------------------------");
        System.out.println("Beenden:                     'q'");
        System.out.print("> ");
        System.out.flush();
    }

    private String liesEingabe() throws IOException {
        return in.readLine();
    }

    private void verarbeiteEingabe(String line) throws IOException {
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
                einloggen(name, passwort);
                break;
            case "2":
                artikelListeAnzeigen();
                break;
            case "q":
                System.out.println("Auf Wiedersehen!");
                break;
            default:
                System.err.println("Ungültige Eingabe!");
        }
    }

    private void einloggen(String name, String passwort) throws IOException {
        int zahl = SV.checkLogin(name, passwort);
        if(zahl == 1){
            einloggenKunde();
        } else if(zahl == 2){
            einloggenMitarbeiter();
        } else if (zahl == 0) {
            System.err.println("Falsche Eingabe!");
        }
    }

    private void registrieren(String passwort, String passwort2, String name, String strasse, String plz, String wohnort) {
        boolean erfolg = SV.checkPasswort(passwort, passwort2);
        if (erfolg) {
            SV.kundeAnlegen(name, passwort, strasse, plz, wohnort);
            System.out.println("Erfolgreich registriert!");
        }else{
            System.err.println("Passwörter stimmen nicht überein");
        }
    }

    private void MAanlegen(String passwort, String passwort2, String name) {
        boolean erfolg = SV.checkPasswort(passwort, passwort2);
        if (erfolg) {
            SV.mitarbeiterAnlegen(name, passwort);
            System.out.println("Erfolgreich registriert als Mitarbeiter!");
        } else {
            System.err.println("Passwörter stimmen nicht überein");
        }
    }

    private void hinzufuegenWarenkorb(String artikelname, int anzahl) {
        try {
            Artikel a = SV.holeArtikel(artikelname);
            SV.artikelBestandVerringern(anzahl, a);
            Artikel wkArtikel = new Artikel(a.getName(), a.getNummer(), a.getPreis(), anzahl);
            Warenkorb w = SV.getWarenkorb();
            w.artikelHinzufuegen(wkArtikel);
            System.out.println("Artikel hinzugefügt: " + wkArtikel);
        } catch (ArtikelNichtGefundenException | BestandNichtVorhandenException e) {
            System.err.println(e.getMessage());
        }
    }

    private void entfernenWarenkorb(String artikelname, int anzahl) {
        Warenkorb WK = SV.getWarenkorb();

        try {
            WK.istArtikelImWarenkorb(artikelname);
            WK.checkAnzahlDesArtikels(anzahl, artikelname);
            System.out.println("Der Artikel '" + artikelname + "' wurde erfolgreich " + anzahl + "x entfernt.");
        } catch (ArtikelNichtGefundenException | BestandNichtVorhandenException e){
            System.err.println(e.getMessage());
        }
    }

    private void einloggenKunde() throws IOException {
            System.out.println("Erfolgreich angemeldet!");
            String option = "";
            do {
                System.out.println("\nKunden Optionen:                        ");
                System.out.println("Artikelliste ansehen:                  '1'");
                System.out.println("Warenkorb ansehen:                     '2'");
                System.out.println("------------------------------------------");
                System.out.println("Ausloggen:                             'q'");
                System.out.print("> ");
                System.out.flush();
                option = liesEingabe();
                switch (option) {
                    case "1":
                        artikelListeAnzeigen();
                        break;
                    case "2":
                        warenkorbAnzeigen();
                        break;
                    case "q":
                        System.out.println("Ausgeloggt");
                        SV.schreibeArtikelDaten("Shop_A.txt");
                        SV.schreibeKundenDaten("Shop_K.txt");
                        SV.schreibeEreignisDaten("Shop_E.txt");
                        break;
                    default:
                        System.err.println("Ungültige Eingabe!");
                }
            } while (!option.equals("q")) ;
    }

    private void einloggenMitarbeiter() throws IOException, NumberFormatException {
            System.out.println("Erfolgreich angemeldet!");
            String option = "";
            do {
                System.out.println("\nMitarbeiter Optionen:                   ");
                System.out.println("Artikelliste anzeigen:                 '1'");
                System.out.println("Artikel hinzufügen:                    '2'");
                System.out.println("Bestand erhöhen:                       '3'");
                System.out.println("Mitarbeiter hinzufügen:                '4'");
                System.out.println("Ereignisse anzeigen:                   '5'");
                System.out.println("------------------------------------------");
                System.out.println("Ausloggen:                             'q'");
                System.out.print("> ");
                System.out.flush();
                option = liesEingabe();
                String artikelname;
                double preis;
                int bestand;
                int anzahl;
                String neuerName;
                String passwort1;
                String passwort2;

                switch (option) {
                    case "1":
                        artikelListeAnzeigen();
                        break;
                    case "2":
                        System.out.print("Artikelname  > ");
                        artikelname = liesEingabe();
                        System.out.print("Preis  > ");
                        try {
                            preis = Double.parseDouble(liesEingabe());
                            System.out.print("Bestand  > ");
                            bestand = Integer.parseInt(liesEingabe());
                            Artikel a = SV.artikelAnlegen(artikelname, preis, bestand);
                            System.out.println("Neuer Artikel angelegt: " + a);
                        } catch (NumberFormatException e) {
                            System.err.println("Eingabe muss eine Zahl sein!");
                        } catch (ArtikelExistiertBereitsException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "3":
                        System.out.print("Artikelname  > ");
                        artikelname = liesEingabe();
                        try {
                            Artikel artikel = SV.holeArtikel(artikelname);
                            System.out.print("Anzahl  > ");
                                anzahl = Integer.parseInt(liesEingabe());
                                artikel.bestandErhoehen(anzahl);
                                SV.ereignisBestandErhoeht(artikelname, anzahl);
                                System.out.println("Bestand von '" + artikelname + "' um " + anzahl + " erhöht.");
                        } catch (NumberFormatException e) {
                                System.err.println("Eingabe muss eine Zahl sein!");
                        } catch (ArtikelNichtGefundenException e) {
                                System.err.println(e.getMessage());
                        }
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
                    case "5":
                        System.out.println("Ereignisse: ");
                        Collections.sort(SV.gebeEreignisListe(), Comparator.comparing(Ereignis :: getDatum));
                        SV.gebeEreignisListe().forEach(System.out::println);
                        break;
                    case "q":
                        System.out.println("Ausgeloggt");
                        SV.schreibeArtikelDaten("Shop_A.txt");
                        SV.schreibeMitarbeiterDaten("Shop_M.txt");
                        SV.schreibeEreignisDaten("Shop_E.txt");
                        break;
                    default:
                        System.err.println("Ungültige Eingabe!");
                }
            } while (!option.equals("q"));
    }

    private void artikelListeAnzeigen () throws IOException{
        List<Artikel> artikelListe = SV.gibAlleArtikel();

        String option = "";
        do {
            System.out.println("\nSortieren nach:                          ");
            System.out.println("Von A-Z:                                '1'");
            System.out.println("Von Z-A:                                '2'");
            System.out.println("Artikelnummer aufsteigend               '3'");
            System.out.println("Artikelnummer absteigend:               '4'");
            System.out.println("Preis aufsteigend:                      '5'");
            System.out.println("Preis absteigend:                       '6'");
            System.out.println("Bestand aufsteigend:                    '7'");
            System.out.println("Bestand absteigend:                     '8'");
            System.out.println("-------------------------------------------");
            System.out.println("Beenden:                                'q'");
            System.out.print("> ");
            System.out.flush();
            option = liesEingabe();

            switch (option) {
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
        } while (!option.equals("q"));
    }

    private void warenkorbAnzeigen() throws IOException, NumberFormatException {
        Warenkorb WK = SV.getWarenkorb();

        Map<String, Artikel> warenkorb = WK.getWarenkorb();

        System.out.println("Dein Warenkorb: ");
        for (Artikel a : warenkorb.values()){
            System.out.println(a);
        }
        String option = "";
        do {
            System.out.println("\nOptionen in deinem Warenkorb:           ");
            System.out.println("Warenkorb ansehen:                     '0'");
            System.out.println("Artikel hinzufügen:                    '1'");
            System.out.println("Artikel entfernen:                     '2'");
            System.out.println("Alles im Warenkorb kaufen:             '3'");
            System.out.println("Warenkorb leeren:                      '4'");
            System.out.println("------------------------------------------");
            System.out.println("Beenden:                               'q'");
            System.out.print("> ");
            System.out.flush();
            option = liesEingabe();
            String artikelname;
            int anzahl;
            switch (option) {
                case "0":
                    System.out.println("Dein Warenkorb: ");
                    for (Artikel a : warenkorb.values()){
                        System.out.println(a);
                    }
                    break;
                case "1":
                    artikelListeAnzeigen();
                    System.out.print("Artikelname  > ");
                    artikelname = liesEingabe();
                    System.out.print("Anzahl  > ");
                    try {
                        anzahl = Integer.parseInt(liesEingabe());
                        hinzufuegenWarenkorb(artikelname, anzahl);
                    } catch (NumberFormatException e) {
                        System.err.println("Eingabe muss eine Zahl sein!");
                    }
                    break;
                case "2":
                    System.out.print("Artikelname  > ");
                    artikelname = liesEingabe();
                    System.out.print("Anzahl  > ");
                    anzahl = Integer.parseInt(liesEingabe());
                    entfernenWarenkorb(artikelname, anzahl);
                    SV.artikelZurueck(artikelname, anzahl);
                    break;
                case "3":
                    if (warenkorb.isEmpty()){
                        System.err.println("Warenkorb ist leer!");
                    }else {
                        System.out.println("Sind Sie sicher?:                    ");
                        System.out.println("Fortfahren:                       '1'");
                        System.out.println("Abbrechen:                        '2'");
                        String eingabe = liesEingabe();
                        if (eingabe.equals("1")) {
                            System.out.println("Dein Einkauf: ");
                            Rechnung rechnung = SV.erzeugeRechnung();
                            System.out.println(rechnung);
                            for (Artikel a : warenkorb.values()){
                                System.out.println(a);
                            }
                            WK.warenkorbLeeren();
                        } else if (eingabe.equals("2")) {
                            System.err.println("Vorgang wurde Abgebrochen!");
                        }
                    }
                    break;
                case "4":
                    for (Artikel a : warenkorb.values()){
                        SV.artikelZurueck(a.getName(), a.getBestand());
                    }
                    WK.warenkorbLeeren();
                    System.out.println("Warenkorb wurde geleert");
                    break;
                case "q":
                    break;
                default:
                    System.err.println("Ungültige Eingabe!");
            }
        } while (!option.equals("q"));
    }
}
