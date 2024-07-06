package server.domain;

import common.Ereignis;
import server.persistence.FilePersistenceManager;
import server.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EreignisVerwaltung {
    private List<Ereignis> ereignisListe = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();

    public List<Ereignis> getEreignisListe() {
        return ereignisListe;
    }

    public void ereignisHinzufuegen(Ereignis ereignis) {
        ereignisListe.add(ereignis);
    }


    public void liesDaten(String datei) throws IOException {
        ereignisListe = pm.leseEreignisListe(datei);
    }

    public void schreibeDaten(String datei) throws IOException {
        pm.schreibeEreignisListe(ereignisListe, datei);
    }
}
