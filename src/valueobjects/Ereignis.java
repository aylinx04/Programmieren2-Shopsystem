package src.valueobjects;

import java.time.LocalDate;

public class Ereignis {
    LocalDate datum = LocalDate.now();
    private String status;

    public Ereignis(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ereignis: " + datum + "\n" + status;
    }

    public LocalDate getDatum() {
        return datum;
    }
}
