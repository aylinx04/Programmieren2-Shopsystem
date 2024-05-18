package src.domain;

import src.persistence.FilePersistenceManager;
import src.persistence.PersistenceManager;
import src.valueobjects.Mitarbeiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MitarbeiterVerwaltung {
    List<Mitarbeiter> MitarbeiterListe = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();
    public void liesDaten(String datei) throws IOException {
        MitarbeiterListe = pm.leseMitarbeiterListe(datei);
    }
    public void schreibeDaten(String datei) throws IOException  {
        pm.schreibeMitarbeiterListe(MitarbeiterListe, datei);
    }
    public List<Mitarbeiter> getMitarbeiterListe() {
        return MitarbeiterListe;
    }
}
