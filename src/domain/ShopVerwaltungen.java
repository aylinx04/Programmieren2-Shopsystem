package src.domain;

import src.domain.exceptions.ArtikelExistiertBereitsException;
import src.domain.exceptions.ArtikelNichtGefundenException;
import src.domain.exceptions.BestandNichtVorhandenException;
import src.valueobjects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShopVerwaltungen {
    Kunde eingeloggt;
    Mitarbeiter eing;
    private Warenkorb warenkorb = new Warenkorb();
    private Ereignisliste ereignisliste = new Ereignisliste();
    private String datei;
    private ArtikelVerwaltung dieArtikel;
    private MitarbeiterVerwaltung MA;
    private KundenVerwaltung kV;

    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    public Rechnung erzeugeRechnung() {
        Rechnung rechnung = new Rechnung(eingeloggt);

        Map<String, Artikel> inhalt = warenkorb.getWarenkorb();

        for (Artikel artikel : inhalt.values()) {
            rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
        }
        Ereignis ereignis = new Ereignis("Kunde: " + eingeloggt.getName() + "\nDer Einkauf: " + inhalt);
        ereignisliste.ereignisHinzufuegen(ereignis);

        return rechnung;
    }

    public List<Ereignis> gebeEreignisListe(){
        return ereignisliste.getEreignisListe();
    }

    public List<Artikel> gibAlleArtikel() {
        return dieArtikel.getArtikelListe();
    }

    public List<Mitarbeiter> gibAlleMitarbeiter(){
        return MA.getMitarbeiterListe();
    }

    public List<Kunde> gibAlleKunden(){ return kV.getKundenListe(); }

    public ShopVerwaltungen(String datei) throws IOException {
        this.datei = datei;
        dieArtikel = new ArtikelVerwaltung();
        dieArtikel.liesDaten(datei+"_A.txt");
        MA = new MitarbeiterVerwaltung();
        MA.liesDaten(datei+"_M.txt");
        kV = new KundenVerwaltung();
        kV.liesDaten(datei+"_K.txt");
    }

    public int checkLogin(String name, String passwort) {
        for (Kunde u : gibAlleKunden()) {
            if (u.getName().equals(name) && u.getPasswort().equals(passwort)) {
                eingeloggt = u;
                return 1;
            }
        }
        for (Mitarbeiter m : MA.getMitarbeiterListe()) {
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

    public Artikel holeArtikel(String name) throws ArtikelNichtGefundenException {
        for (Artikel a : gibAlleArtikel()){
            if(a.getName().equals(name)){
                return a;
            }
        }
        throw new ArtikelNichtGefundenException(name);
    }

    public void artikelZurueck(String name, int anzahl) {
        for (Artikel a : gibAlleArtikel()){
            if(a.getName().equals(name)){
                a.bestandErhoehen(anzahl);
            }
        }
    }

    public void artikelBestandVerringern(int anzahl, Artikel a) throws BestandNichtVorhandenException {
        if (a.getBestand() >= anzahl) {
            a.bestandVerringern(anzahl);
        } else {
            throw new BestandNichtVorhandenException();
        }
    }

    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort){
        gibAlleKunden().add(new Kunde(name, gibAlleKunden().size()+1, passwort, strasse, plz, wohnort));
    }

    public Artikel artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException {
        if (dieArtikel.sucheArtikel(name) != null) {
            throw new ArtikelExistiertBereitsException(name);
        }
        Artikel a = new Artikel(name, gibAlleArtikel().size()+1, preis, bestand);
        gibAlleArtikel().add(a);
        Ereignis ereignis = new Ereignis("Mitarbeiter: " + eing.getName() + "\nHinzugefügter Artikel: " + a);
        ereignisliste.ereignisHinzufuegen(ereignis);
        return a;
    }

    public void ereignisBestandErhoeht(String artikelname, int anzahl){
        Ereignis ereignis = new Ereignis("Mitarbeiter: " + eing.getName() + "\nArtikel: " + artikelname + "\nErhöhter Bestand: " + anzahl);
        ereignisliste.ereignisHinzufuegen(ereignis);
    }

    public void mitarbeiterAnlegen(String name, String passwort) {
        int mitarbeiterNummer = gibAlleMitarbeiter().size() + 1; // Aktuelle Anzahl der Mitarbeiter + 1
        gibAlleMitarbeiter().add(new Mitarbeiter(name, mitarbeiterNummer, passwort));
        Ereignis ereignis = new Ereignis("Mitarbeiter: " + eing.getName() + "\nHinzugefügter Mitarbeiter: " + name);
        ereignisliste.ereignisHinzufuegen(ereignis);
    }

    public void schreibeArtikelDaten(String datei) throws IOException {
        dieArtikel.schreibeDaten(datei);
    }

    public void schreibeMitarbeiterDaten(String datei) throws IOException {
        MA.schreibeDaten(datei);
    }

    public void schreibeKundenDaten(String datei) throws IOException {
        kV.schreibeDaten(datei);
    }
}
