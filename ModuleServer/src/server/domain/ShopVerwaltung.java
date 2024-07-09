package server.domain;

import common.*;
import common.exceptions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


public class ShopVerwaltung implements IShopVerwaltung {
    private String datei;
    private Kunde eingeloggt;
    private Mitarbeiter eing;
    private ThreadLocal<Warenkorb> wk = ThreadLocal.withInitial(Warenkorb::new);
    private ArtikelVerwaltung aV;
    private MitarbeiterVerwaltung mV;
    private KundenVerwaltung kV;
    private EreignisVerwaltung eV;
    private LocalDate date = LocalDate.now();

    public Kunde getLoggedInCustomer() {
        return eingeloggt;
    }

    public Map<String, Artikel> getWk() {
        return wk.get().getWarenkorb();
    }

    synchronized public String erzeugeRechnung() {
        Rechnung rechnung = new Rechnung(eingeloggt);

        Map<String, Artikel> inhalt = wk.get().getWarenkorb();

        for (Artikel artikel : inhalt.values()) {
            rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
        }
        Ereignis ereignis = new Ereignis(date.toString(),"Kunde: " + eingeloggt.getName(),
                "Ereignis - Der Einkauf: " + inhalt.values());
        eV.ereignisHinzufuegen(ereignis);

        return rechnung.toString();
    }

    public List<Ereignis> gibEreignisListe(){
        return eV.getEreignisListe();
    }

    public List<Artikel> gibAlleArtikel() {
        return aV.getArtikelListe();
    }

