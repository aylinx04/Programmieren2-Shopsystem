package common;


public class Ereignis {
    private String datum;
    private String person;
    private String status;

    public Ereignis(String datum, String person, String status) {
        this.datum = datum;
        this.person = person;
        this.status = status;
    }

    public String getDatum() {
        return datum;
    }

    public String getPerson() {
        return person;
    }

    public String getStatus() {
        return status;
    }

    public String toString(){
        return datum + "\n" + person + "\n" + status;
    }
}
