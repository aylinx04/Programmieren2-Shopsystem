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
    private GridBagConstraints c = new GridBagConstraints();

    public EinloggenMitarbeiter(JFrame parent, String title, boolean modal, ShopVerwaltungen SV) {
        super(parent, title, modal);
        this.SV = SV;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        buttonsLayoutMitarbeiter();
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

                    break;
                case "Hinzufügen":
                    artikelHinzuLayout();
                    break;
                case "Bestand":

                    break;
                case "Mitarbeiter hinzufügen":

                    break;
                case "Ereignisse":

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
