package src.ui;

import src.domain.ShopVerwaltungen;
import src.valueobjects.Artikel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class BenutzerOberflaeche {

    private static ShopVerwaltungen SV = new ShopVerwaltungen();
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


    public static void main (String[]args){
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
        System.out.println("\nBefehle:                ");
        System.out.println("Einloggen:             '1'");
        System.out.println("Artikelliste anzeigen: '2'");
        System.out.println("--------------------------");
        System.out.println("Beenden:               'q'");
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
                einloggen(name, passwort);
                break;
            case "2":
                artikelListeAnzeigen();
                break;
        }
    }

    private static void einloggen(String name, String passwort){
        boolean erfolg = SV.checkLogin(name, passwort);
        if (erfolg) {
            System.out.println("Erfolgreich angemeldet!");
        }else{
            System.out.println("Falsche Eingabe!");
        }
    }

    private static void artikelListeAnzeigen(){
        List<Artikel> artikelListe = SV.getArtikelListe();

        for(int i = 0; i < artikelListe.size(); i++) {
            Artikel a = artikelListe.get(i);
            System.out.println("Aktueller Artikel: " + a.getName());
        }
    }

}
