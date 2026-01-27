package interfaceUsuario;

import processing.core.PApplet;
import processing.core.PImage;
import server.Var;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UnecessaryLoading {
    PApplet parent;
    int[] backgroundColor;
    int[] textColor;
    int altura;
    int largura;
    String clienteWaiting = "Procurando hospedeiro";
    public boolean concluido = false;
    boolean check1 = false;
    boolean check2 = false;
    boolean conectado = false;
    String falseCpuCheck = "CPU check";
    String falseGpuCheck = "GPU check";
    byte contPontos1 = 0;
    byte contPontos2 = 0;
    int cont = 0;
    byte textSize = 30;
    int x = 10;
    PImage logo;
    UnecessaryLoading(PApplet p, int[] backgroundColor, int[] textColor, int altura, int largura){
        this.parent = p;
        this.altura = altura;
        this.largura = largura;
        this. backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    void printaPontosCPU(){
        parent.text(falseCpuCheck, x, textSize * 7 + 20);
        falseCpuCheck = falseCpuCheck + ".";
        contPontos1++;
        if(contPontos1 >= 3)check1 = true;
    }

    void printaPontosGPU(){
        parent.text(falseCpuCheck, x, textSize * 7 + 20);
        parent.text(falseGpuCheck, x, textSize * 10 + 20);
        falseGpuCheck = falseGpuCheck + ".";
        contPontos2++;
        if(contPontos2 >= 3)check2 = true;
    }


    String aux = clienteWaiting;
    void display(){
        parent.background(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
        parent.textSize(textSize);
        parent.fill(textColor[0],textColor[1],textColor[2]);
        parent.text("Inicializando em: " + getIP(), x,60);
        if (!Var.clienteConectado){
            parent.delay(1000);
            parent.fill(255,0,0);
            parent.text(clienteWaiting, x, textSize*4);
            clienteWaiting = clienteWaiting + ".";
            cont++;
            if(cont > 15){
                parent.text("Falha!", x, textSize*6+20);
                clienteWaiting = aux;
                cont = 0;
            }
        }

        if(Var.clienteConectado){
            conectado = true;
            parent.fill(0,255,0);
            parent.text("Sucesso!", x, textSize*6);
            parent.fill(255, 0, 0);
            parent.delay(500);
            if(check1&&check2)concluido = true;
        }
    }

    void disconnect(PImage logo){
        parent.background(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
        parent.textSize(textSize);
        parent.fill(255,0,0);
        parent.text("houve uma casualidade envolvendo hospedeiro", x, 60);
        parent.image(logo, (((largura - 240) / 2) - (132 / 2)), (altura / 2));
    }

    private String getIP(){
        String ip = "";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Não foi possível obter o endereço IP do host local: " + e.getMessage());
        }
        return ip;
    }


}