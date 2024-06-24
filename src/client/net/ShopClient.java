package src.client.net;

import src.common.*;
import src.common.exceptions.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ShopClient implements IShopVerwaltung {
    private Socket socket;
    private BufferedReader socketIn;
    private PrintStream socketOut;
    final String separator = ";";

    public ShopClient() throws IOException {
        socket = new Socket("127.0.0.1", 1399);
        socket.setSoTimeout(1000);
        InputStream inputStream = socket.getInputStream();
        socketIn = new BufferedReader(new InputStreamReader(inputStream));
        socketOut = new PrintStream(socket.getOutputStream());
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

        for (int i=1; i < data.length; i+=4) {
            String name = data[i];
            int nummer = Integer.parseInt(data[i+1]);
            double preis = Double.parseDouble(data[i+2]);
            int bestand = Integer.parseInt(data[i+3]);

            if (data.length > i+4 && data[i+4].matches("\\d+")) {
                int packungsgroesse = Integer.parseInt(data[i+4]);
                Massengutartikel m = new Massengutartikel(name, nummer, preis, bestand, packungsgroesse);
                warenkorb.artikelHinzufuegen(m);
                i+=1;
            } else {
                Artikel a = new Artikel(name, nummer, preis, bestand);
                warenkorb.artikelHinzufuegen(a);            }
        }

        return warenkorb;
    }

    @Override
    public Rechnung erzeugeRechnung() {
        return null;
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
    public Artikel holeArtikel(String name) throws ArtikelNichtGefundenException {
        return null;
    }

    @Override
    public void artikelZurueck(String name, int anzahl) {

    }

    @Override
    public void artikelBestandVerringern(Artikel a, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {

    }

    @Override
    public void istArtikelImWarenkorb(String artikelname) throws ArtikelNichtGefundenException {

    }

    @Override
    public void checkAnzahlDesArtikels(String artikelname, int anzahl) throws BestandNichtVorhandenException, PackungsgroesseException {

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
    public Artikel artikelAnlegen(String name, double preis, int bestand) throws ArtikelExistiertBereitsException {
        return null;
    }

    @Override
    public Artikel artikelAnlegen(String name, double preis, int bestand, int packungsgroesse) throws ArtikelExistiertBereitsException {
        return null;
    }

    @Override
    public void ereignisBestandErhoeht(Artikel artikel, int anzahl) throws PackungsgroesseException {

    }

    @Override
    public void mitarbeiterAnlegen(String name, String passwort) throws MitarbeiterExistiertBereitsException {

    }

    @Override
    public void schreibeArtikelDaten(String datei) {
        String cmd = Commands.CMD_SCHREIBE_ARTIKEL_DATEN.name() + separator + datei;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_ARTIKEL_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void schreibeMitarbeiterDaten(String datei) {
        String cmd = Commands.CMD_SCHREIBE_MITARBEITER_DATEN.name() + separator + datei;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_MITARBEITER_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void schreibeKundenDaten(String datei) {
        String cmd = Commands.CMD_SCHREIBE_KUNDEN_DATEN.name() + separator + datei;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_KUNDEN_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public void schreibeEreignisDaten(String datei) {
        String cmd = Commands.CMD_SCHREIBE_EREIGNIS_DATEN.name() + separator + datei;
        socketOut.println(cmd);

        String[] data = readResponse();
        if (Commands.valueOf(data[0]) != Commands.CMD_SCHREIBE_EREIGNIS_DATEN_RESP) {
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
    }

    @Override
    public List<Artikel> sucheArtikel(String titel) {
        return null;
    }

    @Override
    public List<Ereignis> sucheEreignis(String titel) {
        return null;
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
