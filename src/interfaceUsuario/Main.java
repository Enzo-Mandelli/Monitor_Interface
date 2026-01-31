package interfaceUsuario;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import server.*;
public class Main extends PApplet {
    //===== Variables =====
    int windowWidth;
    int windowHeight;
    int tamBotao; //tamanho do botão de subir ou descer
    int usableHeight = 20; //onde começa a area utilizavel da janela
    int margemQuadrado;
    int quantSquares = 0;
    short contIterations = 0;
    int quantMonitor = 1;
    int page = 1;
    int totalPorPagina = 9; // 3x3
    int firstIndex = 0;
    int squareBeingEdited = 0;
    int[] initialPointsButtom = new int[2]; // pos inicial do canto superior esquerdo do botao de subir
    int[] backgroundColor = {0,0,0};
    int[] textColor = {255,255,255};
    boolean conected = false;
    public PFont fonte;
    String data = "";
    String debuga = "x: " + mouseX + " y: " + mouseY;
    
    //===== Objects ===== 
    Server server = new Server();
    PImage logoError;
    UnecessaryLoading unecessaryLoading = new UnecessaryLoading(this, backgroundColor, textColor, windowHeight, windowWidth);

    
    
    public static void main(String[] args) {
        PApplet.main("interfaceUsuario.Main");
    } //start the processing thread

    @Override
    public void settings() {
        windowWidth = displayWidth;
        windowHeight = displayWidth;
        windowWidth = round(windowWidth *0.6f);
        windowHeight = round(windowHeight *0.5f);
        size(windowWidth, windowHeight);
    }
    
    
    @Override
    public void setup() {
        server.startSystem();
        fonte = createFont("src/interfaceUsuario/resources/fonteUltrakill.ttf", 16); //chose the font

        //currently the screen that has this image doesn't work
        logoError = loadImage("src/interfaceUsuario/resources/logoError.jpg"); //chose the error image
        
        //===== set the fix values ===== 
        margemQuadrado = round(windowWidth * 0.015f);
        windowMove(displayWidth/2 - windowWidth/2, displayHeight/50); // initialize the window close to centralised
        textFont(fonte);
    }
    
    public void updateQuadrados(){
        //[names] + [values] + [types] + [pointer]
        data = server.getData();
        if(data.isEmpty() || data.isBlank())return; // if the data is null doesn't update the data
        //get the values of the squares
        quantSquares = DataFiltering.contVar(data);
        String[] names = DataFiltering.sliceStr(data,0);
        String[] values = DataFiltering.sliceStr(data,1);
        String[] types = DataFiltering.sliceStr(data,2);
        String[] pointers = DataFiltering.sliceStr(data,3);
        //update de values of the squares
        for(int i = 0; i < quantSquares; i++){
            if(quantSquares != Var.squares.size()){
                Var.squares.add(new Quadrado(this, 0.6f));
            }
            Var.squares.get(i).varName = names[i];
            Var.squares.get(i).varValue = values[i];
            Var.squares.get(i).type = types[i];
            Var.squares.get(i).pointer = pointers[i];
        }
        
    }


    @Override
    public void draw() {
        contIterations++;
        if(!Var.clienteConectado) {
            if(conected){
                server.stopSystem();
                unecessaryLoading.disconnect(logoError);
                conected = false;
            }
            server.startSystem(); // make the connection with the esp
            //make the unecessary loading if it didnt connect yet
            if (!unecessaryLoading.concluido) {
                unecessaryLoading.display();
            }
        }else{
            conected = true; 
            updateQuadrados(); //always update the squares
            background(0, 0, 0); //set the color black
            fill(0); //set the interior of the rect black
            stroke(255); //set the border white
            rect((windowWidth - round(windowWidth * 0.2f)), 0, windowWidth * 0.2f, windowHeight);
            drawButtom(); //function that draws the buttom
            int cont = 0;
            for (int j = 0; j < 3; j++) { // Linhas
                for (int i = 0; i < 3; i++) { // Colunas
                    if (cont < quantSquares) {
                        int indiceAtual = firstIndex + (j * 3 + i);
                        if (indiceAtual < Var.squares.size()) {
                            // X position of the square
                            int posX = margemQuadrado + (i * (Var.squares.get(i).squareSize + margemQuadrado));
                            // Y position of the square
                            int posY = usableHeight + (j * (Var.squares.get(i).squareSize + margemQuadrado));
                            Var.squares.get(indiceAtual).display(posX, posY);
                        }
                    }
                    cont++;
                }
            }

            if (mousePressed) {
                for(int i = 0; i < quantSquares; i++){
                    if(Var.squares.get(i).isMouseOver()){
                        squareBeingEdited = i;
                        break;
                    }
                }
                buttomClicked();
                if(Var.squares.get(squareBeingEdited).isMouseOverSendData()){
                    Var.squares.get(squareBeingEdited).sendDataClicked();
                    server.sendData(squareBeingEdited);
                }
            }
        }
    }

