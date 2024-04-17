package src.domain;

import src.valueobjects.Artikel;

import java.util.ArrayList;
import java.util.List;

public class ShopVerwaltungen {
    List<Artikel> ArtikelListe = new ArrayList<>();
     public ShopVerwaltungen (){
         ArtikelListe.add(new Artikel("Tomate", 1));
         ArtikelListe.add(new Artikel("Gurke", 2));
         ArtikelListe.add(new Artikel("Reis", 3));
         ArtikelListe.add(new Artikel("Salat", 4));
         ArtikelListe.add(new Artikel("Fanta", 5));
     }

     public List<Artikel> getArtikelListe(){
         return ArtikelListe;
     }
}
