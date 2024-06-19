package src.server.domain;

import src.server.persistence.FilePersistenceManager;
import src.server.persistence.PersistenceManager;
import src.common.Mitarbeiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MitarbeiterVerwaltung {
    private List<Mitarbeiter> mitarbeiterListe = new ArrayList<>();
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

    public Mitarbeiter sucheMitarbeiter(String name) {
        for (Mitarbeiter mitarbeiter : mitarbeiterListe) {
            if (mitarbeiter.getName().equals(name)) {
                return mitarbeiter;
            }
        }
        return null;
    }
}
