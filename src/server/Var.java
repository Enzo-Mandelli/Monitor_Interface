package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Var {
    public static boolean clienteConectado = false;
    public static int portUDP = 9192; // A mesma porta configurada no ESP32
    public static int portTCP = 9394;
    public static Socket currentClientSocket;
    static ServerSocket serverSocket;
    public static String ipDoEsp = ""; // Preenchido pelo UDP
    public static String data = "";
    static {
        try {
            serverSocket = new ServerSocket(Var.portTCP);
        } catch (IOException e) {
            System.out.println("erro no socket");
            throw new RuntimeException(e);
        }
    }


}
