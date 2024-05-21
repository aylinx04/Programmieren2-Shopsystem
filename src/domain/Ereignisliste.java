package src.domain;

import src.valueobjects.Ereignis;

import java.util.ArrayList;
import java.util.List;

public class Ereignisliste {
    private List<Ereignis> ereignisListe = new ArrayList<>();

    public List<Ereignis> getEreignisListe() {
        return ereignisListe;
    }

    public void ereignisHinzufuegen(Ereignis ereignis) {
        ereignisListe.add(ereignis);
    }
}
