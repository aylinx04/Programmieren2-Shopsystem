package src.server.net;

import src.common.Artikel;
import src.common.Commands;
import src.common.Ereignis;
import src.common.IShopVerwaltung;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

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
//            case CMD_GET_WK -> handleGetWK();
//            case CMD_ERZEUGE_RECHNUNG -> handleErzeugeRechnung();
            case CMD_GIB_EREIGNISLISTE -> handleGibEreignisListe();
            case CMD_GIB_ALLE_ARTIKEL -> handleGibAlleArtikel();
            default -> System.err.println("Ungueltige Anfrage empfangen!");
        }
    }

//    private void handleGetWK(){
//        shop.getWk();
//    }
    private void handleGibEreignisListe(){
        List<Ereignis> result = shop.gibEreignisListe();

        String cmd = Commands.CMD_GIB_EREIGNISLISTE_RESP.name();

        for (Ereignis e : result) {
            cmd += separator + e.getDatum();
            cmd += separator + e.getPerson();
            cmd += separator + e.getStatus();
        }

        socketOut.println(cmd);
    }
    private void handleGibAlleArtikel(){
        List<Artikel> result = shop.gibAlleArtikel();

        String cmd = Commands.CMD_GIB_ALLE_ARTIKEL_RESP.name();

        for (Artikel a : result){
            cmd += separator + a.getName();
            cmd += separator + a.getNummer();
            cmd += separator + a.getPreis();
            cmd += separator + a.getBestand();
        }

        socketOut.println(cmd);
    }
}
