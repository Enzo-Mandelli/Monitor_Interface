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
    byte textSize = 30;
    int x;
    PImage logo;

    UnecessaryLoading(PApplet p, int[] backgroundColor, int[] textColor, int altura, int largura){

        this.parent = p;
        this.altura = altura;
        this.largura = largura;
        this. backgroundColor = backgroundColor;
        this.textColor = textColor;
        x = largura;

    }

    String aux = clienteWaiting;

    void display(){

        parent.background(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
        parent.textSize(textSize);
        parent.fill(255,0,0);
        parent.text("Inicializando em: " + getIP(), 10,60);

    }

    void disconnect(PImage logo){

        parent.background(backgroundColor[0], backgroundColor[1], backgroundColor[2]);
        parent.fill(255, 0, 0);
        parent.textSize(textSize);

        parent.textAlign(PApplet.CENTER, PApplet.CENTER);

        parent.text("Houve uma casualidade envolvendo o hospedeiro", largura / 2f, 60);

        float imgX = (largura / 2f) - (logo.width / 2f);
        float imgY = (altura / 2f) - (logo.height / 2f);

        parent.image(logo, imgX, imgY);

        parent.textAlign(PApplet.LEFT, PApplet.BASELINE);
        parent.delay(2000);
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