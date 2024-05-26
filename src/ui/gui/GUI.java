package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.valueobjects.Artikel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI extends JFrame {
    private ShopVerwaltungen SV;
    private JTextField textfieldNummer = new JTextField();
    private JTextField textfieldTitel = new JTextField();
    private JButton hinzufuegenButton = new JButton("Hinzufügen");
    private JTextField suchTextFeld = new JTextField();
    private JButton suchenButton = new JButton("Suche");
    JList artikelListe = new JList();
    private ArtikelTabelModel artikelModel;
    private JTable artikelTabel;


    public GUI(String titel, ShopVerwaltungen SV) {
        super(titel);
        this.SV = SV;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(300, 200);
        setLocation(0, 500);

        artikelModel = new ArtikelTabelModel(SV.gibAlleArtikel());
        artikelTabel = new JTable(artikelModel);

        JScrollPane scrollPane = new JScrollPane(artikelTabel);
        add(scrollPane, BorderLayout.CENTER);

        String suchBegriff = suchTextFeld.getText();
        java.util.List<Artikel> suchErgebnis;
        if (suchBegriff.isEmpty()) {
            suchErgebnis = SV.gibAlleArtikel();
        } else {
            suchErgebnis = SV.sucheNachTitel(suchBegriff);
        }
        artikelModel.setArtikel(suchErgebnis);
        setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        try {
            String dateiPfad = "Shop";
            ShopVerwaltungen shopVerwaltungen = new ShopVerwaltungen(dateiPfad);
            GUI gui = new GUI("E-Shop", shopVerwaltungen);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
