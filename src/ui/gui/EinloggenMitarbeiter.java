package src.ui.gui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.ArtikelExistiertBereitsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EinloggenMitarbeiter extends JDialog {
    private ShopVerwaltungen SV;
    private JTextField artikeltextField = new JTextField();
    private JTextField preistextField = new JTextField();
    private JTextField bestandtextField = new JTextField();
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton artikelHinzufügenButton = new JButton("Hinzufügen");
    private JButton bestandErhöhenButton = new JButton("Bestand");
    private JTextField packungsgroesseTextField = new JTextField();
    private ButtonGroup massengutGroup = new ButtonGroup();
    private JRadioButton massengutJa = new JRadioButton("Ja");
    private JRadioButton massengutNein = new JRadioButton("Nein");
    private JButton mitarbeiterHinzufügenButton = new JButton("Mitarbeiter hinzufügen");
    private JButton ereignisListeButton = new JButton("Ereignisse");
    private JButton ausloggenButton = new JButton("Ausloggen");
    private JPanel westPanel = new JPanel(new GridBagLayout());
    private GridBagConstraints c = new GridBagConstraints();

    public EinloggenMitarbeiter(JFrame parent, String title, boolean modal, ShopVerwaltungen SV) {
        super(parent, title, modal);
        this.SV = SV;
        buttonsLayoutMitarbeiter();
        setSize(640, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void buttonsLayoutMitarbeiter() {
        Dimension eingabeFeldGroesse = new Dimension(140, 30);
        c.anchor = GridBagConstraints.NORTH;

        artikelListeButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        westPanel.add(artikelListeButton, c);

        artikelHinzufügenButton.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 1;
        westPanel.add(artikelHinzufügenButton, c);

        bestandErhöhenButton.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 2;
        westPanel.add(bestandErhöhenButton, c);

        mitarbeiterHinzufügenButton.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 3;
        westPanel.add(mitarbeiterHinzufügenButton, c);

        ereignisListeButton.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 4;
        westPanel.add(ereignisListeButton, c);

        ausloggenButton.setPreferredSize(eingabeFeldGroesse);
        c.gridx = 5;
        westPanel.add(ausloggenButton, c);

        artikelListeButton.addActionListener(new ButtonActionListener());
        artikelHinzufügenButton.addActionListener(new ButtonActionListener());
        bestandErhöhenButton.addActionListener(new ButtonActionListener());
        mitarbeiterHinzufügenButton.addActionListener(new ButtonActionListener());
        ereignisListeButton.addActionListener(new ButtonActionListener());
        ausloggenButton.addActionListener(new ButtonActionListener());

        add(westPanel, BorderLayout.NORTH);
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();

            switch (source.getText()) {
                case "Artikelliste":

                    break;
                case "Hinzufügen":
                    zeigeArtikelHinzufuegen();
                    break;
                case "Bestand":

                    break;
                case "Mitarbeiter hinzufügen":

                    break;
                case "Ereignisse":

                    break;
                case "Ausloggen":

                    break;
                default:
                    break;
            }
        }
    }

    private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, Dimension groesse, GridBagConstraints c) {
        component.setPreferredSize(groesse);
        c.gridx = gridx;
        c.gridy = gridy;
        panel.add(component, c);
    }

    private void zeigeArtikelHinzufuegen() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Dimension eingabeFeldGroesse = new Dimension(140, 30);
        c.anchor = GridBagConstraints.NORTH;

        addComponent(formPanel, new JLabel("Artikelname:"), 0, 0, eingabeFeldGroesse, c);
        addComponent(formPanel, artikeltextField, 1, 0, eingabeFeldGroesse, c);
        addComponent(formPanel, new JLabel("Preis:"), 0, 1, eingabeFeldGroesse, c);
        addComponent(formPanel, preistextField, 1, 1, eingabeFeldGroesse, c);
        addComponent(formPanel, new JLabel("Bestand:"), 0, 2, eingabeFeldGroesse, c);
        addComponent(formPanel, bestandtextField, 1, 2, eingabeFeldGroesse, c);
        addComponent(formPanel, new JLabel("Massengutartikel:"), 0, 3, eingabeFeldGroesse, c);

        massengutGroup.add(massengutJa);
        massengutGroup.add(massengutNein);
        addComponent(formPanel, massengutJa, 1, 3, eingabeFeldGroesse, c);
        addComponent(formPanel, massengutNein, 2, 3, eingabeFeldGroesse, c);

        JLabel labelPackungsgroesse = new JLabel("Packungsgröße:");
        addComponent(formPanel, labelPackungsgroesse, 0, 4, eingabeFeldGroesse, c);
        addComponent(formPanel, packungsgroesseTextField, 1, 4, eingabeFeldGroesse, c);

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
        formPanel.add(speichernButton, c);

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    verarbeiteArtikelHinzuKlick();
                } catch (ArtikelExistiertBereitsException ex) {
                    JOptionPane.showMessageDialog(EinloggenMitarbeiter.this, "Artikel existiert bereits!", "Fehler", JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        getContentPane().removeAll();
        add(westPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    void verarbeiteArtikelHinzuKlick() throws ArtikelExistiertBereitsException {
        String artikel = artikeltextField.getText();
        double preis = Double.parseDouble(preistextField.getText());
        int bestand = Integer.parseInt(bestandtextField.getText());

        if (massengutJa.isSelected()) {
            int packungsgroesse = Integer.parseInt(packungsgroesseTextField.getText());
            SV.artikelAnlegen(artikel, preis, bestand, packungsgroesse);
        } else {
            SV.artikelAnlegen(artikel, preis, bestand);
        }
    }
}
