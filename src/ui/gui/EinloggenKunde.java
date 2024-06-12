package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.valueobjects.exceptions.ArtikelNichtGefundenException;
import src.valueobjects.exceptions.BestandNichtVorhandenException;
import src.valueobjects.exceptions.PackungsgroesseException;
import src.valueobjects.Artikel;
import src.valueobjects.Massengutartikel;
import src.valueobjects.Warenkorb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class EinloggenKunde extends JDialog{
    private ShopVerwaltungen SV;
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton warenkorbButton = new JButton("Warenkorb");
    private JButton ausloggenButton = new JButton("Ausloggen");
    private JButton artikelHinzuButton = new JButton("in den Warenkorb");
    private JButton artikelEntfernenButton = new JButton("entfernen");
    private JButton warenkorbLeerenButton = new JButton("Warenkorb leeren");
    private JButton kaufenButton = new JButton("Kaufen");
    private JTextField textfieldArtikelname = new JTextField();
    private JTextField textfieldAnzahl = new JTextField();
    private JTextField textfieldArtikelname2 = new JTextField();
    private JTextField textfieldAnzahl2 = new JTextField();
    private JPanel kundenPanel = new JPanel(new GridBagLayout());
    private JPanel hinzuPanel = new JPanel();
    private JPanel entfernenPanel = new JPanel();
    private JPanel kaufPanel = new JPanel();
    private ArtikelTabelModel artikelModel;
    private JTable artikelTabel;

    public EinloggenKunde(JFrame parent, String title, boolean modal, ShopVerwaltungen SV) {
        super(parent, title, modal);
        this.SV = SV;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        buttonsLayoutKunde();
        artikelTabelle();
        entfernenLayout();
        hinzufuegenLayout();

        setSize(640, 480);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void buttonsLayoutKunde() {
        kundenPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        addComponent(kundenPanel, artikelListeButton, 0, 0, eingabeFeldGroesse, c);
        addComponent(kundenPanel, warenkorbButton, 1, 0, eingabeFeldGroesse, c);
        addComponent(kundenPanel, ausloggenButton, 2, 0, eingabeFeldGroesse, c);

        artikelListeButton.addActionListener(new EinloggenKunde.ButtonActionListener());
        warenkorbButton.addActionListener(new EinloggenKunde.ButtonActionListener());

        ausloggenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int antwort = JOptionPane.showConfirmDialog(EinloggenKunde.this, "Möchten Sie sich wirklich abmelden?", "Abmelden", JOptionPane.YES_NO_OPTION);
                if (antwort == JOptionPane.YES_OPTION) {
                    dispose();
                    try {
                        SV.schreibeArtikelDaten("Shop_A.txt");
                        SV.schreibeKundenDaten("Shop_K.txt");
                        SV.schreibeEreignisDaten("Shop_E.txt");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        add(kundenPanel, BorderLayout.NORTH);
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
                    artikelTabelle();
                    break;
                case "Warenkorb":
                    zeigeWarenkorb();
                    break;
                default:
                    break;
            }
        }
    }

    private void artikelTabelle() {
        artikelModel = new ArtikelTabelModel(SV.gibAlleArtikel());
        artikelTabel = new JTable(artikelModel);

        JPanel artikelPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(artikelTabel);

        getContentPane().removeAll();
        add(kundenPanel, BorderLayout.NORTH);
        add(hinzuPanel, BorderLayout.WEST);
        artikelPanel.add(scrollPane, BorderLayout.CENTER);
        add(artikelPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void hinzufuegenLayout() {
        hinzuPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        addComponent(hinzuPanel, new JLabel("Artikelname:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(hinzuPanel, textfieldArtikelname, 0, 1, eingabeFeldGroesse, c);
        addComponent(hinzuPanel, new JLabel("Anzahl:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(hinzuPanel, textfieldAnzahl, 0, 3, eingabeFeldGroesse, c);

        c.weighty = 1.0;
        addComponent(hinzuPanel, artikelHinzuButton, 0, 4, eingabeFeldGroesse, c); hinzuPanel.add(artikelHinzuButton, c);
        artikelHinzuButton.addActionListener(this::verarbeiteHinzufuegenKlick);

        add(hinzuPanel, BorderLayout.WEST);
    }

    private void zeigeWarenkorb() {
        Warenkorb warenkorb = SV.getWk();
        Map<String, Artikel> warenkorbMap = warenkorb.getWarenkorb();
        ArrayList<Artikel> warenkorbListe = new ArrayList<>(warenkorbMap.values());

        ArtikelTabelModel warenkorbModel = new ArtikelTabelModel(warenkorbListe);
        JTable warenkorbTabel = new JTable(warenkorbModel);

        JPanel warenkorbPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(warenkorbTabel);

        getContentPane().removeAll();
        add(kundenPanel, BorderLayout.NORTH);
        add(entfernenPanel, BorderLayout.WEST);
        warenkorbPanel.add(scrollPane, BorderLayout.CENTER);
        add(warenkorbPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void kaufenLayout(){
        getContentPane().removeAll();

        kaufPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;

        addComponent(kaufPanel, new JLabel("Ihr Einkauf:"), 0, 0, eingabeFeldGroesse, c);

        JTextArea rechnung = new JTextArea(SV.erzeugeRechnung().toString());
            c.gridx =0;
            c.gridy =1;
        kaufPanel.add(rechnung,c);

        Warenkorb warenkorb = SV.getWk();
        Map<String, Artikel> warenkorbMap = warenkorb.getWarenkorb();
        ArrayList<Artikel> warenkorbListe = new ArrayList<>(warenkorbMap.values());

        ArtikelTabelModel warenkorbModel = new ArtikelTabelModel(warenkorbListe);
        JTable warenkorbTabel = new JTable(warenkorbModel);

        JPanel warenkorbPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(warenkorbTabel);


        add(kaufPanel, BorderLayout.CENTER);
        add(kundenPanel, BorderLayout.NORTH);
        warenkorbPanel.add(scrollPane, BorderLayout.SOUTH);
        add(warenkorbPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void entfernenLayout() {
        entfernenPanel.setLayout(new GridBagLayout());
        Dimension eingabeFeldGroesse = new Dimension(140,30);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;

        addComponent(entfernenPanel, new JLabel("Artikelname:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(entfernenPanel, textfieldArtikelname2, 0, 1, eingabeFeldGroesse, c);
        addComponent(entfernenPanel, new JLabel("Anzahl:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(entfernenPanel, textfieldAnzahl2, 0, 3, eingabeFeldGroesse, c);

        c.weighty = 1.0;
        addComponent(entfernenPanel, artikelEntfernenButton, 0, 4, eingabeFeldGroesse, c); entfernenPanel.add(artikelEntfernenButton, c);
        artikelEntfernenButton.addActionListener(this::verarbeiteEntfernenKlick);
        addComponent(entfernenPanel, warenkorbLeerenButton, 0, 5, eingabeFeldGroesse, c); entfernenPanel.add(warenkorbLeerenButton, c);
        warenkorbLeerenButton.addActionListener(this::verarbeiteLeerenKlick);
        addComponent(entfernenPanel, kaufenButton, 0, 6, eingabeFeldGroesse, c);
        kaufenButton.addActionListener(this::verarbeiteKaufenKlick);

        add(entfernenPanel, BorderLayout.WEST);
    }

    private void verarbeiteHinzufuegenKlick(ActionEvent e){
        if(!e.getSource().equals(artikelHinzuButton))
            return;
        if(textfieldArtikelname.getText().isEmpty() ||
                textfieldAnzahl.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String artikelname = textfieldArtikelname.getText();

        try {
            int anzahl = Integer.parseInt(textfieldAnzahl.getText());
            textfieldArtikelname.setText(null);
            textfieldAnzahl.setText(null);
            Artikel a = SV.holeArtikel(artikelname);
            SV.artikelBestandVerringern(a, anzahl);
            Artikel wkArtikel;
            if(a instanceof Massengutartikel m){
                wkArtikel = new Massengutartikel(m.getName(), m.getNummer(), m.getPreis(), anzahl, m.getPackungsgroesse());
            } else {
                wkArtikel = new Artikel(a.getName(), a.getNummer(), a.getPreis(), anzahl);
            }
            Warenkorb w = SV.getWk();
            w.artikelHinzufuegen(wkArtikel);
            JOptionPane.showMessageDialog(this, "Artikel '" + artikelname + "' erfolgreich " + anzahl + "x zum Warenkorb hinzugefügt!",
                    "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie gültige Zahlenwerte ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (ArtikelNichtGefundenException | BestandNichtVorhandenException | PackungsgroesseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void verarbeiteKaufenKlick(ActionEvent e){
            int antwort = JOptionPane.showConfirmDialog(EinloggenKunde.this, "Möchten Sie wirklich kaufen?", "Kauf", JOptionPane.YES_NO_OPTION);
                if(antwort ==JOptionPane.YES_OPTION) {
                kaufenLayout();
                    Warenkorb warenkorb = SV.getWk();
                    warenkorb.warenkorbLeeren();
                }
    }

    private void verarbeiteEntfernenKlick(ActionEvent e){
        if(!e.getSource().equals(artikelEntfernenButton))
            return;
        if(textfieldArtikelname2.getText().isEmpty() ||
                textfieldAnzahl2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String artikelname = textfieldArtikelname2.getText();

        try {
            int anzahl = Integer.parseInt(textfieldAnzahl2.getText());
            textfieldArtikelname2.setText(null);
            textfieldAnzahl2.setText(null);
            SV.istArtikelImWarenkorb(artikelname);
            SV.checkAnzahlDesArtikels(artikelname, anzahl);
            SV.artikelZurueck(artikelname, anzahl);
            JOptionPane.showMessageDialog(this, "Artikel '" + artikelname + "' erfolgreich " + anzahl + "x aus dem Warenkorb entfernt!",
                    "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie gültige Zahlenwerte ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (ArtikelNichtGefundenException | BestandNichtVorhandenException | PackungsgroesseException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verarbeiteLeerenKlick(ActionEvent e){
        if(!e.getSource().equals(warenkorbLeerenButton))
            return;
        Warenkorb warenkorb = SV.getWk();
        Map<String, Artikel> warenkorbMap = warenkorb.getWarenkorb();
        for (Artikel a : warenkorbMap.values()){
            SV.artikelZurueck(a.getName(), a.getBestand());
        }
        warenkorb.warenkorbLeeren();
        JOptionPane.showMessageDialog(this, "Warenkorb wurde geleert!",
                "Erfolg", JOptionPane.INFORMATION_MESSAGE);
    }
}
