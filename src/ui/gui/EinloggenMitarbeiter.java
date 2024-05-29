package src.ui.gui;

import javax.swing.*;
import java.awt.*;

public class EinloggenMitarbeiter extends JFrame{
    private JButton artikelHinzufügenButton = new JButton("Hinzufügen");
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton bestandErhöhenButton = new JButton("Bestand");
    private JButton mitarbeiterHinzufügenButton = new JButton("Mitarbeiter Hinzufügen");
    private JButton ereignisListeButton = new JButton("Ereignisse");
    private JButton ausloggenButton = new JButton("Ausloggen");

    public EinloggenMitarbeiter() {
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

        add(westPanel, BorderLayout.NORTH);
    }
}
