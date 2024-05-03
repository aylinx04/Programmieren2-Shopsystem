package src.valueobjects;

import src.domain.ShopVerwaltungen;

import java.util.ArrayList;
import java.util.List;

public class Warenkorb {
    private static ShopVerwaltungen SV = new ShopVerwaltungen();
    static List<Artikel> warenkorb = new ArrayList<>();

    public Warenkorb(){
        warenkorb.add(new Artikel("Brot", 7, 2.99, 22));
    }

    public static List<Artikel> getWarenkorb() {return warenkorb;}

    public void artikelHinzufuegen(Artikel artikel){
        warenkorb.add(artikel);
    }

}