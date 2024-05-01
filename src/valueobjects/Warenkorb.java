package src.valueobjects;

import src.domain.ShopVerwaltungen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Warenkorb {
    private static ShopVerwaltungen SV = new ShopVerwaltungen();
    static List<Artikel> warenkorb = new ArrayList<>();

    public Warenkorb(){
        warenkorb.add(new Artikel("Brot", 7, "2,99€", 22));
    }

    public static List<Artikel> getWarenkorb() {return warenkorb;}

    public boolean artikelHinzufuegen(String name){
        for (Artikel a : SV.getArtikelListe()){
            if(a.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

}