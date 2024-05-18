package src.persistence;

import src.domain.exceptions.ArtikelNichtGefundenException;
import src.valueobjects.Artikel;
import src.valueobjects.Mitarbeiter;

import java.io.IOException;
import java.util.List;

public interface PersistenceManager {
    List<Artikel> leseArtikelListe(String dateiquelle) throws IOException;
    void schreibeArtikelListe(List<Artikel> ArtikelListe, String datei) throws IOException;
    List<Mitarbeiter> leseMitarbeiterListe(String dateiquelle) throws IOException;
    void schreibeMitarbeiterListe(List<Mitarbeiter> MitarbeiterListe, String datei) throws  IOException;
}
