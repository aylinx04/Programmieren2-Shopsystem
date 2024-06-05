package src.ui.gui;

import src.domain.ShopVerwaltungen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EinloggenKunde extends JDialog{
    private ShopVerwaltungen SV;
    private JButton artikelListeButton = new JButton("Artikelliste");
    private JButton warenkorbButton = new JButton("Warenkorb");
    private JButton ausloggenButton = new JButton("Ausloggen");
    private JPanel kundenPanel = new JPanel(new GridBagLayout());
    private ArtikelTabelModel artikelModel;
    private JTable artikelTabel;

    public EinloggenKunde(JFrame parent, String title, boolean modal, ShopVerwaltungen SV) {
        super(parent, title, modal);
        this.SV = SV;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        buttonsLayoutKunde();
        tabelle();
        setSize(640, 480);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void buttonsLayoutKunde(){
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
                    tabelle();
                    break;
                case "Warenkorb":

                    break;
                default:
                    break;
            }
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
        add(kundenPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

}
