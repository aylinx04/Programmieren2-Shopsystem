package src.domain;

import src.persistence.FilePersistenceManager;
import src.persistence.PersistenceManager;
import src.valueobjects.Kunde;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KundenVerwaltung {
    private List<Kunde> kundenListe = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();

    public void liesDaten(String datei) throws IOException {
        kundenListe = pm.leseKundenListe(datei);
    }

    public void schreibeDaten(String datei) throws IOException  {
        pm.schreibeKundenListe(kundenListe, datei);
    }

    public List<Kunde> getKundenListe() {
        return kundenListe;
    }
}
