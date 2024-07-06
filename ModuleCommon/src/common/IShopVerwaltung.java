package common;


import common.exceptions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface IShopVerwaltung {
    Kunde getLoggedInCustomer();
    Map<String, Artikel> getWk();
    String erzeugeRechnung();
    List<Ereignis> gibEreignisListe();
    List<Artikel> gibAlleArtikel();
    int checkLogin(String name, String passwort) throws LoginFehlgeschlagenException;
    void checkPasswort(String passwort, String passwort2) throws RegistrierenFehlgeschlagenException;
    void checkPackungsgroesse(int packungsgroesse, int bestand) throws PackungsgroesseException;
    void artikelInDenWk(String name, int anzahl) throws ArtikelNichtGefundenException;
    void artikelZurueck(String name, int anzahl);
    void artikelBestandVerringern(String name, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException;
    void istArtikelImWarenkorb(String artikelname) throws ArtikelNichtGefundenException;
    void wkArtikelEntfernen(String artikelname, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException;
    void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort);
    void artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException;
    void artikelAnlegen(String name, double preis, int bestand, int packungsgroesse) throws ArtikelExistiertBereitsException;
    void bestandErhoehen(String name, int anzahl) throws ArtikelNichtGefundenException, PackungsgroesseException;
    void mitarbeiterAnlegen(String name, String passwort) throws MitarbeiterExistiertBereitsException;
    void schreibeArtikelDaten() throws IOException;
    void schreibeMitarbeiterDaten() throws IOException;
    void schreibeKundenDaten() throws IOException;
    void schreibeEreignisDaten() throws IOException;
    List<Artikel> sucheArtikel(String titel);
    List<Ereignis> sucheEreignis(String titel);
    void warenkorbLeeren();
    void warenkorbLeerenNachKauf();
    void vonAbisZ();
    void vonZbisA();
    void artikelnummerAufsteigend();
    void artikelnummerAbsteigend();
    void preisAufsteigend();
    void preisAbsteigend();
    void bestandAufsteigend();
    void bestandAbsteigend();
    void datumAufsteigend();
    void datumAbsteigend();
}
