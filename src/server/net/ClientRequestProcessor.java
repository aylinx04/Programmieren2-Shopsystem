package src.server.net;

import src.common.*;
import src.common.exceptions.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

public class ClientRequestProcessor implements Runnable {
    private BufferedReader socketIn;
    private PrintStream socketOut;
    final String separator = ";";
    IShopVerwaltung shop;

    public ClientRequestProcessor(Socket s, IShopVerwaltung shop) throws IOException {
        this.shop = shop;

        OutputStream outputStream = s.getOutputStream();
        socketOut = new PrintStream(outputStream);

        InputStream inputStream = s.getInputStream();
        socketIn = new BufferedReader(new InputStreamReader(inputStream));
    }

    public void run() {
        while(true) {
            try {
                String receivedData = socketIn.readLine();
                handleCommandRequest(receivedData);
            } catch (SocketException e) {
                System.err.println("Client hat Verbindung geschlossen");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCommandRequest(String receivedData) {
        System.err.println("Vom Client empfangende Daten: " + receivedData);
        String[] parts = receivedData.split(separator);

        switch (Commands.valueOf(parts[0])) {
            case CMD_GET_WK -> handleGetWK();
//            case CMD_ERZEUGE_RECHNUNG -> handleErzeugeRechnung();
            case CMD_GIB_EREIGNISLISTE -> handleGibEreignisListe();
            case CMD_GIB_ALLE_ARTIKEL -> handleGibAlleArtikel();
            case CMD_CHECK_LOGIN -> handleCheckLogin(parts);
            case CMD_CHECK_PASSWORT -> handleCheckPasswort(parts);
            case CMD_CHECK_PACKUNGSGROESSE -> handleCheckPackungsgroesse(parts);
            case CMD_ARTIKEL_IN_DEN_WK -> handleArtikelInDenWK(parts);
            case CMD_ARTIKEL_ZURUECK -> handleArtikelZurueck(parts);
            case CMD_ARTIKEL_BESTAND_VERRINGERN -> handleBestandVerringern(parts);
            case CMD_IST_ARTIKEL_IM_WARENKORB -> handleIstArtikelImWarenkorb(parts);
            case CMD_WK_ARTIKEL_ENTFERNEN -> handleWkArtikelEntfernen(parts);
            case CMD_KUNDE_ANLEGEN -> handleKundeAnlegen(parts);
//            case CMD_ARTIKEL_ANLEGEN -> handleArtikelAnlegen(parts);
            case CMD_BESTAND_ERHOEHEN -> handlebestandErhoehen(parts);
//            case CMD_MITARBEITER_ANLEGEN -> handleMitarbeiterAnlegen(parts);
            case CMD_SCHREIBE_ARTIKEL_DATEN -> handleSchreibeArtikelDaten(parts);
            case CMD_SCHREIBE_MITARBEITER_DATEN -> handleSchreibeMitarbeiterDaten(parts);
            case CMD_SCHREIBE_KUNDEN_DATEN -> handleSchreibeKundenDaten(parts);
            case CMD_SCHREIBE_EREIGNIS_DATEN -> handleSchreibeEreignisDaten(parts);
//            case CMD_SUCHE_ARTIKEL -> handleSucheArtikel(parts);
//            case CMD_SUCHE_EREIGNIS -> handleSucheEreignis(parts);
            default -> System.err.println("Ungueltige Anfrage empfangen!");
        }
    }

    private void handleGetWK(){
        Warenkorb w = shop.getWk();
        Map<String, Artikel> warenkorbMap = w.getWarenkorb();

        String cmd = Commands.CMD_GET_WK_RESP.name();

        for (Artikel a : warenkorbMap.values()) {
            cmd += separator + a.getName();
            cmd += separator + a.getNummer();
            cmd += separator + a.getPreis();
            cmd += separator + a.getBestand();
            if (a instanceof Massengutartikel m) {
                cmd += separator + m.getPackungsgroesse();
            }
        }

        socketOut.println(cmd);
    }

    private void handleErzeugeRechnung() {

    }

    private void handleGibEreignisListe() {
        List<Ereignis> result = shop.gibEreignisListe();

        String cmd = Commands.CMD_GIB_EREIGNISLISTE_RESP.name();

        for (Ereignis e : result) {
            cmd += separator + e.getDatum();
            cmd += separator + e.getPerson();
            cmd += separator + e.getStatus();
        }

        socketOut.println(cmd);
    }

    private void handleGibAlleArtikel() {
        List<Artikel> result = shop.gibAlleArtikel();

        String cmd = Commands.CMD_GIB_ALLE_ARTIKEL_RESP.name();

        for (Artikel a : result){
            cmd += separator + a.getName();
            cmd += separator + a.getNummer();
            cmd += separator + a.getPreis();
            cmd += separator + a.getBestand();
            if (a instanceof Massengutartikel m) {
                cmd += separator + m.getPackungsgroesse();
            }
        }

        socketOut.println(cmd);
    }

    private void handleCheckLogin(String[] data) {
        String name = data[1];
        String passwort = data[2];

        String cmd = Commands.CMD_CHECK_LOGIN_RESP.name();
        try {
            int result = shop.checkLogin(name, passwort);
            cmd += separator + result;
        } catch (LoginFehlgeschlagenException e) {
            cmd += separator + "0";
        }

        socketOut.println(cmd);
    }

    private void handleCheckPasswort(String[] data) {
        String passwort = data[1];
        String passwort2 = data[2];

        String cmd = Commands.CMD_CHECK_PASSWORT_RESP.name();
        try {
            shop.checkPasswort(passwort, passwort2);
            cmd += separator + "true";
        } catch (RegistrierenFehlgeschlagenException e) {
            cmd += separator + "false";
        }

        socketOut.println(cmd);
    }

    private void handleCheckPackungsgroesse(String[] data) {
        int packungsgroesse = Integer.parseInt(data[1]);
        int bestand = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_CHECK_PACKUNGSGROESSE_RESP.name();
        try {
            shop.checkPackungsgroesse(packungsgroesse, bestand);
            cmd += separator + "true";
        } catch (PackungsgroesseException e) {
            cmd += separator + "false";
        }

        socketOut.println(cmd);
    }

    private void handleArtikelInDenWK(String[] data) {
        String name = data[1];
        int anzahl = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_ARTIKEL_IN_DEN_WK_RESP.name();
        try {
            shop.artikelInDenWk(name, anzahl);
            cmd += separator + name + separator + anzahl;
        } catch (ArtikelNichtGefundenException e) {
            cmd += separator + "false";
        }

        socketOut.println(cmd);
    }

    private void handleArtikelZurueck(String[] data) {
        String name = data[1];
        int anzahl = Integer.parseInt(data[2]);

        shop.artikelZurueck(name, anzahl);

        String cmd = Commands.CMD_ARTIKEL_ZURUECK_RESP.name();
        cmd += separator + name + separator + anzahl;

        socketOut.println(cmd);
    }

    private void handleBestandVerringern(String[] data) {
        String name = data[1];
        int anzahl = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_ARTIKEL_BESTAND_VERRINGERN_RESP.name();
        try {
            shop.artikelBestandVerringern(name, anzahl);
            cmd += separator + name + separator + anzahl;
        } catch (BestandNichtVorhandenException e) {
            cmd += separator + "Fehler 1";
        } catch (PackungsgroesseException e) {
            cmd += separator + "Fehler 2";
        }
        socketOut.println(cmd);
    }

    private void handleIstArtikelImWarenkorb(String[] data) {
        String artikelname = data[1];

        String cmd = Commands.CMD_IST_ARTIKEL_IM_WARENKORB_RESP.name();
        try {
            shop.istArtikelImWarenkorb(artikelname);
            cmd += separator + "true";
        } catch (ArtikelNichtGefundenException e) {
            cmd += separator + "false";
        }
        socketOut.println(cmd);
    }

    private void handleWkArtikelEntfernen(String[] data) {
        String name = data[1];
        int anzahl = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_WK_ARTIKEL_ENTFERNEN_RESP.name();
        try {
            shop.wkArtikelEntfernen(name, anzahl);
            cmd += separator + name + separator + anzahl;
        } catch (BestandNichtVorhandenException e) {
            cmd += separator + "Fehler 1";
        } catch (PackungsgroesseException e) {
            cmd += separator + "Fehler 2";
        }
        socketOut.println(cmd);
    }

    private void handleKundeAnlegen(String[] data) {
        String cmd = Commands.CMD_KUNDE_ANLEGEN_RESP.name();
        shop.kundeAnlegen(data[1], data[2], data[3], data[4], data[5]);
        socketOut.println(cmd);
    }

    private void handlebestandErhoehen(String[] data) {
        String name = data[1];
        int anzahl = Integer.parseInt(data[2]);

        String cmd = Commands.CMD_BESTAND_ERHOEHEN_RESP.name();
        try {
            shop.bestandErhoehen(name, anzahl);
            cmd += separator + name + separator + anzahl;
        } catch (ArtikelNichtGefundenException e) {
            cmd += separator + "Fehler 1";
        } catch (PackungsgroesseException e) {
            cmd += separator + "Fehler 2";
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeArtikelDaten(String[] data) {
        String cmd = Commands.CMD_SCHREIBE_ARTIKEL_DATEN_RESP.name();
        try {
            shop.schreibeArtikelDaten(data[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeMitarbeiterDaten(String[] data) {
        String cmd = Commands.CMD_SCHREIBE_MITARBEITER_DATEN_RESP.name();
        try {
            shop.schreibeMitarbeiterDaten(data[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeKundenDaten(String[] data) {
        String cmd = Commands.CMD_SCHREIBE_KUNDEN_DATEN_RESP.name();
        try {
            shop.schreibeKundenDaten(data[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeEreignisDaten(String[] data) {
        String cmd = Commands.CMD_SCHREIBE_EREIGNIS_DATEN_RESP.name();
        try {
            shop.schreibeEreignisDaten(data[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

}
