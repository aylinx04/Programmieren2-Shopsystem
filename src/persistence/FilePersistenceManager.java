package src.persistence;

import src.valueobjects.Artikel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FilePersistenceManager implements PersistenceManager{
    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public List<Artikel> leseArtikelListe(String datei) throws IOException{
        reader = new BufferedReader(new FileReader(datei));
        List<Artikel> artikelBestand = new ArrayList<>();
        Artikel einArtikel;
        do {
        	einArtikel = ladeArtikel();
        if (einArtikel != null) {

            artikelBestand.add(einArtikel);
        }
    } while (einArtikel != null);

		return artikelBestand;
    }

    public void schreibeArtikelListe(List<Artikel> ArtikelListe, String datei) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));

        for(Artikel a : ArtikelListe)
            speicherArtikel(a);

        writer.close();
    }

    private Artikel ladeArtikel() throws IOException {
        String name = liesZeile();
        if (name == null) {
            return null;
        }
        String nummerString = liesZeile();
        int nummer = Integer.parseInt(nummerString);
        String preisString = liesZeile();
        double preis = Double.parseDouble(preisString);
        String bestandString = liesZeile();
        int bestand = Integer.parseInt(bestandString);

        return new Artikel(name, nummer, preis, bestand);
    }

    private boolean speicherArtikel(Artikel a) throws IOException {
        schreibeZeile(a.getName());
        schreibeZeile(String.valueOf(a.getNummer()));
        schreibeZeile(String.valueOf(a.getPreis()));
        schreibeZeile(String.valueOf(a.getBestand()));
        return true;
    }

    private String liesZeile() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    private void schreibeZeile(String daten) {
        if (writer != null)
            writer.println(daten);
    }

}
