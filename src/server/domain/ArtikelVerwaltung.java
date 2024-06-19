package src.server.domain;

import src.server.persistence.FilePersistenceManager;
import src.server.persistence.PersistenceManager;
import src.common.Artikel;

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

    public Artikel sucheArtikel(String name) {
        for (Artikel artikel : artikelListe) {
            if (artikel.getName().equals(name)) {
                return artikel;
            }
        }
        return null;
    }
}
