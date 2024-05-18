package src.domain;

import src.persistence.FilePersistenceManager;
import src.persistence.PersistenceManager;
import src.valueobjects.Mitarbeiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MitarbeiterVerwaltung {
    List<Mitarbeiter> mitarbeiterListe = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();

    public void liesDaten(String datei) throws IOException {
        mitarbeiterListe = pm.leseMitarbeiterListe(datei);
    }

    public void schreibeDaten(String datei) throws IOException  {
        pm.schreibeMitarbeiterListe(mitarbeiterListe, datei);
    }

    public List<Mitarbeiter> getMitarbeiterListe() {
        return mitarbeiterListe;
    }
}
