package interfaceUsuario;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.ArrayList;
import server.*;
public class Main extends PApplet {
    public PFont fonte;
    int largura = displayWidth;
    int altura = width;
    String debuga = "x: " + mouseX + " y: " + mouseY;
    int tamBotao; //tamanho do botão de subir ou descer
    int alturaUtil = 20; //onde começa a area utilizavel da janela
    int[] pontoInicialBotao = new int[2]; // pos inicial do canto superior esquerdo do botao de subir
    Quadrado quadrado;
    int margemQuadrado;
    int quantQuadrados = 0;
    int quantMonitor = 1;
    Server server = new Server();
    String data;
    int page = 1;
    int totalPorPagina = 9; // 3x3
    int inicioIndice = page * totalPorPagina;
    int quadradoEditado = 0;
    public static void main(String[] args) {
        PApplet.main("interfaceUsuario.Main");
    }

    @Override
    public void settings() {
        largura = displayWidth;
        altura = displayWidth;
        largura = round(largura*0.6f);
        altura = round(altura*0.5f);
        size(largura , altura);
    }
    @Override
    public void setup() {
        fonte = createFont("src/interfaceUsuario/resources/fonteUltrakill.ttf", 16);
        quadrado = new Quadrado(this, 0.6f);
        textFont(fonte);
        while(Var.clienteConectado) server.serverTCP(); //garante que só inicia ao conectar
        data = server.getData();
        quantQuadrados = DataFiltering.contVar(data);
    }

    ArrayList<Quadrado> quadrados = new ArrayList<Quadrado>();
    public void updateQuadrados(){
        //[names] + [values] + [types] + [pointer]
        String dados = Var.data;
        String[] nomes = DataFiltering.sliceStr(dados,0);
        String[] valor = DataFiltering.sliceStr(dados,1);
        String[] tipos = DataFiltering.sliceStr(dados,2);
        String[] ponteiros = DataFiltering.sliceStr(dados,3);
        for(int i = 0; i < quantQuadrados; i++){
            quadrados.add(new Quadrado(this, 0.6f));
            quadrados.get(i).nomeVar = nomes[i];
            quadrados.get(i).valorVar = valor[i];
            quadrados.get(i).type = tipos[i];
            quadrados.get(i).pointer = ponteiros[i];
        }
    }


    @Override
    public void draw() {
        updateQuadrados();

        if(Var.clienteConectado) Server.serverUDP();
        margemQuadrado = round(largura*0.015f);
        background(0,0,0);
        fill(0);
        stroke(255);
        //rect(vertice superior esquedo x, vertice superior esquedo y, largura, altura)
        rect((largura-round(largura*0.2f)), 0, largura*0.2f, altura);
        desenhaBotao();
        int cont = 0;
        for (int j = 0; j < 3; j++) { // Linhas
            for (int i = 0; i < 3; i++) { // Colunas

                int indiceAtual = inicioIndice + (j * 3 + i);

                if (indiceAtual < quadrados.size()) {
                    // Cálculo do X: Margem inicial + (i * (Lado + Espaçamento))
                    int posX = margemQuadrado + (i * (quadrado.ladoQuadrado + margemQuadrado));

                    // Cálculo do Y: Altura inicial + (j * (Lado + Espaçamento))
                    int posY = alturaUtil + (j * (quadrado.ladoQuadrado + margemQuadrado));

                    // Renderização
                    quadrados.get(indiceAtual).display(posX, posY);

                }
            }
        }

        if(mousePressed){
            digitaDadao();
            clicaBotao();
        }
    }

    void clicaBotao(){
        if(isWithin(pontoInicialBotao[0], pontoInicialBotao[1] ,pontoInicialBotao[0]+tamBotao,  pontoInicialBotao[1] + tamBotao)){
            if(9+page*3 < quantQuadrados)page++;
        }
        if(isWithin(pontoInicialBotao[0], altura-tamBotao, pontoInicialBotao[0] + tamBotao,  altura)){
            if(page>0)page--;
        }
    }

