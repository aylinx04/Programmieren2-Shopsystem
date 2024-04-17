package src.valueobjects;

public class Mitarbeiter {
    private String name;
    private int nummer;

    public Mitarbeiter(String name, int nummer){
        this.name = name;
        this.nummer = nummer;
    }


    public String getName() {
        return name;
    }

    public int getNummer() {
        return nummer;
    }
}