package server.net;

import common.IShopVerwaltung;
import server.domain.ShopVerwaltung;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ShopServer {
    public static void main(String[] args) throws IOException {
        IShopVerwaltung shop = new ShopVerwaltung("Shop");

        ServerSocket ss = new ServerSocket(1399);
        System.out.println("Server laeuft und wartet auf eingehende Verbindungen!");

        while(true) {
            Socket s = ss.accept();

            ClientRequestProcessor c = new ClientRequestProcessor(s, shop);

            Thread t = new Thread(c);
            t.start();

            System.err.println("Client verbunden!");
        }
    }
}