    void digitaDadao(){
        for(int i = 0; i < quantQuadrados; i++){
            int[] pointsSlot = quadrados.get(i).getPoints();
            System.out.println(pointsSlot[2] + " " + pointsSlot[3]);
            if(isWithin(
            pointsSlot[0],
            pointsSlot[1],
            pointsSlot[2],
            pointsSlot[3]
            )) {
                quadradoEditado = i;
            }
        }
    }


    void desenhaBotao(){
        pontoInicialBotao = new int[]{(largura - round(largura * 0.2f)) - tamBotao, alturaUtil};
        tamBotao = round(altura*0.05f);
        square(pontoInicialBotao[0], pontoInicialBotao[1], tamBotao);
        //linhas do botao de subir ou descer
        line((pontoInicialBotao[0] + round(tamBotao*0.2f)),//x1
                (alturaUtil + tamBotao/1.5f),//y1
                (largura-round(largura*0.2f) - tamBotao/2f) ,//x2
                (alturaUtil + tamBotao/3f));//y2

        line((pontoInicialBotao[0] + tamBotao/2f),//x1
                (alturaUtil + tamBotao/3f),//y1
                largura - round(largura*0.2f) - round(tamBotao*0.2f),//x2
                (alturaUtil + tamBotao/1.5f));//y2

        square((pontoInicialBotao[0]), altura - tamBotao, tamBotao);

        line((pontoInicialBotao[0] + round(tamBotao*0.2f)),//x1
                (altura - tamBotao/1.5f),//y1
                (largura - round(largura*0.2f) - tamBotao/2f) ,//x2
                (altura - tamBotao/3f));//y2

        line((pontoInicialBotao[0] + tamBotao/2f),//x1
                (altura - tamBotao/3f),//y1
                largura - round(largura*0.2f) - round(tamBotao*0.2f),//x2
                (altura - tamBotao/1.5f));//y2
    }

    boolean isWithin(int x1, int y1, int x2, int y2){
        boolean isWithin = false;
        if((mouseX > x1 && mouseX < x2) && (mouseY > y1 && mouseY < y2)) isWithin = true;
        return isWithin;
    }

    void debug(){
        debuga = "x: " + mouseX + " y: " + mouseY;
        textSize(20);
        fill(255);
        text(debuga, (largura/4), (altura/4));
        stroke(255);
        line(0,mouseY,largura, mouseY);
        line(mouseX,0,mouseX, altura);
    }// mostra a posição do mouse


    //PRECISA SER ADAPTADO PARA O USO NESSE CÓDIGO!!!
    public void keyPressed() { // metodo que detecta digitação
        if(quadradoEditado == -1) return;
        String text = quadrados.get(quadradoEditado).slotValor;
        if (key == BACKSPACE) {
            if (text.length() > 0) {
                //aqui uma forma desnecessariamente complicada de apagar o ultimo caracter escrito
                String[] palavraFatiada = text.split(""); // divide a String em letras
                String palavraRemontada = "";
                for(int i = 0; i < palavraFatiada.length-1; i++){ // ignora a ultima letra assim a deletando da frase
                    palavraRemontada = palavraRemontada + palavraFatiada[i];
                }
                text = palavraRemontada;
            }
        }else if (key != ESC) {
            text = text + key;
        }else if(key == ESC){
            quadradoEditado = -1;
        }
        quadrados.get(quadradoEditado).slotValor = text;
    }//detecta a tecla pressionada

}




