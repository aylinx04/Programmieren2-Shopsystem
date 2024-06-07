package src.domain;

import src.domain.exceptions.*;
import src.valueobjects.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ShopVerwaltungen {
    Kunde eingeloggt;
    Mitarbeiter eing;
    private Warenkorb wk = new Warenkorb();
    private ArtikelVerwaltung aV;
    private MitarbeiterVerwaltung mV;
    private KundenVerwaltung kV;
    private EreignisVerwaltung eV;
    LocalDate date = LocalDate.now();

    public Warenkorb getWk() {
        return wk;
    }

    public Rechnung erzeugeRechnung() {
        Rechnung rechnung = new Rechnung(eingeloggt);

        Map<String, Artikel> inhalt = wk.getWarenkorb();

        for (Artikel artikel : inhalt.values()) {
            rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
        }
        Ereignis ereignis = new Ereignis(date.toString(),"Kunde: " + eingeloggt.getName(), "Ereignis - Der Einkauf: " + inhalt.values());
        eV.ereignisHinzufuegen(ereignis);

        return rechnung;
    }

    public List<Ereignis> gebeEreignisListe(){
        return eV.getEreignisListe();
    }

    public List<Artikel> gibAlleArtikel() {
        return aV.getArtikelListe();
    }

    public ShopVerwaltungen(String datei) throws IOException {
        aV = new ArtikelVerwaltung();
        aV.liesDaten(datei+"_A.txt");
        mV = new MitarbeiterVerwaltung();
        mV.liesDaten(datei+"_M.txt");
        kV = new KundenVerwaltung();
        kV.liesDaten(datei+"_K.txt");
        eV = new EreignisVerwaltung();
        eV.liesDaten(datei+"_E.txt");
    }

    public int checkLogin(String name, String passwort) throws LoginFehlgeschlagenException {
        for (Kunde u : kV.getKundenListe()) {
            if (u.getName().equals(name) && u.getPasswort().equals(passwort)) {
                eingeloggt = u;
                return 1;
            }
        }
        for (Mitarbeiter m : mV.getMitarbeiterListe()) {
            if (m.getName().equals(name) && m.getPasswort().equals(passwort)) {
                eing = m;
                return 2;
            }
        }
        throw new LoginFehlgeschlagenException();
    }

    public void checkPasswort(String passwort, String passwort2) throws RegistrierenFehlgeschlagenException {
        if(!passwort.equals(passwort2)) {
            throw new RegistrierenFehlgeschlagenException();
        }
    }

    public void checkPackungsgroesse(int packungsgroesse, int bestand) throws PackungsgroesseException {
        if(bestand % packungsgroesse != 0) {
            throw new PackungsgroesseException();
        }
    }

    public Artikel holeArtikel(String name) throws ArtikelNichtGefundenException {
        for (Artikel a : aV.getArtikelListe()){
            if(a.getName().equals(name)){
                return a;
            }
        }
        throw new ArtikelNichtGefundenException(name);
    }

    public void artikelZurueck(String name, int anzahl) {
        for (Artikel a : aV.getArtikelListe()){
            if(a.getName().equals(name)){
                a.bestandErhoehen(anzahl);
            }
        }
    }

    public void artikelBestandVerringern(Artikel a, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {
        if (a instanceof Massengutartikel m) {
            if ((anzahl % m.getPackungsgroesse()) != 0) {
                throw new PackungsgroesseException();
            }
        }
        if (a.getBestand() >= anzahl) {
            a.bestandVerringern(anzahl);
        } else {
            throw new BestandNichtVorhandenException();
        }
    }

    public void istArtikelImWarenkorb(String artikelname) throws ArtikelNichtGefundenException {
        if (!wk.getWarenkorb().containsKey(artikelname)) {
            throw new ArtikelNichtGefundenException(artikelname);
        }
    }

    public void checkAnzahlDesArtikels(String artikelname, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {
        Artikel artikel = wk.getWarenkorb().get(artikelname);
        if (artikel instanceof Massengutartikel m) {
            if ((anzahl % m.getPackungsgroesse()) != 0) {
                throw new PackungsgroesseException();
            }
        }
        if (artikel.getBestand() >= anzahl) {
            wk.artikelEntfernen(artikelname, anzahl);
        } else {
            throw new BestandNichtVorhandenException();
        }
    }

    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort){
        kV.getKundenListe().add(new Kunde(name, kV.getKundenListe().size()+1, passwort, strasse, plz, wohnort));
    }

    public Artikel artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException {
        if (aV.sucheArtikel(name) != null) {
            throw new ArtikelExistiertBereitsException(name);
        }
        Artikel a = new Artikel(name, aV.getArtikelListe().size()+1, preis, bestand);
        aV.getArtikelListe().add(a);
        Ereignis ereignis = new Ereignis(date.toString(), "Mitarbeiter: " + eing.getName(), "Ereignis - Hinzugefügter Artikel: " + a);
        eV.ereignisHinzufuegen(ereignis);
        return a;
    }

    public Artikel artikelAnlegen(String name, double preis, int bestand, int packungsgroesse) throws ArtikelExistiertBereitsException {
        if (aV.sucheArtikel(name) != null) {
            throw new ArtikelExistiertBereitsException(name);
        }
        Artikel a = new Massengutartikel(name, aV.getArtikelListe().size()+1, preis, bestand, packungsgroesse);
        aV.getArtikelListe().add(a);
        Ereignis ereignis = new Ereignis(date.toString(), "Mitarbeiter: " + eing.getName(), "Ereignis - Hinzugefügter Artikel: " + a);
        eV.ereignisHinzufuegen(ereignis);
        return a;
    }

    public void ereignisBestandErhoeht(Artikel artikel, int anzahl) throws PackungsgroesseException {
        if (artikel instanceof Massengutartikel m) {
            if ((anzahl % m.getPackungsgroesse()) != 0) {
                throw new PackungsgroesseException();
            }
        }
        artikel.bestandErhoehen(anzahl);
        Ereignis ereignis = new Ereignis(date.toString(), "Mitarbeiter: " + eing.getName(), "Ereignis - Artikel '" + artikel.getName() + "' um " + anzahl +  " erhoeht.");
        eV.ereignisHinzufuegen(ereignis);
    }

    public void mitarbeiterAnlegen(String name, String passwort) {
        int mitarbeiterNummer = mV.getMitarbeiterListe().size() + 1; // Aktuelle Anzahl der Mitarbeiter + 1
        mV.getMitarbeiterListe().add(new Mitarbeiter(name, mitarbeiterNummer, passwort));
    }

    public void schreibeArtikelDaten(String datei) throws IOException {
        aV.schreibeDaten(datei);
    }

    public void schreibeMitarbeiterDaten(String datei) throws IOException {
        mV.schreibeDaten(datei);
    }

    public void schreibeKundenDaten(String datei) throws IOException {
        kV.schreibeDaten(datei);
    }

    public void schreibeEreignisDaten(String datei) throws IOException {
        eV.schreibeDaten(datei);
    }

    public List<Artikel> sucheArtikel(String titel) {
        List<Artikel> suchErg = new ArrayList<>();
        for (Artikel artikel : aV.getArtikelListe()) {
            if (artikel.getName().contains(titel)) {
                suchErg.add(artikel);
            }
        }
        return suchErg;
    }

    public List<Ereignis> sucheEreignis(String titel) {
        List<Ereignis> suchErg = new ArrayList<>();
        for (Ereignis ereignis : eV.getEreignisListe()) {
            if (ereignis.getStatus().contains(titel)) {
                suchErg.add(ereignis);
            } else if (ereignis.getPerson().contains(titel)) {
                suchErg.add(ereignis);
            } else if (ereignis.getDatum().contains(titel)) {
                suchErg.add(ereignis);
            }
        }
        return suchErg;
    }

    public List<Artikel> sucheNachTitel(String titel) {
        return sucheArtikel(titel);
    }

    public List<Ereignis> sucheNachEreignis(String ereignis){
        return sucheEreignis(ereignis);
    }
}
