package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.LoginFehlgeschlagenException;
import src.valueobjects.Artikel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GUI extends JFrame {
    private ShopVerwaltungen SV;
    private JTextField textfieldName = new JTextField();
    private JPasswordField passwordfieldPasswort = new JPasswordField();
    private JButton einloggenButton = new JButton("Einloggen");
    private JTextField suchTextFeld = new JTextField();
    private JButton suchenButton = new JButton("Suche");
    private ArtikelTabelModel artikelModel;
    private EinloggenMitarbeiter einloggenMitarbeiter;
    private EinloggenKunde einloggenKunde;
    private JTable artikelTabel;


    public GUI() {
        super("E-Shop");
        try{
            SV = new ShopVerwaltungen("Shop");
        } catch (IOException e){
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        tabelle();
        layoutEinloggen();

        addWindowListener(new FensterSchliesser());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(640, 480);
        setLocation(0, 500);
        setVisible(true);
    }

    private void tabelle(){
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
    }
    private void layoutEinloggen(){
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        JLabel labelName = new JLabel("Name:");
        c.gridx = 0;
        c.gridy = 0;
        westPanel.add(labelName, c);

        textfieldName.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 0;
        c.gridy = 1;
        westPanel.add(textfieldName, c);

        JLabel labelPasswort = new JLabel("Passwort:");
        c.gridx = 0;
        c.gridy = 2;
        westPanel.add(labelPasswort, c);

        passwordfieldPasswort.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 0;
        c.gridy = 3;
        westPanel.add(passwordfieldPasswort, c);

        einloggenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 4;
        westPanel.add(einloggenButton, c);
        einloggenButton.addActionListener(dasEvent -> verarbeiteEinloggenKlick(dasEvent));

        add(westPanel, BorderLayout.WEST);


    }
    void verarbeiteEinloggenKlick(ActionEvent e) {
        if(!e.getSource().equals(einloggenButton))
            return;

        String name = textfieldName.getText();
        String passwort = String.valueOf(passwordfieldPasswort.getPassword());

        einloggen(name, passwort);
    }

    private void einloggen(String name, String passwort) {
        try {
            int zahl = SV.checkLogin(name, passwort);
            if(zahl == 1){
                einloggenKunde = new EinloggenKunde();
                einloggenKunde.setVisible(true);
            } else if(zahl == 2) {
                einloggenMitarbeiter = new EinloggenMitarbeiter();
                einloggenMitarbeiter.setVisible(true);
            }
        } catch (LoginFehlgeschlagenException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}