/*

package src.Interface;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import src.dataHandler.ComunicacaoDados;
import src.server.ServerCreate;
import src.server.Var;
import src.server.dataFilter;

import java.io.IOException;

public class Main extends PApplet {

    int largura = 960;
    byte posEsp32 = Var.posEsp32; //o index na arraylist do esp32 desejado
    int altura = 600;
    int alturaImagem = 158;
    int larguraImagem = 432;
    int[] pXTextBox = {462-240, 740-240};
    int[] pyTextBox = {194, 384};
    int larguraTxt = 40;
    int alturaTxt = 20;
    int posXEncoder = 516-240;
    int posYEncoder = 350;
    int posXaltura[] = {320, 820};
    byte indexCaixa = 0;
    int colorEnviar = 0;
    PImage imgCentral;
    PImage defaultImage;
    PImage fr;
    PImage fl;
    PImage rl;
    PImage rr;
    PImage logo;
    PImage logoError;
    String debuga = "x: " + mouseX + " y: " + mouseY;
    int[] backgroundColor = {0,0,0};
    int[] textColor = {255,255,255};
    public static ServerCreate servidor = new ServerCreate();
    TelaInicializando unecessaryLoading = new TelaInicializando(this, backgroundColor, textColor, altura,largura);
    public static Thread thread = new Thread(servidor);


    public static void main(String[] args) {
        PApplet.main("src.Interface.Main");
        thread.run();
    }

    TextBox frTxt;//1
    TextBox flTxt;//2
    TextBox rrTxt;//3
    TextBox rlTxt;//4
    public PFont fonte;
    public TextBox encoderL;
    public TextBox encoderR;
    public String acelerometro = "";
    public TextBox alturaFR;
    public TextBox alturaFL;
    public TextBox alturaRR;
    public TextBox alturaRL;
    public String alturaFMeio = "";
    public String alturaRMeio = "";
    public String sensorDistFL = "";
    public String sensorDistFr = "";
    public String sensorDistRL = "";
    public String sensorDistRR = "";
    int[] corSist = {0,255,0};
    String sistemaOn = "Sistema ON";

    @Override
    public void settings() {
        size(largura, altura);
        frTxt = new TextBox(this,larguraTxt, alturaTxt, pXTextBox[0], pyTextBox[0], 16, "angulo");
        flTxt = new TextBox(this,larguraTxt, alturaTxt, pXTextBox[0], pyTextBox[1], 16, "angulo");
        rrTxt = new TextBox(this,larguraTxt, alturaTxt, pXTextBox[1], pyTextBox[0], 16, "angulo");
        rlTxt = new TextBox(this,larguraTxt, alturaTxt, pXTextBox[1], pyTextBox[1], 16, "angulo");
        encoderL = new TextBox(this,larguraTxt, alturaTxt, posXEncoder, posYEncoder, 16, "encoder");
        encoderR = new TextBox(this,larguraTxt, alturaTxt, posXEncoder, posYEncoder-120, 16, "encoder");
        alturaFR = new TextBox(this,larguraTxt, alturaTxt, posXaltura[0]-240, posYEncoder-120, 16, "Altura");
        alturaFL = new TextBox(this,larguraTxt, alturaTxt, posXaltura[0]-240, posYEncoder, 16, "Altura");
        alturaRR = new TextBox(this,larguraTxt, alturaTxt, posXaltura[1]-240, posYEncoder-120, 16, "Altura");
        alturaRL = new TextBox(this,larguraTxt, alturaTxt, posXaltura[1]-240, posYEncoder, 16, "Altura");
        flTxt.offset = (flTxt.offset*-1)+alturaTxt+10;
        rlTxt.offset = (rlTxt.offset*-1)+alturaTxt+10;
        encoderR.offset = (encoderR.offset*-1) + alturaTxt + 10;
        alturaFL.offset = (alturaFL.offset*-1) + alturaTxt+10;
        alturaRL.offset = (alturaRL.offset*-1) + alturaTxt+10;
    }



    @Override
    public void setup() {
        rr = loadImage("src/main/java/src/Interface/resources/RL.png");
        fr = loadImage("src/main/java/src/Interface/resources/FL.png");
        fl = loadImage("src/main/java/src/Interface/resources/FR.png");
        rl = loadImage("src/main/java/src/Interface/resources/RR.png");
        imgCentral = defaultImage = loadImage("src/main/java/src/Interface/resources/default.png");
        fonte = createFont("src/main/java/src/Interface/resources/fonteUltrakill.ttf", 16);
        logo = loadImage("src/main/java/src/Interface/resources/logo.jpg");
        logoError = loadImage("src/main/java/src/Interface/resources/logoError.jpg");
    }

    @Override
    public void draw() {
        textFont(fonte);
        if(!unecessaryLoading.concluido){
            unecessaryLoading.display();
            if(!unecessaryLoading.check1 && unecessaryLoading.conectado)unecessaryLoading.printaPontosCPU();
            if(unecessaryLoading.check1)unecessaryLoading.printaPontosGPU();
        }else {
            if(Var.clienteConectado){
                updateDados();

                background(0, 0, 0);
                background(0, 0, 0);
                image(imgCentral, (((largura - 240) / 2) - (larguraImagem / 2)), ((altura / 2) - (alturaImagem / 2)));
                display();
                if (!mouseOver()) gerenciadorImagens(indexCaixa);
                fill(corSist[0], corSist[1], corSist[2]);
                textSize(50);
                text(sistemaOn, (largura / 2) - 400, 80);
                fill(0, 0, 0);
                stroke(255);
                rect(960 - 240, 0, largura, altura);
                rect(960 - 240, 40, largura, altura);
                textSize(20);
                fill(255);
                text("Dados adicionais", 965 - 240, 35);
                textSize(16);
                text("altura media frente: " + alturaFMeio, 965 - 240, 65);
                text("altura media atras: " + alturaRMeio, 965 - 240, 95);
                text("Acelerometro: " + acelerometro, 965 - 240, 120);
                text("sensor distancia fr: " + sensorDistFr + "mm", 965 - 240, 145);
                text("sensor distancia rr: " + sensorDistRR + "mm", 965 - 240, 170);
                text("sensor distancia fl: " + sensorDistFL + "mm", 965 - 240, 195);
                text("sensor distancia rl: " + sensorDistRL + "mm", 965 - 240, 220);
                fill(colorEnviar);
                rect(largura - 200, altura - 80, 160, 40 );
                fill(255- colorEnviar);
                text("Enviar", largura - 150, altura - 55);
                colorEnviar = 0;
                image(logo, 10, altura-100);
            }else{
                unecessaryLoading.disconnect(logoError);
                delay(5000);
                unecessaryLoading.concluido = false;
                unecessaryLoading.check1 = false;
                unecessaryLoading.check2 = false;
                unecessaryLoading.cont = 0;
                unecessaryLoading.contPontos1 = 0;
                unecessaryLoading.contPontos2 = 0;
                unecessaryLoading.falseCpuCheck = "";
                unecessaryLoading.falseGpuCheck = "";
            }

        }

    }
    public void onOff(boolean on){
        if (on) {
            corSist[0] = 0;
            corSist[1] = 255;
            corSist[2] = 0;
            sistemaOn = "Sistema ON";
        }else{
            corSist[0] = 255;
            corSist[1] = 0;
            corSist[2] = 0;
            sistemaOn = "Sistema OFF";
        }
    }


    void updateDados(){
        if(Var.dados.size() > 0) {
            String[] itens = Var.dados.get(posEsp32).split(",");
            encoderL.text = itens[0];
            encoderR.text = itens[1];
            acelerometro = itens[7];
            alturaFR.text = itens[2];
            alturaFL.text = itens[3];
            alturaRR.text = itens[4];
            alturaRL.text = itens[5];
            sensorDistFL = itens[8];
            sensorDistFr = itens[9];
            sensorDistRL = itens[10];
            sensorDistRR = itens[11];

            try {
                alturaFMeio = String.valueOf((Integer.valueOf(itens[2]) + Integer.valueOf(itens[3]) / 2));
                alturaRMeio = String.valueOf((Integer.valueOf(itens[4]) + Integer.valueOf(itens[5]) / 2));
            } catch (Exception e) {
                alturaFMeio = "NaN";
                alturaRMeio = "NaN";
            }
        }


        //Ordem texto
        /*
        0 EncoderR
        1 EncoderL
        2 AlturaFR
        3 AlturaFL
        4 AlturaRR
        5 AlturaRL
        6 Acelerometro
        8 distFL
        9 distFR
        10 distRl
        11 distRR


    }

public void updateText(){
    dataFilter data = new dataFilter();
    data.preencheArray();
    data.setParametros();
    if(dataFilter.encoderR){
        encoderR.fill(0,255,0);
    }else{
        encoderR.fill(backgroundColor[0],backgroundColor[1],backgroundColor[2]);
    }
    if(dataFilter.encoderL){
        encoderL.fill(0,255,0);
    }else{
        encoderL.fill(backgroundColor[0],backgroundColor[1],backgroundColor[2]);
    }
    sensorDistFr = dataFilter.alturaFR;
    sensorDistFL = dataFilter.alturaFL;
    sensorDistRR = dataFilter.alturaRR;
    sensorDistRL = dataFilter.alturaRL;

}

public void preparaArgumentoEnvio(){
    try{
        String[] argumento = new String[5];
        argumento[0] = frTxt.text + ",";
        argumento[1] = flTxt.text + ",";
        argumento[2] = rrTxt.text + ",";
        argumento[3] = rlTxt.text + ",";
        argumento[4] = ";";
        for(int i = 0; i < argumento.length; i++){
            if(argumento[i].equals("")){
                argumento[i] = "v,";
            }
        }
        ComunicacaoDados.enviaDados(argumento);
    } catch (Exception e) {
        System.out.println(e);
    }
}

void display(){
    frTxt.display();
    flTxt.display();
    rrTxt.display();
    rlTxt.display();
    encoderL.display();
    encoderR.display();
    alturaFR.display();
    alturaRR.display();
    alturaFL.display();
    alturaRL.display();
}//gerencia a exibição dos objetos em tela

boolean mouseOver(){
    boolean estaSobCaixa = true;
    int indexCaixa = this.indexCaixa;
    if(frTxt.isMouseOver()){
        indexCaixa = 1;
    }else if(flTxt.isMouseOver()){
        indexCaixa = 2;
    }else if(rlTxt.isMouseOver()){
        indexCaixa = 3;
    }else if(rrTxt.isMouseOver()) {
        indexCaixa = 4;
    }else{
        indexCaixa = 0;
        estaSobCaixa = false;
    }
    gerenciadorImagens(indexCaixa);
    return estaSobCaixa;
}//retorna se o mouse está sobre determinados pontos

void gerenciadorImagens(int indexCaixa){
    switch (indexCaixa){
        case 1:
            imgCentral = fr;
            break;
        case 2:
            imgCentral = fl;
            break;
        case 3:
            imgCentral = rl;
            break;
        case 4:
            imgCentral = rr;
            break;
        default:
            imgCentral = defaultImage;
    }
}//altera a imagem a ser exibida


@Override
public void mouseClicked() {
    if(frTxt.isMouseOver()){
        indexCaixa = 1;
    }else if(flTxt.isMouseOver()){
        indexCaixa = 2;
    }else if(rlTxt.isMouseOver()){
        indexCaixa = 3;
    }else if(rrTxt.isMouseOver()){
        indexCaixa = 4;
    }else{
        indexCaixa = 0;
    }
    if( altura-40 > mouseY && mouseY > altura- 80 ){
        if(mouseX > largura-200 && mouseX < largura - 40){
            colorEnviar = 255;
            preparaArgumentoEnvio();
        }
    }

}

void debug(){
    debuga = "x: " + mouseX + " y: " + mouseY;
    textSize(20);
    fill(255);
    text(debuga, largura/4, altura/4);
    line(0,mouseY,largura, mouseY);
    line(mouseX,0,mouseX, altura);
}// mostra a posição do mouse


public void keyPressed() { // metodo que detecta digitação
    String text = switch (indexCaixa) {
        case 1 -> frTxt.text;
        case 2 -> flTxt.text;
        case 3 -> rlTxt.text;
        case 4 -> rrTxt.text;
        default -> "";
    };
    if (key == BACKSPACE) {
        if (text.length() > 0) {
            //aqui uma forma desnecessariamente complicada de apagar o ultimo caracter escrito
            String[] palavraFatiada = text.split(""); // divide a String em letras
            String palavraRemontada = "";
            for(int i = 0; i < palavraFatiada.length-1; i++){ // ignora a ultima letra assim a deletando da frase
                palavraRemontada = palavraRemontada + palavraFatiada[i];
            }
            text = palavraRemontada;
        }
    }else if (key != ESC) {
        text = text + key;
    }else if(key == ESC){
        indexCaixa = 0;
        gerenciadorImagens(indexCaixa);
    }
    switch (indexCaixa){
        case 1:
            frTxt.text = text;
            break;
        case 2:
            flTxt.text = text;
            break;
        case 3:
            rlTxt.text = text;
            break;
        case 4:
            rrTxt.text = text;
            break;

    }
}//detecta a tecla pressionada

}
 */