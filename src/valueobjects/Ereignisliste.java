package src.valueobjects;

import java.util.ArrayList;
import java.util.List;

public class Ereignisliste {
    List<Ereignis> ereignisListe = new ArrayList<>();

    public List<Ereignis> getEreignisListe() {
        return ereignisListe;
    }

    public void ereignisHinzufuegen(Ereignis ereignis) {
        ereignisListe.add(ereignis);
    }
}
