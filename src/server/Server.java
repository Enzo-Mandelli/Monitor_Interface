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
    private static boolean tcpRodando = false;

    public static void iniciarSistema() {
        // Inicia o TCP uma única vez
        if (!tcpRodando) {
            serverTCP();
            tcpRodando = true;
        }
        // Inicia o UDP para descoberta
        serverUDP();
    }

    public static void serverUDP() {
        // Use uma thread para o UDP também, para não travar o programa principal
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(Var.portUDP)) {
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];

                while (true) {
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
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void serverTCP() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(Var.portTCP)) {
                while (!Thread.currentThread().isInterrupted()) {
                    // Aceita o ESP32
                    Socket clientSocket = serverSocket.accept();
                    Var.clienteConectado = true;

                    // Salva o socket globalmente para o sendData poder usar depois!
                    Var.currentClientSocket = clientSocket;

                    new Thread(() -> handlesESP32(clientSocket)).start();
                }
            } catch (IOException e) { /* ... */ }
        }).start();
    }

    public String getData() {
        return Var.data;
    }

    private static void handlesESP32(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Comunicação estável com: " + clientSocket.getInetAddress());
            while ((Var.data = in.readLine()) != null) {

                // Processa o dado recebido (ex: "0x3FF...;10.5;f")
                // Se o ESP32 desconectar, o readLine retorna null e o loop acaba
            }
        } catch (IOException e) {
            System.out.println("ESP32 desconectado: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                Var.clienteConectado = false;
            } catch (IOException e) { e.printStackTrace(); }
        }
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

