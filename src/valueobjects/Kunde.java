package src.valueobjects;

public class Kunde {
    private String name;
    private int kundenNr;
    private String straße;
    private String plz;
    private String wohnort;

    public Kunde(String name, int nr) {
        this.name = name;
        kundenNr = nr;
    }

    public String getName() {
        return name;
    }

    public int getKundenNr() {
        return kundenNr;
    }

    public String getStraße() {
        return straße;
    }

    public String getPlz() {
        return plz;
    }

   public String getWohnort() {
        return wohnort;
   }
}
