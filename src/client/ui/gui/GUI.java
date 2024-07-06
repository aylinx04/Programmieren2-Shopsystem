package src.client.ui.gui;

import src.client.net.ShopClient;
import src.common.IShopVerwaltung;
import src.common.exceptions.LoginFehlgeschlagenException;
import src.common.exceptions.RegistrierenFehlgeschlagenException;
import src.common.Artikel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GUI extends JFrame {
    private IShopVerwaltung SV;
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
            SV = new ShopClient();
        } catch (IOException e){
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabelle();
        einloggenLayout();

        registrierenButton.addActionListener(e -> {
            if (e.getSource() == registrierenButton) {
                registrierenLayout();
                setVisible(true);
            }
        });

        addWindowListener(new FensterSchliesser());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void tabelle() {
        JPanel panel = new JPanel(new BorderLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);

        artikelModel = new ArtikelTabelModel(SV.gibAlleArtikel());
        artikelTabel = new JTable(artikelModel);
        setSpaltenBreite(artikelTabel);

        JScrollPane scrollPane = new JScrollPane(artikelTabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JLabel labelSuche = new JLabel("Suche:");
        suchTextFeld.setPreferredSize(eingabeFeldGroesse);
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

        String[] sortieren = {"Von A-Z", "Von Z-A", "Artikelnummer aufsteigend", "Artikelnummer absteigend", "Preis aufsteigend", "Preis absteigend", "Bestand aufsteigend", "Bestand absteigend"};
        JComboBox<String> sortierAuswahl = new JComboBox<>(sortieren);
        JLabel sortierenLabel = new JLabel("Sortieren nach: ");
        sortierAuswahl.addActionListener(e -> {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String selectedOption = (String) comboBox.getSelectedItem();
            if (selectedOption.equals("Von A-Z")) {
                SV.vonAbisZ();

            } else if (selectedOption.equals("Von Z-A")) {
                SV.vonZbisA();

            } else if (selectedOption.equals("Artikelnummer aufsteigend")) {
                SV.artikelnummerAufsteigend();

            } else if (selectedOption.equals("Artikelnummer absteigend")) {
                SV.artikelnummerAbsteigend();

            } else if (selectedOption.equals("Preis aufsteigend")) {
                SV.preisAufsteigend();

            } else if (selectedOption.equals("Preis absteigend")) {
                SV.preisAbsteigend();

            } else if (selectedOption.equals("Bestand aufsteigend")) {
                SV.bestandAufsteigend();

            } else if (selectedOption.equals("Bestand absteigend")) {
                SV.bestandAbsteigend();

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
            suchErgebnis = SV.sucheArtikel(suchBegriff);
        }
        artikelModel.setArtikel(suchErgebnis);
    }

    private void einloggenLayout() {
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

    private void registrierenLayout() {
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

        jetztRegistrierenButton.addActionListener(this::verarbeiteRegistrierenKlick);

        getContentPane().removeAll();
        add(registrierenPanel, BorderLayout.CENTER);
    }

    private void verarbeiteRegistrierenKlick(ActionEvent e) {
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
        textfieldVorname.setText(null);
        textfieldPasswort.setText(null);
        textfieldPasswort2.setText(null);
        textfieldStrasse.setText(null);
        textfieldPlz.setText(null);
        textfieldWohnort.setText(null);
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

        textfieldName.setText(null);
        passwordfieldPasswort.setText(null);

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

    private void setSpaltenBreite(JTable table) {
        TableColumn column;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(150); // Name
                    break;
                case 1:
                    column.setPreferredWidth(50); // Nummer
                    break;
                case 2:
                    column.setPreferredWidth(50); // Preis
                    break;
                case 3:
                    column.setPreferredWidth(150); // Bestand (Packungsgroesse)
                    break;
                default:
                    column.setPreferredWidth(100);
                    break;
            }
        }
    }
}
