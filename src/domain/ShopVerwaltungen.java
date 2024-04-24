package src.domain;

import src.valueobjects.Artikel;
import src.valueobjects.Kunde;
import src.valueobjects.Mitarbeiter;

import java.util.ArrayList;
import java.util.List;

public class ShopVerwaltungen {
    List<Artikel> ArtikelListe = new ArrayList<>();
    List<Kunde> KundeListe = new ArrayList<>();
    List<Mitarbeiter> MitarbeiterListe = new ArrayList<>();

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

     public void checkLogin(String name, String passwort){

         }
     }
}
