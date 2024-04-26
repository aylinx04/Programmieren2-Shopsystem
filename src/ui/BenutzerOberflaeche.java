package src.ui;

import src.domain.ShopVerwaltungen;
import src.valueobjects.Artikel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BenutzerOberflaeche {

    private static ShopVerwaltungen SV = new ShopVerwaltungen();
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

        switch (line) {
            case "1":
                System.out.print("Name  > ");
                name = liesEingabe();
                System.out.print("Passwort  > ");
                passwort = liesEingabe();
                einloggenKunde(name, passwort);
                break;
            case "2":
                System.out.println("Name: ");
                name = liesEingabe();
                System.out.println("Passwort: ");
                passwort = liesEingabe();
                einloggenMitarbeiter(name, passwort);
                break;
            case "3":
                artikelListeAnzeigen();
                break;
        }
    }

    private static void einloggenKunde(String name, String passwort) throws IOException {
        boolean erfolg = SV.checkLoginKunde(name, passwort);
        if (erfolg) {
            System.out.println("Erfolgreich angemeldet!");

            System.out.println("Kunden Optionen:                          ");
            System.out.println("Warenkorb ansehen:                     '1'");
            System.out.println("Ausloggen:                             '2'");
            System.out.println("------------------------------------------");
            System.out.println("Beenden:                               'q'");
            System.out.print("> ");
            System.out.flush();
            String optionK = liesEingabe();
            switch (optionK) {
                case "1":
                    warenkorbAnzeigen();
                    break;
                case "2":
                    System.out.println("Ausgeloggt");
            }
        } else {
            System.out.println("Falsche Eingabe!");
        }

    }

    private static void einloggenMitarbeiter(String name, String passwort) throws IOException {
        boolean erfolg = SV.checkLoginMitarbeiter(name, passwort);
        if (erfolg) {
            System.out.println("Erfolgreich angemeldet!");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("Mitarbeiter Optionen:                     ");
            System.out.println("Artikel hinzufügen:                    '1'");
            System.out.println("Bestand erhöhen:                       '2'");
            System.out.println("Mitarbeiter hinzufügen:                '3'");
            System.out.println("Bestand erhöhen:                       '4'");
            System.out.println("------------------------------------------");
            System.out.println("Beenden:                               'q'");
            System.out.print("> ");
            System.out.flush();
            String optionM = liesEingabe();
            Scanner scanner = new Scanner(System.in);
            int auswahl;
            switch (optionM) {
                case "1":
                    Artikel neuerArtikel = new Artikel();
                    System.out.println("Neuer Artikel Name: ");
                    break;
                case "2":
                    System.out.println("Entfernt");
            }
        } else {
            System.out.println("Falsche Eingabe!");
        }
    }

    private static void artikelListeAnzeigen () throws IOException{
        List<Artikel> artikelListe = SV.getArtikelListe();

        for (int i = 0; i < artikelListe.size(); i++) {
            Artikel a = artikelListe.get(i);
            System.out.println("Name: " + a.getName() + " Artikelnummer: " + a.getNummer() + " Preis: " + a.getPreis());
        }

        System.out.println("Sotieren nach:                             ");
        System.out.println("Von A-Z:                                '1'");
        System.out.println("Von Z-A:                                '2'");
        System.out.println("Arikelnummer aufsteigend                '3'");
        System.out.println("Artikelnummer absteigend:               '4'");
        System.out.println("Preis aufsteigend:                      '5'");
        System.out.println("Preis absteigend:                       '6'");
        System.out.println("Bestand absteigend:                     '7'");
        System.out.println("Bestand absteigend:                     '8'");
        System.out.println("-------------------------------------------");
        System.out.println("Beenden:                                'q'");
        System.out.print("> ");
        System.out.flush();
        String optionA = liesEingabe();

        switch (optionA){
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
        }


    }

    private static void warenkorbAnzeigen() throws IOException {
        List<Artikel> WK = SV.getWK();

        for (int i = 0; i < WK.size(); i++) {
            Artikel b = WK.get(i);
            System.out.println("Dein Warenkorb: " + b.getName());
        }
        System.out.println("Optionen in deinem Warenkorb:             ");
        System.out.println("Artikel hinzufügen:                    '1'");
        System.out.println("Artikel entfernen:                     '2'");
        System.out.println("------------------------------------------");
        System.out.println("Beenden:                               'q'");
        System.out.print("> ");
        System.out.flush();
        String option = liesEingabe();
        switch (option) {
            case "1":
                System.out.println("Hinzugefügt");
            case "2":
                System.out.println("Entfernt");
        }

    }
}
