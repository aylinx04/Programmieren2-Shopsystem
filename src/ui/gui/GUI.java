package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.LoginFehlgeschlagenException;
import src.valueobjects.Artikel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class GUI extends JFrame {
    private ShopVerwaltungen SV;
    private JTextField textfieldName = new JTextField();
    private JPasswordField passwordfieldPasswort = new JPasswordField();
    private JButton einloggenButton = new JButton("Einloggen");
    private JButton registrierenButton = new JButton("Registrieren");
    private JTextField suchTextFeld = new JTextField();
    private JTextField textfieldVorname = new JTextField();
    private JPasswordField textfieldRPasswort = new JPasswordField();
    private JPasswordField textfieldPasswort2 = new JPasswordField();
    private JTextField textfieldRPLZ = new JTextField();

    private ArtikelTabelModel artikelModel;
    private JTable artikelTabel;
    private EinloggenKunde einloggenKunde;
    private EinloggenMitarbeiter einloggenMitarbeiter;
    private JPanel anfangsKomponente;


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

        registrierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == registrierenButton) {
                    registrierenLayout();
                    setVisible(true);
                }
            }
        });


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

        registrierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == registrierenButton) {
                    for (Component comp : sortierPanel.getComponents()) {
                        comp.setVisible(false);
                    }
                }
            }
        });
        registrierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == registrierenButton) {
                    for (Component comp : panel.getComponents()) {
                        comp.setVisible(false);
                    }
                }
            }
        });
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
        JPanel einloggenPanel = new JPanel();
        einloggenPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        JLabel labelName = new JLabel("Name:");
        c.gridx = 0;
        c.gridy = 0;
        einloggenPanel.add(labelName, c);

        textfieldName.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 0;
        c.gridy = 1;
        einloggenPanel.add(textfieldName, c);

        JLabel labelPasswort = new JLabel("Passwort:");
        c.gridx = 0;
        c.gridy = 2;
        einloggenPanel.add(labelPasswort, c);

        passwordfieldPasswort.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 0;
        c.gridy = 3;
        einloggenPanel.add(passwordfieldPasswort, c);

        einloggenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 4;
        einloggenPanel.add(einloggenButton, c);
        einloggenButton.addActionListener(this::verarbeiteEinloggenKlick);

        registrierenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 5;
        einloggenPanel.add(registrierenButton, c);
        registrierenButton.addActionListener(this::verarbeiteRegistrierenKlick);

        add(einloggenPanel, BorderLayout.WEST);

        registrierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == registrierenButton) {
                    for (Component comp : einloggenPanel.getComponents()) {
                        comp.setVisible(false);
                    }
                }
            }
        });
    }

    private void registrierenLayout(){
        JPanel registrierenPanel = new JPanel();
        registrierenPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        JLabel labelVorname = new JLabel("Name:");
        c.gridx = 0;
        c.gridy = 0;
        registrierenPanel.add(labelVorname, c);

        textfieldVorname.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 0;
        c.gridy = 1;
        registrierenPanel.add(textfieldVorname, c);

        JLabel labelpass = new JLabel("Passwort:");
        c.gridx = 0;
        c.gridy = 2;
        registrierenPanel.add(labelpass, c);

        textfieldRPasswort.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 0;
        c.gridy = 3;
        registrierenPanel.add(textfieldRPasswort, c);

        add(registrierenPanel, BorderLayout.CENTER);

    }


    void verarbeiteRegistrierenKlick(ActionEvent e){
        if (!e.getSource().equals(registrierenButton)) {
            return;
        }

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
                EinloggenKunde einloggenKunde = new EinloggenKunde(this, "Kunden Optionen", true, SV);
                einloggenKunde.setVisible(true);
            } else if(zahl == 2) {
                einloggenMitarbeiter = new EinloggenMitarbeiter(this, "Mitarbeiter Optionene", true, SV);
                einloggenMitarbeiter.setVisible(true);
            }
        } catch (LoginFehlgeschlagenException e) {
            JOptionPane.showMessageDialog(GUI.this, "Anmeldung Fehlgeschlagen!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hideComponents() {
        for (Component comp : anfangsKomponente.getComponents()) {
            comp.setVisible(false);
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}
