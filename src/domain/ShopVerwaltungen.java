package src.domain;

import src.domain.exceptions.ArtikelNichtGefundenException;
import src.valueobjects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShopVerwaltungen {
    List<Kunde> KundenListe = new ArrayList<>();
    List<Mitarbeiter> MitarbeiterListe = new ArrayList<>();
    Kunde eingeloggt;
    Mitarbeiter eing;
    private Warenkorb warenkorb = new Warenkorb();
    private Ereignisliste ereignisliste = new Ereignisliste();
    private String datei;
    private ArtikelVerwaltung dieArtikel;

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

    public ShopVerwaltungen (String datei) throws IOException {
        KundenListe.add(new Kunde("Peter", 1, "geheim", "Lindenalle 22", "87687", "Bremen"));
        MitarbeiterListe.add(new Mitarbeiter("Helga",6,"auchgeheim"));
        this.datei = datei;
        dieArtikel = new ArtikelVerwaltung();
        dieArtikel.liesDaten(datei+"_A.txt");
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

    public Artikel holeArtikel(String name) throws ArtikelNichtGefundenException {
        for (Artikel a : gibAlleArtikel()){
            if(a.getName().equals(name)){
                return a;
            }
        }
        throw new ArtikelNichtGefundenException(name);
    }

    public boolean checkBestand(int anzahl, Artikel a){
        return a.getBestand() >= anzahl;
    }

    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort){
        KundenListe.add(new Kunde(name, KundenListe.size()+1, passwort, strasse, plz, wohnort));
    }

    public void artikelAnlegen(String name, double preis, int bestand) throws IOException {
        Artikel a = new Artikel(name, gibAlleArtikel().size()+1, preis, bestand);
        gibAlleArtikel().add(a);
        dieArtikel.schreibeDaten("Shop_A.txt");
        Ereignis ereignis = new Ereignis("Mitarbeiter: " + eing.getName() + "\nHinzugefügter Artikel: " + a);
        ereignisliste.ereignisHinzufuegen(ereignis);
    }
    public void ereignisBestandErhoeht(String artikelname, int anzahl){
        Ereignis ereignis = new Ereignis("Mitarbeiter: " + eing.getName() + "\nArtikel: " + artikelname + "\nErhöhter Bestand: " + anzahl);
        ereignisliste.ereignisHinzufuegen(ereignis);
    }

    public void mitarbeiterAnlegen(String name, String passwort) {
        MitarbeiterListe.add(new Mitarbeiter(name, MitarbeiterListe.size()+1, passwort));
    }
    public void schreibeDaten(String datei) throws IOException {
        dieArtikel.schreibeDaten(datei);
    }
}
