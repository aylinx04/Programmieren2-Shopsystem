package src.server.net;

import src.common.Commands;
import src.common.IShopVerwaltung;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientRequestProcessor {
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
//            case CMD_GIB_EREIGNISLISTE -> handleGibEreignisListe();
            default -> System.err.println("Ungueltige Anfrage empfangen!");
        }
    }

}
