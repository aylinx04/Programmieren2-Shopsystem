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

    public void artikelHinzufuegen(Artikel artikel){
        if(warenkorb.containsKey(artikel.getName())){
            Artikel vorhandenerArtikel = warenkorb.get(artikel.getName());
            vorhandenerArtikel.setBestand(vorhandenerArtikel.getBestand() + artikel.getBestand());
        }else{
            warenkorb.put(artikel.getName(), artikel);
        }
        Rechnung.gesamtpreisErhoehen(artikel.getPreis() * artikel.getBestand());
    }
    public void artikelEntfernen(String entfernen) {
        Artikel entfernterArtikel = warenkorb.remove(entfernen);
        if (entfernterArtikel != null) {
            System.out.println("Der Artikel '" + entfernen + "' wurde erfolgreich entfernt.");
        } else {
            System.err.println("Es wurde kein Artikel mit dem Namen '" + entfernen + "' gefunden.");
        }
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