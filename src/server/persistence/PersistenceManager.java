package src.server.persistence;

import src.common.Artikel;
import src.common.Ereignis;
import src.common.Kunde;
import src.common.Mitarbeiter;

import java.io.IOException;
import java.util.List;

public interface PersistenceManager {
    List<Artikel> leseArtikelListe(String dateiquelle) throws IOException;
    void schreibeArtikelListe(List<Artikel> ArtikelListe, String datei) throws IOException;
    List<Mitarbeiter> leseMitarbeiterListe(String dateiquelle) throws IOException;
    void schreibeMitarbeiterListe(List<Mitarbeiter> mitarbeiterListe, String datei) throws  IOException;
    List<Kunde> leseKundenListe(String dateiquelle) throws IOException;
    void schreibeKundenListe(List<Kunde> kundenListe, String datei) throws  IOException;
    List<Ereignis> leseEreignisListe(String dateiquelle) throws IOException;
    void schreibeEreignisListe(List<Ereignis> ereignisList, String datei) throws  IOException;
}