    public ShopVerwaltung(String datei) throws IOException {
        this.datei = datei;
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

    synchronized public void artikelInDenWk(String name, int anzahl) throws ArtikelNichtGefundenException {
        Artikel artikel = aV.sucheArtikel(name);
        if (artikel == null) {
            throw new ArtikelNichtGefundenException(name);
        }
        Artikel wkArtikel;
        if(artikel instanceof Massengutartikel m){
            wkArtikel = new Massengutartikel(m.getName(), m.getNummer(), m.getPreis(), anzahl, m.getPackungsgroesse());
        } else {
            wkArtikel = new Artikel(artikel.getName(), artikel.getNummer(), artikel.getPreis(), anzahl);
        }
        wk.get().artikelHinzufuegen(wkArtikel);
    }

    public void artikelZurueck(String name, int anzahl) {
        for (Artikel a : aV.getArtikelListe()) {
            if(a.getName().equals(name)) {
                a.bestandErhoehen(anzahl);
            }
        }
    }

    public void artikelBestandVerringern(String name, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {
        for (Artikel a : aV.getArtikelListe()) {
            if (a.getName().equals(name)) {
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
        }
    }

    public void istArtikelImWarenkorb(String artikelname) throws ArtikelNichtGefundenException {
        if (!wk.get().getWarenkorb().containsKey(artikelname)) {
            throw new ArtikelNichtGefundenException(artikelname);
        }
    }

    synchronized public void wkArtikelEntfernen(String artikelname, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {
        Artikel artikel = wk.get().getWarenkorb().get(artikelname);
        if (artikel instanceof Massengutartikel m) {
            if ((anzahl % m.getPackungsgroesse()) != 0) {
                throw new PackungsgroesseException();
            }
        }
        if (artikel.getBestand() >= anzahl) {
            wk.get().artikelEntfernen(artikelname, anzahl);
        } else {
            throw new BestandNichtVorhandenException();
        }
    }

    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort) {
        kV.getKundenListe().add(new Kunde(name, kV.getKundenListe().size()+1, passwort, strasse, plz, wohnort));
    }

    public void artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException {
        if (aV.sucheArtikel(name) != null) {
            throw new ArtikelExistiertBereitsException(name);
        }
        Artikel a = new Artikel(name, aV.getArtikelListe().size()+1, preis, bestand);
        aV.getArtikelListe().add(a);
        Ereignis ereignis = new Ereignis(date.toString(), "Mitarbeiter: " + eing.getName(),
                "Ereignis - Hinzugefügter Artikel: " + a);
        eV.ereignisHinzufuegen(ereignis);
    }

    public void artikelAnlegen(String name, double preis, int bestand, int packungsgroesse) throws ArtikelExistiertBereitsException {
        if (aV.sucheArtikel(name) != null) {
            throw new ArtikelExistiertBereitsException(name);
        }
        Artikel a = new Massengutartikel(name, aV.getArtikelListe().size()+1, preis, bestand, packungsgroesse);
        aV.getArtikelListe().add(a);
        Ereignis ereignis = new Ereignis(date.toString(), "Mitarbeiter: " + eing.getName(),
                "Ereignis - Hinzugefügter Artikel: " + a);
        eV.ereignisHinzufuegen(ereignis);
    }

    public void bestandErhoehen(String name, int anzahl) throws ArtikelNichtGefundenException, PackungsgroesseException {
        Artikel artikel = aV.sucheArtikel(name);
        if (artikel == null) {
            throw new ArtikelNichtGefundenException(name);
        }
        if (artikel instanceof Massengutartikel m) {
            if ((anzahl % m.getPackungsgroesse()) != 0) {
                throw new PackungsgroesseException();
            }
        }
        artikel.bestandErhoehen(anzahl);
        Ereignis ereignis = new Ereignis(date.toString(), "Mitarbeiter: " + eing.getName(),
                "Ereignis - Artikel '" + artikel.getName() + "' um " + anzahl +  " erhoeht.");
        eV.ereignisHinzufuegen(ereignis);
    }

    public void mitarbeiterAnlegen(String name, String passwort) throws MitarbeiterExistiertBereitsException {
        if (mV.sucheMitarbeiter(name) != null) {
            throw new MitarbeiterExistiertBereitsException(name);
        }
        int mitarbeiterNummer = mV.getMitarbeiterListe().size()+1;
        mV.getMitarbeiterListe().add(new Mitarbeiter(name, mitarbeiterNummer, passwort));
    }

    public void schreibeArtikelDaten() throws IOException {
        aV.schreibeDaten(datei+"_A.txt");
    }

    public void schreibeMitarbeiterDaten() throws IOException {
        mV.schreibeDaten(datei+"_M.txt");
    }

    public void schreibeKundenDaten() throws IOException {
        kV.schreibeDaten(datei+"_K.txt");
    }

    public void schreibeEreignisDaten() throws IOException {
        eV.schreibeDaten(datei+"_E.txt");
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

    synchronized public void warenkorbLeeren() {
        Map<String, Artikel> warenkorbMap = wk.get().getWarenkorb();
        for (Artikel a : warenkorbMap.values()){
            artikelZurueck(a.getName(), a.getBestand());
        }
        wk.get().warenkorbLeeren();
    }

    public void warenkorbLeerenNachKauf() {
        wk.get().warenkorbLeeren();
    }

    public void vonAbisZ() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getName));
    }

    public void vonZbisA() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getName));
        Collections.reverse(aV.getArtikelListe());
    }

    public void artikelnummerAufsteigend() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getNummer));
    }

    public void artikelnummerAbsteigend() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getNummer));
        Collections.reverse(aV.getArtikelListe());
    }

    public void preisAufsteigend() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getPreis));
    }

    public void preisAbsteigend() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getPreis));
        Collections.reverse(aV.getArtikelListe());
    }

    public void bestandAufsteigend() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getBestand));
    }

    public void bestandAbsteigend() {
        Collections.sort(aV.getArtikelListe(), Comparator.comparing(Artikel::getBestand));
        Collections.reverse(aV.getArtikelListe());
    }

    public void datumAufsteigend() {
        Collections.sort(eV.getEreignisListe(), Comparator.comparing(Ereignis::getDatum));
    }

    public void datumAbsteigend() {
        Collections.sort(eV.getEreignisListe(), Comparator.comparing(Ereignis::getDatum));
        Collections.reverse(eV.getEreignisListe());
    }
}
