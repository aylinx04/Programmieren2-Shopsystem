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
            case CMD_GET_LOGGED_IN_CUSTOMER -> handleGetLoggedInCustomer();
            case CMD_GET_WK -> handleGetWK();
            case CMD_ERZEUGE_RECHNUNG -> handleErzeugeRechnung();
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
            case CMD_ARTIKEL_ANLEGEN -> handleArtikelAnlegen(parts);
            case CMD_BESTAND_ERHOEHEN -> handleBestandErhoehen(parts);
            case CMD_MITARBEITER_ANLEGEN -> handleMitarbeiterAnlegen(parts);
            case CMD_SCHREIBE_ARTIKEL_DATEN -> handleSchreibeArtikelDaten();
            case CMD_SCHREIBE_MITARBEITER_DATEN -> handleSchreibeMitarbeiterDaten();
            case CMD_SCHREIBE_KUNDEN_DATEN -> handleSchreibeKundenDaten();
            case CMD_SCHREIBE_EREIGNIS_DATEN -> handleSchreibeEreignisDaten();
            case CMD_SUCHE_ARTIKEL -> handleSucheArtikel(parts);
            case CMD_SUCHE_EREIGNIS -> handleSucheEreignis(parts);
            case CMD_WARENKORB_LEEREN -> handleWarenkorbLeeren();
            case CMD_VON_A_BIS_Z -> handleVonAbisZ();
            case CMD_VON_Z_BIS_A -> handleVonZbisA();
            case CMD_ARTIKELNUMMER_AUFSTEIGEND -> handleArtikelnummerAufsteigend();
            case CMD_ARTIKELNUMMER_ABSTEIGEND -> handleArtikelnummerAbsteigend();
            case CMD_PREIS_AUFSTEIGEND -> handlePreisAufsteigend();
            case CMD_PREIS_ABSTEIGEND -> handlePreisAbsteigend();
            case CMD_BESTAND_AUFSTEIGEND -> handleBestandAufsteigend();
            case CMD_BESTAND_ABSTEIGEND -> handleBestandAbsteigend();
            case CMD_DATUM_AUFSTEIGEND -> handleDatumAufsteigend();
            case CMD_DATUM_ABSTEIGEND -> handleDatumAbsteigend();
            default -> System.err.println("Ungueltige Anfrage empfangen!");
        }
    }

    private void handleGetLoggedInCustomer() {
        Kunde k = shop.getLoggedInCustomer();
        String cmd = Commands.CMD_GET_LOGGED_IN_CUSTOMER_RESP.name();
        cmd += separator + k.getName();
        cmd += separator + k.getNummer();
        cmd += separator + k.getPasswort();
        cmd += separator + k.getStrasse();
        cmd += separator + k.getPlz();
        cmd += separator + k.getWohnort();
        socketOut.println(cmd);
    }

    private void handleGetWK(){
        Map<String, Artikel> warenkorbMap = shop.getWk();

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
        Rechnung rechnung = new Rechnung(shop.getLoggedInCustomer());

        Map<String, Artikel> warenkorbMap = shop.getWk();

        for (Artikel a : warenkorbMap.values()) {
            rechnung.gesamtpreisErhoehen(a.getPreis() * a.getBestand());
        }

        String cmd = Commands.CMD_ERZEUGE_RECHNUNG_RESP.name();
        cmd += separator + rechnung.getDatum();
        cmd += separator + rechnung.getGesamtpreis();
        Kunde kunde = rechnung.getKunde();
        cmd += separator + kunde.getName();
        cmd += separator + kunde.getStrasse();
        cmd += separator + kunde.getPlz();
        cmd += separator + kunde.getWohnort();

        socketOut.println(cmd);
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

    private void handleArtikelAnlegen(String[] data) {
        String name = data[1];
        double preis = Double.parseDouble(data[2]);
        int bestand = Integer.parseInt(data[3]);

        String cmd = Commands.CMD_ARTIKEL_ANLEGEN_RESP.name();
        try {
            if (data.length == 4) {
                shop.artikelAnlegen(name, preis, bestand);
            } else {
                int packungsgroesse = Integer.parseInt(data[4]);
                shop.artikelAnlegen(name, preis, bestand, packungsgroesse);
            }
        } catch (ArtikelExistiertBereitsException e) {
            cmd += separator + "Fehler";
        }
        socketOut.println(cmd);
    }

    private void handleBestandErhoehen(String[] data) {
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

    private void handleMitarbeiterAnlegen(String[] data) {
        String name = data[1];
        String passwort = data[2];

        String cmd = Commands.CMD_MITARBEITER_ANLEGEN_RESP.name();
        try {
            shop.mitarbeiterAnlegen(name, passwort);
        } catch (MitarbeiterExistiertBereitsException e) {
            cmd += separator + "Fehler";
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeArtikelDaten() {
        String cmd = Commands.CMD_SCHREIBE_ARTIKEL_DATEN_RESP.name();
        try {
            shop.schreibeArtikelDaten();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeMitarbeiterDaten() {
        String cmd = Commands.CMD_SCHREIBE_MITARBEITER_DATEN_RESP.name();
        try {
            shop.schreibeMitarbeiterDaten();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeKundenDaten() {
        String cmd = Commands.CMD_SCHREIBE_KUNDEN_DATEN_RESP.name();
        try {
            shop.schreibeKundenDaten();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSchreibeEreignisDaten() {
        String cmd = Commands.CMD_SCHREIBE_EREIGNIS_DATEN_RESP.name();
        try {
            shop.schreibeEreignisDaten();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketOut.println(cmd);
    }

    private void handleSucheArtikel(String[] data) {
        String titel = data[1];
        List<Artikel> result = shop.sucheArtikel(titel);

        String cmd = Commands.CMD_SUCHE_ARTIKEL_RESP.name();

        for (Artikel a : result) {
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

    private void handleSucheEreignis(String[] data){
        String titel = data[1];
        List<Ereignis> result = shop.sucheEreignis(titel);

        String cmd = Commands.CMD_SUCHE_EREIGNIS_RESP.name();

        for (Ereignis e : result) {
            cmd += separator + e.getDatum();
            cmd += separator + e.getPerson();
            cmd += separator + e.getStatus();
        }

        socketOut.println(cmd);
    }

    private void handleWarenkorbLeeren(){
        shop.warenkorbLeeren();
        String cmd = Commands.CMD_WARENKORB_LEEREN_RESP.name();

        socketOut.println(cmd);
    }

    private void handleVonAbisZ(){
        shop.vonAbisZ();
        String cmd = Commands.CMD_VON_A_BIS_Z_RESP.name();

        socketOut.println(cmd);
    }

    private void handleVonZbisA(){
        shop.vonZbisA();
        String cmd = Commands.CMD_VON_Z_BIS_A_RESP.name();
        socketOut.println(cmd);
    }

    private void handleArtikelnummerAufsteigend() {
        shop.artikelnummerAufsteigend();
        String cmd = Commands.CMD_ARTIKELNUMMER_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handleArtikelnummerAbsteigend() {
        shop.artikelnummerAbsteigend();
        String cmd = Commands.CMD_ARTIKELNUMMER_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handlePreisAufsteigend() {
        shop.preisAufsteigend();
        String cmd = Commands.CMD_PREIS_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handlePreisAbsteigend() {
        shop.preisAbsteigend();
        String cmd = Commands.CMD_PREIS_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handleBestandAufsteigend() {
        shop.bestandAufsteigend();
        String cmd = Commands.CMD_BESTAND_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handleBestandAbsteigend() {
        shop.bestandAbsteigend();
        String cmd = Commands.CMD_BESTAND_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handleDatumAufsteigend() {
        shop.datumAufsteigend();
        String cmd = Commands.CMD_DATUM_AUFSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }

    private void handleDatumAbsteigend() {
        shop.datumAbsteigend();
        String cmd = Commands.CMD_DATUM_ABSTEIGEND_RESP.name();
        socketOut.println(cmd);
    }
}
