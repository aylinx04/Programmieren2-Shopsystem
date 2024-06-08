package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.LoginFehlgeschlagenException;
import src.domain.exceptions.RegistrierenFehlgeschlagenException;
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
    private JButton jetztRegistrierenButton = new JButton("Registrieren");
    private JTextField suchTextFeld = new JTextField();
    private JTextField textfieldVorname = new JTextField();
    private JPasswordField textfieldPasswort = new JPasswordField();
    private JPasswordField textfieldPasswort2 = new JPasswordField();
    private JTextField textfieldStrasse = new JTextField();
    private JTextField textfieldPlz = new JTextField();
    private JTextField textfieldWohnort = new JTextField();
    private ArtikelTabelModel artikelModel;
    private JTable artikelTabel;
    private JPanel registrierenPanel;


    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        super("E-Shop");
        try{
            SV = new ShopVerwaltungen("Shop");
        } catch (IOException e){
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabelle();
        einloggenLayout();

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

        String[] sortieren = {"Von A-Z", "Von Z-A", "Artikelnummer aufsteigend", "Artikelnummer absteigend", "Preis aufsteigend", "Preis absteigend", "Bestand aufsteigend", "Bestand absteigend"};
        JComboBox<String> sortierAuswahl = new JComboBox<>(sortieren);
        JLabel sortierenLabel = new JLabel("Sortieren nach: ");
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
        sortierPanel.add(sortierenLabel);
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

    private void einloggenLayout(){
        JPanel einloggenPanel = new JPanel();
        einloggenPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        addComponent(einloggenPanel, new JLabel("Name:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(einloggenPanel, textfieldName, 0, 1, eingabeFeldGroesse, c);
        addComponent(einloggenPanel, new JLabel("Passwort:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(einloggenPanel, passwordfieldPasswort, 0, 3, eingabeFeldGroesse, c);

        c.weighty = 1.0;
        addComponent(einloggenPanel, einloggenButton, 0, 4, eingabeFeldGroesse, c);
        einloggenButton.addActionListener(this::verarbeiteEinloggenKlick);
        addComponent(einloggenPanel, registrierenButton, 0, 5, eingabeFeldGroesse, c);
        registrierenButton.addActionListener(this::verarbeiteRegistrierenKlick);

        add(einloggenPanel, BorderLayout.WEST);
    }

    private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, Dimension groesse, GridBagConstraints c) {
        component.setPreferredSize(groesse);
        c.gridx = gridx;
        c.gridy = gridy;
        panel.add(component, c);
    }

    private void registrierenLayout(){
        registrierenPanel = new JPanel();
        registrierenPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;

        addComponent(registrierenPanel, new JLabel("Name:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, textfieldVorname, 1, 0, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, new JLabel("Passwort:"), 0, 1, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, textfieldPasswort, 1, 1, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, new JLabel("Passwort:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, textfieldPasswort2, 1, 2, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, new JLabel("Strasse:"), 0, 3, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, textfieldStrasse, 1, 3, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, new JLabel("PLZ:"), 0, 4, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, textfieldPlz, 1, 4, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, new JLabel("Wohnort:"), 0, 5, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, textfieldWohnort, 1, 5, eingabeFeldGroesse, c);
        addComponent(registrierenPanel, jetztRegistrierenButton, 0, 6, eingabeFeldGroesse, c);

        jetztRegistrierenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteRegistrierenKlick(e);
            }
        });

        getContentPane().removeAll();
        add(registrierenPanel, BorderLayout.CENTER);
    }

    private void verarbeiteRegistrierenKlick(ActionEvent e){
        if(!e.getSource().equals(jetztRegistrierenButton))
            return;
        if(textfieldVorname.getText().isEmpty() ||
                textfieldPasswort.getPassword().length == 0 ||
                textfieldPasswort2.getPassword().length == 0 ||
                textfieldStrasse.getText().isEmpty() ||
                textfieldPlz.getText().isEmpty() ||
                textfieldWohnort.getText().isEmpty()) {
            JOptionPane.showMessageDialog(GUI.this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = textfieldVorname.getText();
        String passwort = String.valueOf(textfieldPasswort.getPassword());
        String passwort2 = String.valueOf(textfieldPasswort2.getPassword());
        String strasse = textfieldStrasse.getText();
        String plz = textfieldPlz.getText();
        String wohnort = textfieldWohnort.getText();
        try {
            SV.checkPasswort(passwort, passwort2);
            SV.kundeAnlegen(name, passwort, strasse, plz, wohnort);
            JOptionPane.showMessageDialog(GUI.this, "Registrierung erfolgreich!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            if (registrierenPanel != null) {
                registrierenPanel.setVisible(false);
            }
            einloggenLayout();
            tabelle();
            einloggen(name, passwort);
        } catch (RegistrierenFehlgeschlagenException ex) {
            JOptionPane.showMessageDialog(GUI.this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verarbeiteEinloggenKlick(ActionEvent e) {
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
                new EinloggenKunde(this, "Kunden Optionen", true, SV);
            } else if(zahl == 2) {
                new EinloggenMitarbeiter(this, "Mitarbeiter Optionen", true, SV);
            }
        } catch (LoginFehlgeschlagenException e) {
            JOptionPane.showMessageDialog(GUI.this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

}
