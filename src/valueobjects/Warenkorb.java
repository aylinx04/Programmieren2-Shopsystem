package src.valueobjects;


import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    List<Artikel> warenkorb = new ArrayList<>();

    public Warenkorb(){
        warenkorb.add(new Artikel("Brot", 7, 2.99, 22));
    }

    public List<Artikel> getWarenkorb() {return warenkorb;}

    public void artikelHinzufuegen(Artikel artikel){
        warenkorb.add(artikel);
    }

    public void warenkorbLeeren(){
        warenkorb.clear();
    }

    private void clear() {
    }

}