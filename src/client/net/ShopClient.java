package src.client.net;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

public class ShopClient {
    private Socket socket;
    private BufferedReader socketIn;
    private PrintStream socketOut;
    final String separator = ";";

}
