package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Var {
    public static boolean clienteConectado = false;
    public static int portUDP = 9192; // A mesma porta configurada no ESP32
    public static int portTCP = 9394;
    public static Socket currentClientSocket;
    public static String ipDoEsp = ""; // Preenchido pelo UDP
    public static String data = "";



}