    void buttomClicked(){
        if(isWithin(initialPointsButtom[0], initialPointsButtom[1] , initialPointsButtom[0]+tamBotao,  initialPointsButtom[1] + tamBotao)){
            if(firstIndex-3 >= 0) {
                firstIndex = firstIndex - 3;
                {
                    initialPointsButtom = new int[]{(windowWidth - round(windowWidth * 0.2f)) - tamBotao, usableHeight};
                    tamBotao = round(windowHeight * 0.05f);
                    fill(255);
                    square(initialPointsButtom[0], initialPointsButtom[1], tamBotao);
                    //linhas do botao de subir ou descer
                    fill(0);
                    line((initialPointsButtom[0] + round(tamBotao * 0.2f)),//x1
                            (usableHeight + tamBotao / 1.5f),//y1
                            (windowWidth - round(windowWidth * 0.2f) - tamBotao / 2f),//x2
                            (usableHeight + tamBotao / 3f));//y2

                    line((initialPointsButtom[0] + tamBotao / 2f),//x1
                            (usableHeight + tamBotao / 3f),//y1
                            windowWidth - round(windowWidth * 0.2f) - round(tamBotao * 0.2f),//x2
                            (usableHeight + tamBotao / 1.5f));//y2
                    delay(200);
                }
            }
        }
        if(isWithin(initialPointsButtom[0], windowHeight -tamBotao, initialPointsButtom[0] + tamBotao, windowHeight)){
            if(firstIndex + 9 < quantSquares){
                firstIndex = firstIndex+3;
                {
                    fill(255);
                    square((initialPointsButtom[0]), windowHeight - tamBotao, tamBotao);

                    fill(0);
                    line((initialPointsButtom[0] + round(tamBotao*0.2f)),//x1
                            (windowHeight - tamBotao/1.5f),//y1
                            (windowWidth - round(windowWidth *0.2f) - tamBotao/2f) ,//x2
                            (windowHeight - tamBotao/3f));//y2

                    line((initialPointsButtom[0] + tamBotao/2f),//x1
                            (windowHeight - tamBotao/3f),//y1
                            windowWidth - round(windowWidth *0.2f) - round(tamBotao*0.2f),//x2
                            (windowHeight - tamBotao/1.5f));//y2
                    delay(200);
                }
            }
        }
    }


    //function that draws the buttom to roll the page
    void drawButtom(){
        initialPointsButtom = new int[]{(windowWidth - round(windowWidth * 0.2f)) - tamBotao, usableHeight};
        tamBotao = round(windowHeight *0.05f);
        square(initialPointsButtom[0], initialPointsButtom[1], tamBotao);
        //linhas do botao de subir ou descer
        line((initialPointsButtom[0] + round(tamBotao*0.2f)),//x1
                (usableHeight + tamBotao/1.5f),//y1
                (windowWidth -round(windowWidth *0.2f) - tamBotao/2f) ,//x2
                (usableHeight + tamBotao/3f));//y2

        line((initialPointsButtom[0] + tamBotao/2f),//x1
                (usableHeight + tamBotao/3f),//y1
                windowWidth - round(windowWidth *0.2f) - round(tamBotao*0.2f),//x2
                (usableHeight + tamBotao/1.5f));//y2

        square((initialPointsButtom[0]), windowHeight - tamBotao, tamBotao);

        line((initialPointsButtom[0] + round(tamBotao*0.2f)),//x1
                (windowHeight - tamBotao/1.5f),//y1
                (windowWidth - round(windowWidth *0.2f) - tamBotao/2f) ,//x2
                (windowHeight - tamBotao/3f));//y2

        line((initialPointsButtom[0] + tamBotao/2f),//x1
                (windowHeight - tamBotao/3f),//y1
                windowWidth - round(windowWidth *0.2f) - round(tamBotao*0.2f),//x2
                (windowHeight - tamBotao/1.5f));//y2
    }

    boolean isWithin(int x1, int y1, int x2, int y2){
        return (mouseX > x1 && mouseX < x2) && (mouseY > y1 && mouseY < y2);
    }

    void debug(){
        debuga = "x: " + mouseX + " y: " + mouseY;
        textSize(20);
        fill(255);
        text(debuga, (windowWidth /4f), (windowHeight /4f));
        stroke(255);
        line(0,mouseY, windowWidth, mouseY);
        line(mouseX,0,mouseX, windowHeight);
    }// mostra a posição do mouse


    public void keyPressed() { // record on the field what is being typed
        if(squareBeingEdited == -1) return;//if there is no field to type on
        String text = Var.squares.get(squareBeingEdited).sendValue;
        if (key == BACKSPACE) {
            if (!text.isEmpty()) {
                //aqui uma forma desnecessariamente complicada de apagar o ultimo caracter escrito
                String[] slicedText = text.split(""); // divide a String em letras
                String rebuildString = "";
                for(int i = 0; i < slicedText.length-1; i++){ // ignora a ultima letra assim a deletando da frase
                    rebuildString = rebuildString + slicedText[i];
                }
                text = rebuildString;
            }
        }else if (key != ESC) {
            text = text + key;
        }else{
            squareBeingEdited = -1;
        }
        if(squareBeingEdited >= 0)Var.squares.get(squareBeingEdited).sendValue = text;
    }

}



