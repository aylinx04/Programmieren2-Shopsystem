package src.client.net;

import src.common.*;
import src.common.exceptions.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopClient implements IShopVerwaltung {
    private Socket socket;
    private BufferedReader socketIn;
    private PrintStream socketOut;
    final String separator = ";";

    public ShopClient() throws IOException {
        socket = new Socket("127.0.0.1", 1399);
//        socket.setSoTimeout(1000);
        InputStream inputStream = socket.getInputStream();
        socketIn = new BufferedReader(new InputStreamReader(inputStream));
        socketOut = new PrintStream(socket.getOutputStream());
    }

   @Override
    public Kunde getLoggedInCustomer() {
        String cmd = Commands.CMD_GET_LOGGED_IN_CUSTOMER.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_GET_LOGGED_IN_CUSTOMER_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        String name = data[1];
        int nummer = Integer.parseInt(data[2]);
        String passwort = data[3];
        String strasse = data[4];
        String plz = data[5];
        String wohnort = data[6];

        Kunde kunde = new Kunde(name, nummer, passwort, strasse, plz, wohnort);

        return kunde;
    }

    @Override
    public Warenkorb getWk() {
        String cmd = Commands.CMD_GET_WK.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_GET_WK_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
        return createWarenkorbFromData(data);
    }

    public Warenkorb createWarenkorbFromData(String[] data) {
        Warenkorb warenkorb = new Warenkorb();
        Map<String, Artikel> warenkorbMap = warenkorb.getWarenkorb();

        for (int i=1; i < data.length; i+=4) {
            String name = data[i];
            int nummer = Integer.parseInt(data[i+1]);
            double preis = Double.parseDouble(data[i+2]);
            int bestand = Integer.parseInt(data[i+3]);

            if (data.length > i+4 && data[i+4].matches("\\d+")) {
                int packungsgroesse = Integer.parseInt(data[i+4]);
                Massengutartikel m = new Massengutartikel(name, nummer, preis, bestand, packungsgroesse);
                warenkorbMap.put(name, m);
                i+=1;
            } else {
                Artikel a = new Artikel(name, nummer, preis, bestand);
                warenkorbMap.put(name, a);
            }
        }

        return warenkorb;
    }

    @Override
    public String erzeugeRechnung() {
        String cmd = Commands.CMD_ERZEUGE_RECHNUNG.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_ERZEUGE_RECHNUNG_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        LocalDate datum = LocalDate.parse(data[1]);
        double preis = Double.parseDouble(data[2]);
        String name = data[3];
        String strasse = data[4];
        String plz = data[5];
        String wohnort = data[6];

        return datum + "\n" + "Gesamtpreis: " + preis + "€" + "\n" + "Kunde: " +
                name + "\n" + strasse + "\n" + plz + " " + wohnort + "\n";
    }

    @Override
    public List<Ereignis> gibEreignisListe() {
        String cmd = Commands.CMD_GIB_EREIGNISLISTE.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_GIB_EREIGNISLISTE_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
        return createEreignislisteFromData(data);
    }

    @Override
    public List<Artikel> gibAlleArtikel(){
        String cmd = Commands.CMD_GIB_ALLE_ARTIKEL.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_GIB_ALLE_ARTIKEL_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
        return createArtikellisteFromData(data);
    }

    @Override
    public int checkLogin(String name, String passwort) throws LoginFehlgeschlagenException {
        String cmd = Commands.CMD_CHECK_LOGIN.name() + separator + name + separator + passwort;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_CHECK_LOGIN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        int result = Integer.parseInt(data[1]);
        if (result == 0) {
            throw new LoginFehlgeschlagenException();
        }

        return result;
    }

    @Override
    public void checkPasswort(String passwort, String passwort2) throws RegistrierenFehlgeschlagenException {
        String cmd = Commands.CMD_CHECK_PASSWORT.name() + separator + passwort + separator + passwort2;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_CHECK_PASSWORT_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        boolean passwortGleich = Boolean.parseBoolean(data[1]);
        if (!passwortGleich) {
            throw new RegistrierenFehlgeschlagenException();
        }
    }

    @Override
    public void checkPackungsgroesse(int packungsgroesse, int bestand) throws PackungsgroesseException {
        String cmd = Commands.CMD_CHECK_PACKUNGSGROESSE.name() + separator + packungsgroesse + separator + bestand;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_CHECK_PACKUNGSGROESSE_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        boolean packungsgroesseKorrekt = Boolean.parseBoolean(data[1]);
        if (!packungsgroesseKorrekt) {
            throw new PackungsgroesseException();
        }
    }

    @Override
    public void artikelInDenWk(String name, int anzahl) throws ArtikelNichtGefundenException {
        String cmd = Commands.CMD_ARTIKEL_IN_DEN_WK.name() + separator + name + separator + anzahl;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_ARTIKEL_IN_DEN_WK_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if(data.length == 2) {
            throw new ArtikelNichtGefundenException(name);
        }
    }

    @Override
    public void artikelZurueck(String name, int anzahl) {
        String cmd = Commands.CMD_ARTIKEL_ZURUECK.name() + separator + name + separator + anzahl;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_ARTIKEL_ZURUECK_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void artikelBestandVerringern(String name, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {
        String cmd = Commands.CMD_ARTIKEL_BESTAND_VERRINGERN.name() + separator + name + separator + anzahl;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_ARTIKEL_BESTAND_VERRINGERN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if (data[1].equals("Fehler 1")) {
            throw new BestandNichtVorhandenException();
        }
        if (data[1].equals("Fehler 2")) {
            throw new PackungsgroesseException();
        }
    }

    @Override
    public void istArtikelImWarenkorb(String artikelname) throws ArtikelNichtGefundenException {
        String cmd = Commands.CMD_IST_ARTIKEL_IM_WARENKORB.name() + separator + artikelname;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_IST_ARTIKEL_IM_WARENKORB_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        boolean artikelGefunden = Boolean.parseBoolean(data[1]);
        if (!artikelGefunden) {
            throw new ArtikelNichtGefundenException(artikelname);
        }
    }

    @Override
    public void wkArtikelEntfernen(String artikelname, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {
        String cmd = Commands.CMD_WK_ARTIKEL_ENTFERNEN.name() + separator + artikelname + separator + anzahl;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_WK_ARTIKEL_ENTFERNEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if (data[1].equals("Fehler 1")) {
            throw new BestandNichtVorhandenException();
        }
        if (data[1].equals("Fehler 2")) {
            throw new PackungsgroesseException();
        }
    }

    @Override
    public void kundeAnlegen(String name, String passwort, String strasse, String plz, String wohnort) {
        String cmd = Commands.CMD_KUNDE_ANLEGEN.name() + separator + name + separator + passwort +
                separator + strasse + separator + plz + separator + wohnort;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_KUNDE_ANLEGEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException {
        String cmd = Commands.CMD_ARTIKEL_ANLEGEN.name() + separator + name + separator + preis + separator + bestand;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_ARTIKEL_ANLEGEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if (data.length > 1) {
            throw new ArtikelExistiertBereitsException(name);
        }
    }

    @Override
    public void artikelAnlegen(String name, double preis, int bestand, int packungsgroesse) throws ArtikelExistiertBereitsException {
        String cmd = Commands.CMD_ARTIKEL_ANLEGEN.name() + separator + name + separator + preis + separator + bestand +
                separator + packungsgroesse;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_ARTIKEL_ANLEGEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if (data.length > 1) {
            throw new ArtikelExistiertBereitsException(name);
        }
    }

    @Override
    public void bestandErhoehen(String name, int anzahl) throws ArtikelNichtGefundenException, PackungsgroesseException {
        String cmd = Commands.CMD_BESTAND_ERHOEHEN.name() + separator + name + separator + anzahl;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_BESTAND_ERHOEHEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if (data[1].equals("Fehler 1")) {
            throw new ArtikelNichtGefundenException(name);
        }
        if (data[1].equals("Fehler 2")) {
            throw new PackungsgroesseException();
        }
    }

    @Override
    public void mitarbeiterAnlegen(String name, String passwort) throws MitarbeiterExistiertBereitsException {
        String cmd = Commands.CMD_MITARBEITER_ANLEGEN.name() + separator + name + separator + passwort;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_MITARBEITER_ANLEGEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }

        if (data.length > 1) {
            throw new MitarbeiterExistiertBereitsException(name);
        }
    }

    @Override
    public void schreibeArtikelDaten() {
        String cmd = Commands.CMD_SCHREIBE_ARTIKEL_DATEN.name();
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_ARTIKEL_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void schreibeMitarbeiterDaten() {
        String cmd = Commands.CMD_SCHREIBE_MITARBEITER_DATEN.name();
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_MITARBEITER_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void schreibeKundenDaten() {
        String cmd = Commands.CMD_SCHREIBE_KUNDEN_DATEN.name();
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_KUNDEN_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void schreibeEreignisDaten() {
        String cmd = Commands.CMD_SCHREIBE_EREIGNIS_DATEN.name();
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_EREIGNIS_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public List<Artikel> sucheArtikel(String titel) {
        String cmd = Commands.CMD_SUCHE_ARTIKEL.name() + separator + titel;
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_SUCHE_ARTIKEL_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
        return createArtikellisteFromData(data);
    }

    @Override
    public List<Ereignis> sucheEreignis(String titel) {
        String cmd = Commands.CMD_SUCHE_EREIGNIS.name() + separator + titel;
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_SUCHE_EREIGNIS_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
        return createEreignislisteFromData(data);
    }

    @Override
    public void warenkorbLeeren(){
        String cmd = Commands.CMD_WARENKORB_LEEREN.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_WARENKORB_LEEREN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void vonAbisZ(){
        String cmd = Commands.CMD_VON_A_BIS_Z.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_VON_A_BIS_Z_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }
    @Override
    public void vonZbisA(){
        String cmd = Commands.CMD_VON_Z_BIS_A.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_VON_Z_BIS_A_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void artikelnummerAufsteigend(){
        String cmd = Commands.CMD_ARTIKELNUMMER_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_ARTIKELNUMMER_AUFSTEIGEND_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }
    @Override
    public void artikelnummerAbsteigend(){
        String cmd = Commands.CMD_ARTIKELNUMMER_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_ARTIKELNUMMER_ABSTEIGEND_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void preisAufsteigend(){
        String cmd = Commands.CMD_PREIS_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_PREIS_AUFSTEIGEND_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void preisAbsteigend(){
        String cmd = Commands.CMD_PREIS_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_PREIS_ABSTEIGEND_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void bestandAufsteigend(){
        String cmd = Commands.CMD_BESTAND_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_BESTAND_AUFSTEIGEND_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void bestandAbsteigend(){
        String cmd = Commands.CMD_BESTAND_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_BESTAND_ABSTEIGEND_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    private List<Artikel> createArtikellisteFromData(String[] data) {
        List<Artikel> artikelListe = new ArrayList<>();

        for(int i=1; i < data.length; i+=4){
            String name = data[i];
            int nummer = Integer.parseInt(data[i+1]);
            double preis = Double.parseDouble(data[i+2]);
            int bestand = Integer.parseInt(data[i+3]);

            if (data.length > i+4 && data[i+4].matches("\\d+")) {
                int packungsgroesse = Integer.parseInt(data[i+4]);
                artikelListe.add(new Massengutartikel(name, nummer, preis, bestand, packungsgroesse));
                i+=1;
            } else {
                artikelListe.add(new Artikel(name, nummer, preis, bestand));
            }
        }

        return artikelListe;
    }

    private List<Ereignis> createEreignislisteFromData(String[] data) {
        List<Ereignis> ereignisListe = new ArrayList<>();

        for (int i=1; i < data.length; i+=3) {
            String datum = data[i];
            String person = data[i+1];
            String status = data[i+2];

            ereignisListe.add(new Ereignis(datum, person, status));
        }

        return ereignisListe;
    }

    private String[] readResponse() {
        String[] parts = null;
        try {
            String receivedData = socketIn.readLine();
            parts = receivedData.split(separator);

            System.err.println("Empfangene Antwort: " + receivedData);
        } catch(SocketTimeoutException e) {
            System.out.println("Server hat nicht geantwortet.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parts;
    }
}
