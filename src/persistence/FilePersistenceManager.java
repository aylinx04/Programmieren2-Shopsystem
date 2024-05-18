package src.persistence;

import src.valueobjects.Artikel;
import src.valueobjects.Mitarbeiter;

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

    private void speicherArtikel(Artikel a) {
        schreibeZeile(a.getName());
        schreibeZeile(String.valueOf(a.getNummer()));
        schreibeZeile(String.valueOf(a.getPreis()));
        schreibeZeile(String.valueOf(a.getBestand()));
    }

    public List<Mitarbeiter> leseMitarbeiterListe(String datei) throws IOException{
        reader = new BufferedReader(new FileReader(datei));
        List<Mitarbeiter> mitarbeiterHinzu = new ArrayList<>();
        Mitarbeiter einMitarbeiter;
        do {
            einMitarbeiter = ladeMitarbeiter();
            if (einMitarbeiter != null){
                mitarbeiterHinzu.add(einMitarbeiter);
            }
        } while (einMitarbeiter != null);
        return mitarbeiterHinzu;
    }

    public void schreibeMitarbeiterListe(List<Mitarbeiter> MitarbeiterListe, String datei) throws IOException{
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));

        for(Mitarbeiter m : MitarbeiterListe)
            speicherMitarbeiter(m);

        writer.close();
    }

    private Mitarbeiter ladeMitarbeiter() throws IOException{
        String name = liesZeile();
        if (name == null) {
            return null;
        }
        int nummer = Integer.parseInt(liesZeile());
        String passwort = liesZeile();
        return new Mitarbeiter(name, nummer, passwort);
    }

    public void speicherMitarbeiter(Mitarbeiter m) {
        schreibeZeile(m.getName());
        schreibeZeile(String.valueOf(m.getNummer()));
        schreibeZeile(m.getPasswort());
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
