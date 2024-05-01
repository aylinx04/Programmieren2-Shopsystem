package src.valueobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Warenkorb {
    static List<Artikel> warenkorb = new ArrayList<>();

    public Warenkorb(){
        warenkorb.add(new Artikel("Brot", 7, "2,99€", 22));
    }

    public static List<Artikel> getWarenkorb() {return warenkorb;}

}