package src.valueobjects;

import java.time.LocalDate;

public class Ereignis {
    LocalDate datum = LocalDate.now();
    private String status;

    public Ereignis(String status){
        this.status = status;
    }

    public LocalDate getDatum() {
        return datum;
    }

    @Override
    public String toString() {
        return "Ereignis: " + datum + "\n" + status;
    }
}
