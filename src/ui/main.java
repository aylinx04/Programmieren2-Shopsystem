package src.ui;

import src.domain.ShopVerwaltungen;
import src.valueobjects.Artikel;

import java.util.List;

public class main {

    private static ShopVerwaltungen SV = new ShopVerwaltungen();


    public static void main (String[]args){
    List<Artikel> artikelListe = SV.getArtikelListe();

    for(int i = 0; i < artikelListe.size(); i++){
        Artikel a = artikelListe.get(i);

        System.out.println("Aktueller Artikel: " + a.getName());
    }


    }
}
