package src.valueobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Warenkorb {
    List<Artikel> WK = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public Warenkorb(){

    }
    public List<Artikel> getWK() {return WK;}
}