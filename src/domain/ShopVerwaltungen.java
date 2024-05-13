package src.domain;

import src.valueobjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShopVerwaltungen {
    List<Artikel> ArtikelListe = new ArrayList<>();
    List<Kunde> KundenListe = new ArrayList<>();
    List<Mitarbeiter> MitarbeiterListe = new ArrayList<>();
    Kunde eingeloggt;
    Mitarbeiter eing;


    // Warenkorb sollte ein lokales Attribut von ShopVerwaltung sein
    private Warenkorb warenkorb = new Warenkorb();

    public Warenkorb getWarenkorb() {
        return warenkorb;
    }


    public Rechnung erzeugeRechnung() {
        Rechnung rechnung = new Rechnung(eingeloggt);
        // ToDo: Bevor der Warenkorb gekauft / geleert wird, hier die Rechnung erzeugen

        Map<String, Artikel> inhalt = warenkorb.getWarenkorb();

        for (Artikel artikel : inhalt.values()) {
            // Jeden Artikel zur Rechnung hinzufügen
            // rechnung.gesamtpreisErhoehen( ... )

            // artikel.getPreis() * artikel.getBestand()

            // Fanta: Bestand 15
            // Kunde kauft 5 -> 5 * 0.99€ = ??
            // Bleiben 10 übrig
        }

        return rechnung;
    }


    public ShopVerwaltungen (){
        ArtikelListe.add(new Artikel("Tomate", 1, 4.99, 28));
        ArtikelListe.add(new Artikel("Gurke", 2,3.59, 17));
        ArtikelListe.add(new Artikel("Reis", 3, 2, 12));
        ArtikelListe.add(new Artikel("Salat", 4,0.99, 6));
        ArtikelListe.add(new Artikel("Fanta", 5, 4, 15));
        KundenListe.add(new Kunde("Peter", 1, "geheim", "Lindenalle 22", "87687", "Bremen"));
        MitarbeiterListe.add(new Mitarbeiter("Helga",6,"auchgeheim"));
    }

    public List<Artikel> getArtikelListe(){
         return ArtikelListe;
     }

    public int checkLogin(String name, String passwort) {
        for (Kunde u : KundenListe) {
            if (u.getName().equals(name) && u.getPasswort().equals(passwort)) {
                eingeloggt = u;
                return 1;
            }
        }
        for (Mitarbeiter m : MitarbeiterListe) {
            if (m.getName().equals(name) && m.getPasswort().equals(passwort)) {
                eing = m;
                return 2;
            }
        }
        return 0;
    }

    public boolean checkPasswort(String passwort, String passwort2){
        return passwort.equals(passwort2);
    }

    public Artikel holeArtikel(String name){
        for (Artikel a : getArtikelListe()){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    public boolean checkBestand(int anzahl, Artikel a){
        return a.getBestand() >= anzahl;
    }

    public boolean checkObEsDenArtikelGibt(String name){
        for (Artikel a : getArtikelListe()){
            if(a.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort){
        KundenListe.add(new Kunde(name, KundenListe.size()+1, passwort, strasse, plz, wohnort));
    }

    public void artikelAnlegen(String name, double preis, int bestand){
         ArtikelListe.add(new Artikel(name, ArtikelListe.size()+1, preis, bestand));
    }

    public void mitarbeiterAnlegen(String name, String passwort) {
        MitarbeiterListe.add(new Mitarbeiter(name, MitarbeiterListe.size()+1, passwort));
    }

    public Kunde getEingeloggt() {
        return eingeloggt;
    }
}
