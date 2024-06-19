package src.common;


import src.common.exceptions.*;

import java.io.IOException;
import java.util.List;


public interface IShopVerwaltung {

    Warenkorb getWk();
    Rechnung erzeugeRechnung();
    List<Ereignis> gibEreignisListe();
    List<Artikel> gibAlleArtikel();
    int checkLogin(String name, String passwort) throws LoginFehlgeschlagenException;
    void checkPasswort(String passwort, String passwort2) throws RegistrierenFehlgeschlagenException;
    void checkPackungsgroesse(int packungsgroesse, int bestand) throws PackungsgroesseException;
    Artikel holeArtikel(String name) throws ArtikelNichtGefundenException;
    void artikelZurueck(String name, int anzahl);
    void artikelBestandVerringern(Artikel a, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException;
    void istArtikelImWarenkorb(String artikelname) throws ArtikelNichtGefundenException;
    void checkAnzahlDesArtikels(String artikelname, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException;
    void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort);
    Artikel artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException;
    Artikel artikelAnlegen(String name, double preis, int bestand, int packungsgroesse) throws ArtikelExistiertBereitsException;
    void ereignisBestandErhoeht(Artikel artikel, int anzahl) throws PackungsgroesseException;
    void mitarbeiterAnlegen(String name, String passwort) throws MitarbeiterExistiertBereitsException;
    void schreibeArtikelDaten(String datei) throws IOException;
    void schreibeMitarbeiterDaten(String datei) throws IOException;
    void schreibeKundenDaten(String datei) throws IOException;
    void schreibeEreignisDaten(String datei) throws IOException;
    List<Artikel> sucheArtikel(String titel);
    List<Ereignis> sucheEreignis(String titel);
    List<Artikel> sucheNachTitel(String titel);
    List<Ereignis> sucheNachEreignis(String ereignis);
}
