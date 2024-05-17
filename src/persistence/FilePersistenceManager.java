package src.persistence;

import src.valueobjects.Artikel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//public class FilePersistenceManager implements PersistenceManager{
//    private BufferedReader reader = null;
//    private PrintWriter writer = null;
//
//    public List<Artikel> LeseArtikel(String datei) throws IOException{
//        reader = new BufferedReader(new FileReader(datei));
//
//        List<Artikel> artikelBestand = new ArrayList<>();
//        Artikel einArtikel;
//        do {
//            einArtikel =
//        }
//    }
//
//    public void speicherArtikel(List<Artikel> ArtikelListe, String datei) throws IOException {
//        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
//
//        for(Artikel a : ArtikelListe)
//            speicherArtikel(a);
//
//        writer.close();
//    }
//
//}
