package src.persistence;

import src.domain.exceptions.ArtikelNichtGefundenException;
import src.valueobjects.Artikel;

import java.io.IOException;
import java.util.List;

public interface PersistenceManager {
    List<Artikel> LeseArtikel(String dateiquelle) throws IOException, ArtikelNichtGefundenException;
    void speicherArtikel(List<Artikel> ArtikelListe, String datei);
}
