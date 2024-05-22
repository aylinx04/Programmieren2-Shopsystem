package src.valueobjects;

import java.time.LocalDate;

public class Ereignis {
    private String datum;
    private String status;
    private String person;

    public Ereignis(String datum, String person, String status){
        this.person = person;
        this.status = status;
        this.datum = datum;
    }

    public String getDatum() {
        return datum;
    }

    public String getStatus() {
        return status;
    }

    public String getPerson() {
        return person;
    }
}
