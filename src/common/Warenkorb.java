package src.common;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
    Map<String, Artikel> warenkorb = new HashMap<>();

    public Map<String, Artikel> getWarenkorb() { return warenkorb; }

    public void artikelEntfernen(String artikelname, int anzahl) {
        Artikel vorhandenerArtikel = warenkorb.get(artikelname);
        if (vorhandenerArtikel.getBestand() > anzahl) {
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() - anzahl);
        } else {
            warenkorb.remove(artikelname);
        }
    }

    public void artikelHinzufuegen(Artikel artikel) {
        if(warenkorb.containsKey(artikel.getName())) {
            Artikel vorhandenerArtikel = warenkorb.get(artikel.getName());
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() + artikel.getBestand());
        }else{
            warenkorb.put(artikel.getName(), artikel);
        }
    }

    public void warenkorbLeeren(){
        warenkorb.clear();
    }

}