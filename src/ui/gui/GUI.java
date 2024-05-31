package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.LoginFehlgeschlagenException;
import src.valueobjects.Artikel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class GUI extends JFrame {
    private ShopVerwaltungen SV;
    private JTextField textfieldName = new JTextField();
    private JPasswordField passwordfieldPasswort = new JPasswordField();
    private JButton einloggenButton = new JButton("Einloggen");
    private JTextField suchTextFeld = new JTextField();
    private JButton suchenButton = new JButton("Suche");
    private ArtikelTabelModel artikelModel;
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
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void tabelle() {
        artikelModel = new ArtikelTabelModel(SV.gibAlleArtikel());
        artikelTabel = new JTable(artikelModel);
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        JPanel panel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(artikelTabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JLabel labelSuche = new JLabel("Suche:");

        suchTextFeld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aktualisiereSuchergebnisse();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aktualisiereSuchergebnisse();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aktualisiereSuchergebnisse();
            }
        });

        suchTextFeld.setPreferredSize(eingabeFeldGroesse);

        String sotieren[] = {"Von A-Z", "Von Z-A", "Artikelnummer aufsteigend", "Artikelnummer absteigend", "Preis aufsteigend", "Preis absteigend", "Bestand aufsteigend", "Bestand absteigend"};
        JComboBox<String> sortierAuswahl = new JComboBox<>(sotieren);
        JLabel labelsotieren = new JLabel("Sortieren nach: ");
        sortierAuswahl.addActionListener(e -> {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedOption = (String) comboBox.getSelectedItem();
            if (selectedOption.equals("Von A-Z")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getName));

            } else if (selectedOption.equals("Von Z-A")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getName));
                Collections.reverse(SV.gibAlleArtikel());

            } else if (selectedOption.equals("Artikelnummer aufsteigend")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getNummer));

            } else if (selectedOption.equals("Artikelnummer absteigend")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getNummer));
                Collections.reverse(SV.gibAlleArtikel());

            } else if (selectedOption.equals("Preis aufsteigend")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getPreis));

            } else if (selectedOption.equals("Preis absteigend")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getPreis));
                Collections.reverse(SV.gibAlleArtikel());

            } else if (selectedOption.equals("Bestand aufsteigend")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getBestand));

            } else if (selectedOption.equals("Bestand absteigend")) {
                Collections.sort(SV.gibAlleArtikel(), Comparator.comparing(Artikel::getBestand));
                Collections.reverse(SV.gibAlleArtikel());
            }

            artikelModel.setArtikel(SV.gibAlleArtikel());
        });


        JPanel sortierPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortierPanel.add(labelsotieren);
        sortierPanel.add(sortierAuswahl);
        sortierPanel.add(labelSuche);
        sortierPanel.add(suchTextFeld);

        panel.add(sortierPanel, BorderLayout.NORTH);

        add(panel, BorderLayout.CENTER);
    }

    private void aktualisiereSuchergebnisse() {
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
        einloggenButton.addActionListener(this::verarbeiteEinloggenKlick);

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
        GridBagConstraints c = new GridBagConstraints();
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());
        try {
            int zahl = SV.checkLogin(name, passwort);
            if (zahl == 1) {
                JFrame kundenFrame = new EinloggenKunde(SV);
                kundenFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setEnabled(true);
                    }
                });
                setEnabled(false);
                kundenFrame.setVisible(true);
            } else if (zahl == 2) {
                JFrame mitarbeiterFrame = new EinloggenMitarbeiter(SV);
                mitarbeiterFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setEnabled(true);
                    }
                });
                setEnabled(false);
                mitarbeiterFrame.setVisible(true);
            }
        } catch (LoginFehlgeschlagenException e) {
            JOptionPane.showMessageDialog(GUI.this, "Anmeldung Fehlgeschlagen!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new GUI();
    }
}
