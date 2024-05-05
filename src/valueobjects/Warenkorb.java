package src.valueobjects;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
    Map<String, Artikel> warenkorb = new HashMap<>();

    public Warenkorb(){
        warenkorb.put("Brot", new Artikel("Brot", 7, 2.99, 22));
    }

    public Map<String, Artikel> getWarenkorb() {return warenkorb;}

    public void artikelHinzufuegen(Artikel artikel){
        if(warenkorb.containsKey(artikel.getName())){
            Artikel vorhandenerArtikel = warenkorb.get(artikel.getName());
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() + artikel.getBestand());
        }else{
            warenkorb.put(artikel.getName(), artikel);
        }
        Rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
    }

    public void warenkorbLeeren(){
        warenkorb.clear();
    }

}