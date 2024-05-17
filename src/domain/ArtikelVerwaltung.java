package src.domain;

import src.persistence.FilePersistenceManager;
import src.persistence.PersistenceManager;
import src.valueobjects.Artikel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ArtikelVerwaltung {
    private List<Artikel> artikelListe = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();

    public void liesDaten(String datei) throws IOException {
        artikelListe = pm.leseArtikelListe(datei);
    }

    public void schreibeDaten(String datei) throws IOException  {
        pm.schreibeArtikelListe(artikelListe, datei);
    }

    public List<Artikel> getArtikelListe() {
        return artikelListe;
    }
}
