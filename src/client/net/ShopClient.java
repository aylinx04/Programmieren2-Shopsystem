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
        // Verbindung zum Server aufbauen
        socket = new Socket("127.0.0.1", 1399);
        // Siehe Doku:
        // With this option set to a positive timeout value, a read() call on the InputStream associated with
        // this Socket will block for only this amount of time.
        socket.setSoTimeout(1000); // Jegliche Antworten vom Server werden innerhalb einer Sekunde erwartet

        // Streams vom Socket holen
        InputStream inputStream = socket.getInputStream();
        socketIn = new BufferedReader(new InputStreamReader(inputStream));
        socketOut = new PrintStream(socket.getOutputStream());
    }

    @Override
    public Warenkorb getWk() {
        return null;
    }

    @Override
    public Rechnung erzeugeRechnung() {
        return null;
    }

    @Override
    public List<Ereignis> gibEreignisListe() {
        return null;
    }

    @Override
    public List<Artikel> gibAlleArtikel(){
        String cmd = Commands.CMD_GIB_ALLE_ARTIKEL.name();
        socketOut.println(cmd);

        String[] data = readResponse();

        if(Commands.valueOf(data[0]) != Commands.CMD_GIB_ALLE_ARTIKEL_RESP){
            throw new RuntimeException("Ungueltige Antwort auf Anfrage erhalten!");
        }
        return createArtikellisteFromData(data);
    }

    @Override
    public int checkLogin(String name, String passwort) throws LoginFehlgeschlagenException {
        return 0;
    }

    @Override
    public void checkPasswort(String passwort, String passwort2) throws RegistrierenFehlgeschlagenException {

    }

    @Override
    public void checkPackungsgroesse(int packungsgroesse, int bestand) throws PackungsgroesseException {

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
    public void schreibeArtikelDaten(String datei) throws IOException {

    }

    @Override
    public void schreibeMitarbeiterDaten(String datei) throws IOException {

    }

    @Override
    public void schreibeKundenDaten(String datei) throws IOException {

    }

    @Override
    public void schreibeEreignisDaten(String datei) throws IOException {

    }

    @Override
    public List<Artikel> sucheArtikel(String titel) {
        return null;
    }

    @Override
    public List<Ereignis> sucheEreignis(String titel) {
        return null;
    }

    @Override
    public List<Artikel> sucheNachTitel(String titel) {
        return null;
    }

    @Override
    public List<Ereignis> sucheNachEreignis(String ereignis) {
        return null;
    }

    private List<Artikel> createArtikellisteFromData(String[] data){
        List<Artikel> artikelListe = new ArrayList<>();

        for(int i=1; i< data.length; i+=4){
            String name = data[i];
            int nummer = Integer.parseInt(data[i+1]);
            double preis = Double.parseDouble(data[i+2]);
            int bestand = Integer.parseInt(data[i+3]);

            artikelListe.add(new Artikel(name, nummer, preis, bestand));
        }
        return artikelListe;
    }

    private String[] readResponse() {
        String[] parts = null;
        try {
            // Auf Antwort warten. Es wird maximal 1000ms gewartet
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
