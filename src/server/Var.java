package server;

import interfaceUsuario.Quadrado;

import java.net.Socket;
import java.util.ArrayList;


public class Var {
    public static boolean clienteConectado = false;
    public static int portUDP = 9192; // A mesma porta configurada no ESP32
    public static int portTCP = 9394;
    public static Socket currentClientSocket;
    public static String ipEsp = ""; // Preenchido pelo UDP
    public static String data = "";
    public static ArrayList<Quadrado> squares = new ArrayList<>();
    public static boolean flag = false; // flag for the connection
    public static short contFails = 0;


}
