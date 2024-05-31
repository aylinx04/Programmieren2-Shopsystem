package src.ui.gui;

import src.domain.ShopVerwaltungen;

import javax.swing.*;
import java.awt.*;

public class EinloggenKunde extends JDialog{
    private ShopVerwaltungen SV;
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton warenkorbButton = new JButton("Warenkorb");
    private JButton ausloggenButton = new JButton("Ausloggen");

    public EinloggenKunde(JFrame parent, String title, boolean modal, ShopVerwaltungen SV) {
        super(parent, title, modal);
        this.SV = SV;
        buttonsLayoutMitarbeiter();
        setSize(640, 480);
        setLocationRelativeTo(parent);
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
