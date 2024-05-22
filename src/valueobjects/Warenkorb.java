package src.valueobjects;

import src.domain.exceptions.ArtikelNichtGefundenException;
import src.domain.exceptions.BestandNichtVorhandenException;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
    Map<String, Artikel> warenkorb = new HashMap<>();

    public Map<String, Artikel> getWarenkorb() {return warenkorb;}

    public void istArtikelImWarenkorb(String artikelName) throws ArtikelNichtGefundenException {
        if (warenkorb.containsKey(artikelName)) {
        } else {
            throw new ArtikelNichtGefundenException(artikelName);
        }
    }

    public void checkAnzahlDesArtikels(int anzahl, String artikelname) throws BestandNichtVorhandenException {
        Artikel artikel = warenkorb.get(artikelname);
        if (artikel.getBestand() >= anzahl) {
            artikelEntfernen(artikelname, anzahl);
        } else {
            throw new BestandNichtVorhandenException();
        }
    }

    public void artikelEntfernen(String artikelname, int anzahl) {
        Artikel vorhandenerArtikel = warenkorb.get(artikelname);
        if (vorhandenerArtikel.getBestand() > anzahl) {
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() - anzahl);
        } else {
            warenkorb.remove(artikelname);
        }
    }

    public void artikelHinzufuegen(Artikel artikel){
        if(warenkorb.containsKey(artikel.getName())){
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