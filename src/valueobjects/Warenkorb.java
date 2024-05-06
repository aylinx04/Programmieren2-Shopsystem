package src.valueobjects;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
    Map<String, Artikel> warenkorb = new HashMap<>();

    public Warenkorb(){
        warenkorb.put("Brot", new Artikel("Brot", 7, 2.99, 22));
    }

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
        Rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
    }

    public void artikelEntfernen(String artikelname, int anzahl) {
        Artikel vorhandenerArtikel = warenkorb.get(artikelname);
        if (vorhandenerArtikel.getBestand() > anzahl) {
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() - anzahl);
        } else {
            warenkorb.remove(artikelname);
        }
        Rechnung.gesamtpreisVerringern(vorhandenerArtikel.getPreis() * anzahl);
    }

    public void derKauf() {


        LocalDate Datum = LocalDate.now();
        System.out.println(Datum);
        System.out.println(" ");

        double gesamtPreis = 0.0;
        System.out.println("Dein Einkauf: ");
        for (Artikel a : warenkorb.values()){
            System.out.println(a);
            gesamtPreis += (a.getPreis() * a.getBestand());
        }

        System.out.println("Gesamtpreis: " + gesamtPreis + "€");
    }


    public void warenkorbLeeren(){
        warenkorb.clear();
    }

}