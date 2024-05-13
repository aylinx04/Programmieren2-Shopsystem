package src.valueobjects;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
    Map<String, Artikel> warenkorb = new HashMap<>();

    public Map<String, Artikel> getWarenkorb() {return warenkorb;}

    public boolean istArtikelImWarenkorb(String artikelName) {
        return warenkorb.containsKey(artikelName);
    }

    public boolean checkAnzahlDesArtikels(int anzahl, String artikelName) {
        if (warenkorb.containsKey(artikelName)) {
            Artikel artikel = warenkorb.get(artikelName);
            return artikel.getBestand() >= anzahl;
        } else {
            return false;
        }
    }

    public void artikelHinzufuegen(Artikel artikel){
        if(warenkorb.containsKey(artikel.getName())){
            Artikel vorhandenerArtikel = warenkorb.get(artikel.getName());
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() + artikel.getBestand());
        }else{
            warenkorb.put(artikel.getName(), artikel);
        }
      //  Rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
    }

    public void artikelEntfernen(String artikelname, int anzahl) {
        Artikel vorhandenerArtikel = warenkorb.get(artikelname);
        if (vorhandenerArtikel.getBestand() > anzahl) {
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() - anzahl);
        } else {
            warenkorb.remove(artikelname);
        }
   //     Rechnung.gesamtpreisVerringern(vorhandenerArtikel.getPreis() * anzahl);
    }

    public void warenkorbLeeren(){
        warenkorb.clear();
    }

}