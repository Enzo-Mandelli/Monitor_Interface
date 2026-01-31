package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Server {
    /**
     * Servidor Java que fica escutando pedidos de descoberta UDP.
     * Ao receber um "DISCOVER_SERVER", ele responde para o ESP32.
     */
    public static String dataToSend; // the data to send
    private static boolean udpRunning = false; //flags to determine if server thread is running
    private static boolean tcpRunning = false;
    static boolean flagStartTimer = true;

    public static void startSystem() { //makes sure that the server is started only once
        if (!udpRunning) {
            serverUDP();
            udpRunning = true;
        }

        if (!tcpRunning) {
            serverTCP();
            tcpRunning = true;
        }
        if(flagStartTimer){
            count5sec();
            flagStartTimer = false;
        }
    }


    private static void serverUDP() { // udp server is used to find the esp as quickly as possible
        new Thread(() -> { // runs the server on a thread

            try (DatagramSocket socket = new DatagramSocket(Var.portUDP)) {
                socket.setBroadcast(true); // start reading the broadcast to find the esp transmition
                byte[] buffer = new byte[1024];

                    // get and read the packet
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if ("DISCOVER_SERVER".equals(message)) {//checks if the message is the one expected
                        byte[] responseData = "SERVER_ACK".getBytes();
                        DatagramPacket replyPacket = new DatagramPacket(
                                responseData, responseData.length,
                                packet.getAddress(), packet.getPort()
                        );
                        socket.send(replyPacket); // reply the also expected answer so the esp can find the server ip
                        System.out.println("ESP32 descoberto no IP: " + packet.getAddress());

                        //save the esp IP so it is possible to send data afterward
                        Var.ipEsp = packet.getAddress().getHostAddress();
                        Var.clienteConectado = true; // change the flag to the connection in Var
                    }
            } catch (Exception e) {
                e.printStackTrace();
                udpRunning = false;
            }
        }).start();
    }

    private static void serverTCP() {//tcp is used to a stable connection
        new Thread(() -> {// runs the server on a thread
            try (ServerSocket serverSocket = new ServerSocket(Var.portTCP)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Var.currentClientSocket = clientSocket;
                    Var.clienteConectado = true;

                    // create a thread that is(almost) always reading
                    new Thread(() -> {
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                            String line;
                            while ((line = in.readLine()) != null) {
                                if (line.equals("__online__")) {
                                    Var.contFails = 0;
                                } else {
                                    Var.data = line;
                                }
                            }
                        } catch (IOException e) {
                            Var.clienteConectado = false;
                        }
                    }).start();

                }
            } catch (IOException e) {
                Var.clienteConectado = false;
                tcpRunning = false;
            }
        }).start();
    }

    public String getData() {
        return Var.data;
    }//updates data


    public static void sendData(int indexSquare) {
        //data order: [pointer][data][type]
        try {
            if (Var.currentClientSocket != null && !Var.currentClientSocket.isClosed()) {
                PrintWriter out = new PrintWriter(Var.currentClientSocket.getOutputStream(), true);
                out.println(DataFiltering.prepareStatement(indexSquare)); // Envia o comando para o ESP32
            }
        } catch (IOException e) {
            System.err.println("Erro ao enviar: " + e.getMessage());
        }
    }
    private static void checkConnection(String data) {
        //data order: [pointer][data][type]
        try {
            if (Var.currentClientSocket != null && !Var.currentClientSocket.isClosed()) {
                PrintWriter out = new PrintWriter(Var.currentClientSocket.getOutputStream(), true);
                out.println(data);
            }
        } catch (IOException e) {
            System.err.println("Erro ao enviar: " + e.getMessage());
        }
    }

    public static void stopSystem() {
        try {
            udpRunning = false;
            tcpRunning = false;

            if (Var.currentClientSocket != null) {
                Var.currentClientSocket.close(); // Fecha a conexão com o ESP
            }

            Var.ipEsp = null;
            Var.clienteConectado = false;
            System.out.println("Servidor interrompido para reinicialização...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void count5sec() {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    if (Var.clienteConectado) {
                        Var.contFails++;
                        checkConnection("__check__");

                        if (Var.contFails > 2) {
                            System.out.println("Timeout: Cliente desconectado");
                            Var.clienteConectado = false;
                            Var.contFails = -2;
                            if (Var.currentClientSocket != null) Var.currentClientSocket.close();
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }).start();
    }

}

