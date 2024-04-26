package src.domain;

import src.valueobjects.Artikel;
import src.valueobjects.Kunde;
import src.valueobjects.Mitarbeiter;

import java.util.ArrayList;
import java.util.List;

public class ShopVerwaltungen {
    List<Artikel> ArtikelListe = new ArrayList<>();
    List<Artikel> WK = new ArrayList<>();
    List<Kunde> KundenListe = new ArrayList<>();
    List<Mitarbeiter> MitarbeiterListe = new ArrayList<>();

     public ShopVerwaltungen (){
         ArtikelListe.add(new Artikel("Tomate", 1, "4,99€"));
         ArtikelListe.add(new Artikel("Gurke", 2,"3,59€"));
         ArtikelListe.add(new Artikel("Reis", 3, "2€"));
         ArtikelListe.add(new Artikel("Salat", 4,"0,99€"));
         ArtikelListe.add(new Artikel("Fanta", 5, "4€"));
         KundenListe.add(new Kunde("Peter", 1, "geheim"));
         MitarbeiterListe.add(new Mitarbeiter("Helga","6","auchgeheim"));
     }

     public List<Artikel> getArtikelListe(){
         return ArtikelListe;
     }

    public void setArtikelListe(List<Artikel> artikelListe) {
        ArtikelListe = artikelListe;
    }

    public List<Artikel> getWK() {return WK;}

     public boolean checkLoginKunde(String name, String passwort){
         for(Kunde u : KundenListe){
             if(u.getName().equals(name) && u.getPasswort().equals(passwort)){
                 return true;
             }

         }
         return false;
     }

    public boolean checkLoginMitarbeiter(String name, String passwort){
        for(Mitarbeiter m : MitarbeiterListe){
            if(m.getName().equals(name) && m.getPasswort().equals(passwort)){
                return true;
            }

        }
        return false;
    }
}
