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
    PImage logoError;
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
    boolean conected = false;
    String data = "";
    int[] backgroundColor = {0,0,0};
    int[] textColor = {255,255,255};
    int page = 1;
    int totalPorPagina = 9; // 3x3
    int inicioIndice = 0;
    int quadradoEditado = 0;
    UnecessaryLoading unecessaryLoading = new UnecessaryLoading(this, backgroundColor, textColor, altura, largura);
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
        server.startSystem();
        fonte = createFont("src/interfaceUsuario/resources/fonteUltrakill.ttf", 16);
        logoError = loadImage("src/interfaceUsuario/resources/logoError.jpg");
        quadrado = new Quadrado(this, 0.6f);
    }

    ArrayList<Quadrado> quadrados = new ArrayList<Quadrado>();
    public void updateQuadrados(){
        //[names] + [values] + [types] + [pointer]
        data = server.getData();
        if(data.isEmpty() || data.isBlank())return;
        quantQuadrados = DataFiltering.contVar(data);
        String[] nomes = DataFiltering.sliceStr(data,0);
        String[] valor = DataFiltering.sliceStr(data,1);
        String[] tipos = DataFiltering.sliceStr(data,2);
        String[] ponteiros = DataFiltering.sliceStr(data,3);
        for(int i = 0; i < quantQuadrados; i++){
            if(quantQuadrados != quadrados.size()){
                quadrados.add(new Quadrado(this, 0.6f));
            }
            quadrados.get(i).nomeVar = nomes[i];
            quadrados.get(i).valorVar = valor[i];
            quadrados.get(i).type = tipos[i];
            quadrados.get(i).pointer = ponteiros[i];
        }
    }


    @Override
    public void draw() {
        if(!Var.clienteConectado) {
            if(conected){
                unecessaryLoading.disconnect(logoError);
                conected = false;
            }
            server.startSystem();
            textFont(fonte);
            if (!unecessaryLoading.concluido) {
                unecessaryLoading.display();
                if (!unecessaryLoading.check1 && unecessaryLoading.conectado) unecessaryLoading.printaPontosCPU();
                if (unecessaryLoading.check1) unecessaryLoading.printaPontosGPU();
            }
            delay(1000);
        }else{
            conected = true;
            updateQuadrados();
            margemQuadrado = round(largura * 0.015f);
            background(0, 0, 0);
            fill(0);
            stroke(255);
            //rect(vertice superior esquedo x, vertice superior esquedo y, largura, altura)
            rect((largura - round(largura * 0.2f)), 0, largura * 0.2f, altura);
            desenhaBotao();
            int cont = 0;
            for (int j = 0; j < 3; j++) { // Linhas
                for (int i = 0; i < 3; i++) { // Colunas
                    if (cont < quantQuadrados) {
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
                    cont++;
                }
            }

            if (mousePressed) {
                digitaDadao();
                clicaBotao();
            }
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



