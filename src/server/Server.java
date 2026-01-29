package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /**
     * Servidor Java que fica escutando pedidos de descoberta UDP.
     * Ao receber um "DISCOVER_SERVER", ele responde para o ESP32.
     */
    public static String dataToSend;
    private static boolean udpRodando = false;
    private static boolean tcpRodando = false;

    public static void startSystem() {
        if (!udpRodando) {
            serverUDP();
            udpRodando = true;
        }
        // Inicia o TCP uma única vez
        if (!tcpRodando) {
            serverTCP();
            tcpRodando = true;
        }
    }


    public static void serverUDP() {
        // Use uma thread para o UDP também, para não travar o programa principal
        new Thread(() -> {

            try (DatagramSocket socket = new DatagramSocket(Var.portUDP)) {
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if ("DISCOVER_SERVER".equals(message)) {
                        byte[] responseData = "SERVER_ACK".getBytes();
                        DatagramPacket replyPacket = new DatagramPacket(
                                responseData, responseData.length,
                                packet.getAddress(), packet.getPort()
                        );
                        socket.send(replyPacket);
                        System.out.println("ESP32 descoberto no IP: " + packet.getAddress());

                        // Guardamos o IP do ESP32 para usar no sendData depois
                        Var.ipDoEsp = packet.getAddress().getHostAddress();
                        Var.clienteConectado = true;
                    }
            } catch (Exception e) {
                e.printStackTrace();
                udpRodando = false;
            }
        }).start();
    }

    public static void serverTCP() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(Var.portTCP)) {
                System.out.println("foi");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Var.currentClientSocket = clientSocket;
                    Var.clienteConectado = true;

                    // CRIA UMA THREAD DE LEITURA PERMANENTE
                    new Thread(() -> {
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                            String line;
                            while ((line = in.readLine()) != null) {
                                Var.data = line; // Atualiza a variável global
                            }
                            if(Var.data == null) Var.clienteConectado = false;
                        } catch (IOException e) {
                            Var.clienteConectado = false;
                        }
                    }).start();
                }
            } catch (IOException e) {
                Var.clienteConectado = false;
                tcpRodando = false;
            }
        }).start();
    }

    // O getData agora só devolve o que a Thread de leitura já buscou
    public String getData() {
        return Var.data;
    }


    public static void sendData(String data) {
        try {
            if (Var.currentClientSocket != null && !Var.currentClientSocket.isClosed()) {
                PrintWriter out = new PrintWriter(Var.currentClientSocket.getOutputStream(), true);
                out.println(data); // Envia o comando para o ESP32
            }
        } catch (IOException e) {
            System.err.println("Erro ao enviar: " + e.getMessage());
        }
    }

}

