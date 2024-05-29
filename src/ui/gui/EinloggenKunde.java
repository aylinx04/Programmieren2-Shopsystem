package src.ui.gui;

import javax.swing.*;
import java.awt.*;

public class EinloggenKunde extends JFrame{
    private JButton warenkorbButton = new JButton("Warenkorb");
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton ausloggenButton = new JButton("Ausloggen");

    public EinloggenKunde() {
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

        warenkorbButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        westPanel.add(warenkorbButton, c);

        ausloggenButton.setPreferredSize(eingabeFeldGroesse);
        c.weighty = 1.0;
        c.gridx = 5;
        c.gridy = 0;
        westPanel.add(ausloggenButton, c);
//        artikelListeButton.addActionListener(dasEvent -> verarbeiteEinloggenKlick(dasEvent));

        add(westPanel, BorderLayout.NORTH);
    }
}
