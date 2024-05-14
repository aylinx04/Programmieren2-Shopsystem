package src.valueobjects;

import java.time.LocalDate;

public class Ereignis {
    private LocalDate datum;
    private String status;

    public Ereignis(String status){
        this.datum = datum;
        this.status = status;
    }

    public LocalDate getDatum() {
        return datum;
    }

    @Override
    public String toString() {
        return "Ereignis:" + datum + "\n" + status;
    }
}
