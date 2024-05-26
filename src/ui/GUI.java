package src.ui;

import src.domain.ShopVerwaltungen;
import src.domain.exceptions.ArtikelExistiertBereitsException;
import src.valueobjects.Artikel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame{
    private ShopVerwaltungen SV;
    private JTextField textfieldNummer = new JTextField();
    private JTextField textfieldTitel = new JTextField();
    private JButton hinzufuegenButton = new JButton("Hinzufügen");
    private JTextField suchTextFeld = new JTextField();
    private JButton suchenButton = new JButton("Suche");
    JList artikelListe = new JList();


    public GUI(String titel){
        super(titel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(300,200);
        setLocation(0,500);
        setVisible(true);
    }


    public static void main(String[] args) {
        GUI gui = new GUI("E-Shop");

    }
}
