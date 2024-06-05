package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.ArtikelExistiertBereitsException;
import src.domain.exceptions.ArtikelNichtGefundenException;
import src.domain.exceptions.PackungsgroesseException;
import src.domain.exceptions.RegistrierenFehlgeschlagenException;
import src.valueobjects.Artikel;
import src.valueobjects.Ereignis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class EinloggenMitarbeiter extends JDialog {
    private ShopVerwaltungen SV;
    private JTextField mitarbeiterName = new JTextField();
    private JPasswordField passwort = new JPasswordField();
    private JPasswordField passwort1 = new JPasswordField();
    private JTextField artikeltextField = new JTextField();
    private JTextField preistextField = new JTextField();
    private JTextField bestandtextField = new JTextField();
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton artikelHinzufuegenButton = new JButton("Hinzufügen");
    private JButton bestandErhoehenButton = new JButton("Bestand");
    private JTextField packungsgroesseTextField = new JTextField();
    private ButtonGroup massengutGroup = new ButtonGroup();
    private JRadioButton massengutJa = new JRadioButton("Ja");
    private JRadioButton massengutNein = new JRadioButton("Nein");
    private JButton mitarbeiterHinzufuegenButton = new JButton("Mitarbeiter hinzufügen");
    private JButton ereignisListeButton = new JButton("Ereignisse");
    private JButton ausloggenButton = new JButton("Ausloggen");
    private JPanel mitarbeiterPanel = new JPanel(new GridBagLayout());
    private JPanel mitarbeiterHinzuPanel = new JPanel();
    private ArtikelTabelModel artikelModel;
    private JTable artikelTabel;
    private EreignisTabelModel ereignisModel;
    private JTable ereignisTabel;

    private GridBagConstraints c = new GridBagConstraints();

    public EinloggenMitarbeiter(JFrame parent, String title, boolean modal, ShopVerwaltungen SV) {
        super(parent, title, modal);
        this.SV = SV;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        buttonsLayoutMitarbeiter();
        tabelle();
        setSize(640, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void buttonsLayoutMitarbeiter() {
        Dimension eingabeFeldGroesse = new Dimension(140, 30);
        c.anchor = GridBagConstraints.NORTH;

        addComponent(mitarbeiterPanel, artikelListeButton, 0, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterPanel, artikelHinzufuegenButton, 1, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterPanel, bestandErhoehenButton, 2, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterPanel, mitarbeiterHinzufuegenButton, 3, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterPanel, ereignisListeButton, 4, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterPanel, ausloggenButton, 5, 0, eingabeFeldGroesse, c);

        artikelListeButton.addActionListener(new ButtonActionListener());
        artikelHinzufuegenButton.addActionListener(new ButtonActionListener());
        bestandErhoehenButton.addActionListener(new ButtonActionListener());
        mitarbeiterHinzufuegenButton.addActionListener(new ButtonActionListener());
        ereignisListeButton.addActionListener(new ButtonActionListener());

        add(mitarbeiterPanel, BorderLayout.NORTH);

        ausloggenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int antwort = JOptionPane.showConfirmDialog(EinloggenMitarbeiter.this, "Möchten Sie sich wirklich abmelden?", "Abmelden", JOptionPane.YES_NO_OPTION);
                if (antwort == JOptionPane.YES_OPTION) {
                    dispose();
                    try {
                        SV.schreibeArtikelDaten("Shop_A.txt");
                        SV.schreibeMitarbeiterDaten("Shop_M.txt");
                        SV.schreibeEreignisDaten("Shop_E.txt");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, Dimension groesse, GridBagConstraints c) {
        component.setPreferredSize(groesse);
        c.gridx = gridx;
        c.gridy = gridy;
        panel.add(component, c);
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();

            switch (source.getText()) {
                case "Artikelliste":
                    tabelle();
                    break;
                case "Hinzufügen":
                    artikelHinzuLayout();
                    break;
                case "Bestand":
                    bestandLayout();
                    break;
                case "Mitarbeiter hinzufügen":
                    mitarbeiterHinzuLayout();
                    break;
                case "Ereignisse":
                    tabelleEreignisse();
                    break;
                default:
                    break;
            }
        }
    }

    private void artikelHinzuLayout() {
        JPanel artikelHinzuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Dimension eingabeFeldGroesse = new Dimension(140, 30);
        c.anchor = GridBagConstraints.NORTH;

        addComponent(artikelHinzuPanel, new JLabel("Artikelname:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, artikeltextField, 1, 0, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, new JLabel("Preis:"), 0, 1, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, preistextField, 1, 1, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, new JLabel("Bestand:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, bestandtextField, 1, 2, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, new JLabel("Massengutartikel:"), 0, 3, eingabeFeldGroesse, c);

        massengutGroup.add(massengutJa);
        massengutGroup.add(massengutNein);
        addComponent(artikelHinzuPanel, massengutJa, 1, 3, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, massengutNein, 2, 3, eingabeFeldGroesse, c);

        JLabel labelPackungsgroesse = new JLabel("Packungsgröße:");
        addComponent(artikelHinzuPanel, labelPackungsgroesse, 0, 4, eingabeFeldGroesse, c);
        addComponent(artikelHinzuPanel, packungsgroesseTextField, 1, 4, eingabeFeldGroesse, c);

        packungsgroesseTextField.setVisible(false);
        labelPackungsgroesse.setVisible(false);

        massengutJa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                packungsgroesseTextField.setVisible(true);
                labelPackungsgroesse.setVisible(true);
                revalidate();
                repaint();
            }
        });

        massengutNein.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                packungsgroesseTextField.setVisible(false);
                labelPackungsgroesse.setVisible(false);
                revalidate();
                repaint();
            }
        });

        JButton speichernButton = new JButton("Speichern");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        artikelHinzuPanel.add(speichernButton, c);

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    verarbeiteArtikelHinzuKlick();
                } catch (ArtikelExistiertBereitsException ex) {
                    JOptionPane.showMessageDialog(EinloggenMitarbeiter.this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getContentPane().removeAll();
        add(mitarbeiterPanel, BorderLayout.NORTH);
        add(artikelHinzuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void bestandLayout() {
        JPanel bestandPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Dimension eingabeFeldGroesse = new Dimension(140, 30);
        c.anchor = GridBagConstraints.NORTH;

        addComponent(bestandPanel, new JLabel("Artikelname:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(bestandPanel, artikeltextField, 1, 0, eingabeFeldGroesse, c);
        addComponent(bestandPanel, new JLabel("Bestand:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(bestandPanel, bestandtextField, 1, 2, eingabeFeldGroesse, c);

        JButton speichernButton = new JButton("Speichern");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        bestandPanel.add(speichernButton, c);

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    verarbeiteBestandKlick();
                } catch (ArtikelNichtGefundenException | PackungsgroesseException ex) {
                    JOptionPane.showMessageDialog(EinloggenMitarbeiter.this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getContentPane().removeAll();
        add(mitarbeiterPanel, BorderLayout.NORTH);
        add(bestandPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mitarbeiterHinzuLayout() {
        JPanel mitarbeiterHinzuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Dimension eingabeFeldGroesse = new Dimension(140, 30);
        c.anchor = GridBagConstraints.NORTH;

        addComponent(mitarbeiterHinzuPanel, new JLabel("Name:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterHinzuPanel, mitarbeiterName, 1, 0, eingabeFeldGroesse, c);
        addComponent(mitarbeiterHinzuPanel, new JLabel("Passwort:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(mitarbeiterHinzuPanel, passwort, 1, 2, eingabeFeldGroesse, c);
        addComponent(mitarbeiterHinzuPanel, new JLabel("Passwort wiederholen:"), 0, 3, eingabeFeldGroesse, c);
        addComponent(mitarbeiterHinzuPanel, passwort1, 1, 3, eingabeFeldGroesse, c);

        JButton hinzuButton = new JButton("Hinzufügen");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        mitarbeiterHinzuPanel.add(hinzuButton, c);

        hinzuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verarbeiteMitarbeiterKlick();
            }
        });

        getContentPane().removeAll();
        add(mitarbeiterPanel, BorderLayout.NORTH);
        add(mitarbeiterHinzuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    void verarbeiteArtikelHinzuKlick() throws ArtikelExistiertBereitsException {
        try {
            if (artikeltextField.getText().isEmpty() ||
                    Double.parseDouble(preistextField.getText()) == 0 ||
                    Integer.parseInt(bestandtextField.getText()) == 0 ||
                    (!massengutJa.isSelected()) && !massengutNein.isSelected()) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String artikel = artikeltextField.getText();
            double preis = Double.parseDouble(preistextField.getText());
            int bestand = Integer.parseInt(bestandtextField.getText());

            if (massengutJa.isSelected()) {
                int packungsgroesse = Integer.parseInt(packungsgroesseTextField.getText());
                if (Integer.parseInt(packungsgroesseTextField.getText()) == 0) {
                    JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SV.checkPackungsgroesse(packungsgroesse, bestand);
                SV.artikelAnlegen(artikel, preis, bestand, packungsgroesse);
                JOptionPane.showMessageDialog(this, "Artikel erfolgreich angelegt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            } else {
                SV.artikelAnlegen(artikel, preis, bestand);
                JOptionPane.showMessageDialog(this, "Artikel erfolgreich angelegt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie gültige Zahlenwerte ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (PackungsgroesseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    void verarbeiteBestandKlick() throws ArtikelNichtGefundenException, PackungsgroesseException {
        try {
            if (artikeltextField.getText().isEmpty() ||
                    Integer.parseInt(bestandtextField.getText()) == 0) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String artikelname = artikeltextField.getText();
            int bestand = Integer.parseInt(bestandtextField.getText());
            Artikel artikel = SV.holeArtikel(artikelname);
            SV.ereignisBestandErhoeht(artikel, bestand);

            JOptionPane.showMessageDialog(this, "Artikelbestand erfolgreich erhoeht!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie gültige Zahlenwerte ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (ArtikelNichtGefundenException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (PackungsgroesseException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verarbeiteMitarbeiterKlick (){
        if (mitarbeiterName.getText().isEmpty() ||
                passwort.getPassword().length == 0 ||
                passwort1.getPassword().length == 0 ){
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = mitarbeiterName.getText();
        String pass = String.valueOf(passwort.getPassword());
        String pass1 = String.valueOf(passwort1.getPassword());

        try {
            SV.checkPasswort(pass, pass1);
            SV.mitarbeiterAnlegen(name, pass);
            JOptionPane.showMessageDialog(EinloggenMitarbeiter.this, "Mitarbeiter erfolgreich hinzugefügt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            if (mitarbeiterHinzuPanel != null) {
                mitarbeiterHinzuPanel.setVisible(false);
            }
            buttonsLayoutMitarbeiter();
        } catch (RegistrierenFehlgeschlagenException ex) {
            JOptionPane.showMessageDialog(EinloggenMitarbeiter.this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void tabelle() {
        artikelModel = new ArtikelTabelModel(SV.gibAlleArtikel());
        artikelTabel = new JTable(artikelModel);

        JPanel panel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(artikelTabel);


        artikelListeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(true);
                revalidate();
                repaint();
            }
        });

        getContentPane().removeAll();
        add(mitarbeiterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void tabelleEreignisse() {
        ereignisModel = new EreignisTabelModel(SV.gebeEreignisListe());
        ereignisTabel = new JTable(ereignisModel);

        JPanel panel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(ereignisTabel);


        artikelListeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(true);
                revalidate();
                repaint();
            }
        });

        Collections.sort(SV.gebeEreignisListe(), Comparator.comparing(Ereignis::getDatum));


        getContentPane().removeAll();
        add(mitarbeiterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
