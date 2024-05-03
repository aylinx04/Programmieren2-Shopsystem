package src.domain;

import src.valueobjects.Artikel;
import src.valueobjects.Kunde;
import src.valueobjects.Mitarbeiter;

import java.util.ArrayList;
import java.util.List;

import static src.valueobjects.Warenkorb.getWarenkorb;

public class ShopVerwaltungen {
    List<Artikel> ArtikelListe = new ArrayList<>();
    List<Kunde> KundenListe = new ArrayList<>();
    List<Mitarbeiter> MitarbeiterListe = new ArrayList<>();

    public ShopVerwaltungen (){
        ArtikelListe.add(new Artikel("Tomate", 1, 4.99, 28));
        ArtikelListe.add(new Artikel("Gurke", 2,3.59, 17));
        ArtikelListe.add(new Artikel("Reis", 3, 2, 12));
        ArtikelListe.add(new Artikel("Salat", 4,0.99, 6));
        ArtikelListe.add(new Artikel("Fanta", 5, 4, 15));
        KundenListe.add(new Kunde("Peter", 1, "geheim", "Lindenalle 22", "87687", "Bremen"));
        MitarbeiterListe.add(new Mitarbeiter("Helga",6,"auchgeheim"));
    }

    public List<Artikel> getArtikelListe(){
         return ArtikelListe;
     }

    public void setArtikelListe(List<Artikel> artikelListe) {
        ArtikelListe = artikelListe;
    }

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

    public boolean checkPasswort(String passwort, String passwort2){
        return passwort.equals(passwort2);
    }

    public Artikel holeArtikel(String name){
        for (Artikel a : getArtikelListe()){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return getArtikelListe().get(0); //hier eigentlich Fehlerbehandlung
    }
    public boolean checkBestand(int anzahl){
        for (Artikel b : getWarenkorb()){
            if(b.getBestand() >= anzahl){
                return true;
            }
        }
        return false;
    }
    public boolean checkObEsDenArtikelGibt(String name){
        for (Artikel a : getArtikelListe()){
            if(a.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort){
        KundenListe.add(new Kunde(name, KundenListe.size()+1, passwort, strasse, plz, wohnort));
    }

    public void artikelAnlegen(String name, double preis, int bestand){
         ArtikelListe.add(new Artikel(name, ArtikelListe.size()+1, preis, bestand));
    }

    public void mitarbeiterAnlegen(String name, String passwort) {
        MitarbeiterListe.add(new Mitarbeiter(name, MitarbeiterListe.size()+1, passwort));
    }
}
