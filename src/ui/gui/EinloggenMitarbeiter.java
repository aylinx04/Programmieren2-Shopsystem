package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.ArtikelExistiertBereitsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EinloggenMitarbeiter extends JFrame{
    private ShopVerwaltungen SV;
    private JButton artikelHinzufügenButton = new JButton("Hinzufügen");
    private JTextField artikeltextField = new JTextField();
    private JTextField preistextField = new JTextField();
    private JTextField bestandtextField = new JTextField();
    private JRadioButton artikelTyp = new JRadioButton();
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton bestandErhöhenButton = new JButton("Bestand");
    private JButton mitarbeiterHinzufügenButton = new JButton("Mitarbeiter Hinzufügen");
    private JButton ereignisListeButton = new JButton("Ereignisse");
    private JButton ausloggenButton = new JButton("Ausloggen");

    public EinloggenMitarbeiter(ShopVerwaltungen SV) {
        this.SV = SV;
        buttonsLayoutMitarbeiter();
        setSize(640, 480);
        setLocation(0, 500);
        setVisible(true);
    }

    public void buttonsLayoutMitarbeiter(){
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;


        artikelListeButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        westPanel.add(artikelListeButton, c);

        artikelHinzufügenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        westPanel.add(artikelHinzufügenButton, c);

        bestandErhöhenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 2;
        c.gridy = 0;
        westPanel.add(bestandErhöhenButton, c);

        mitarbeiterHinzufügenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 3;
        c.gridy = 0;
        westPanel.add(mitarbeiterHinzufügenButton, c);

        ereignisListeButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 4;
        c.gridy = 0;
        westPanel.add(ereignisListeButton, c);

        ausloggenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 5;
        c.gridy = 0;
        westPanel.add(ausloggenButton, c);
//        artikelListeButton.addActionListener(dasEvent -> verarbeiteEinloggenKlick(dasEvent));

//        if(!e.getSource().equals(artikelHinzufügenButton)){
//            return;
//        JLabel labelName = new JLabel("Artikelname:");
//        c.gridx = 0;
//        c.gridy = 0;
//        westPanel.add(labelName, c);
//
//        artikeltextField.setPreferredSize(eingabeFeldGroesse);
//        c.gridx = 0;
//        c.gridy = 1;
//        westPanel.add(artikeltextField, c);
//        add(westPanel, BorderLayout.NORTH);
//        }
    }

    void verarbeiteArtikelHinzuKlick(ActionEvent e) throws ArtikelExistiertBereitsException {
        if(!e.getSource().equals(artikelHinzufügenButton))
            return;

        String artikel = artikeltextField.getText();
        double preis = Double.parseDouble(preistextField.getText());
        int bestand = Integer.parseInt(bestandtextField.getText());


        SV.artikelAnlegen(artikel, preis, bestand);
    }


























//    void klickVerarbeiten(ActionEvent e, String line){
//
//        switch (line){
//            case artikelHinzufügenButton:
//                if(!e.getSource().equals(artikelHinzufügenButton))
//                    return;
//
//                String artikel = artikeltextField.getText();
//                String preis = preistextField.getText();
//                String bestand = bestandtextField.getText();
//
//
//                einloggen(name, passwort);
//        }
//    }
}
