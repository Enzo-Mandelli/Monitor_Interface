package interfaceUsuario;
import processing.core.PApplet;
public class Quadrado {
    PApplet p;
    float proportion = 1;
    public int width;
    public int height;
    public int spacing = 3;
    public int insideSpace = 20;
    public int squareSize;
    public String varValue = "valor";
    public String varName = "Var";
    public String sendValue = "Place Holder";
    public String pointer;
    public String type;
    int x; // offset X
    int y; // offset Y
    int halfSquare;

    Quadrado(PApplet pa, float proportion) {
        this.p = pa;
        this.proportion = proportion;
        width = PApplet.round(p.displayWidth* proportion) ;
        height = PApplet.round(p.displayHeight* proportion);
        this.squareSize = PApplet.round((width *0.7f)/3);
    }

    public void display(int x, int y) {
        this.x = x;
        this.y = y;
        halfSquare = squareSize / 2;
        insideSpace = p.round(squareSize *0.1f);
        squareCreation();
        valueTextCreation();

        //===== divisor creation =====
        p.stroke(255);
        p.line(x+ insideSpace,y+ halfSquare,x+ squareSize - insideSpace,y+ halfSquare);

        varNameCreation();
        sendDataCreation();
        sendDataFieldCreation();
        drawSendDataHighlight();
    }

    public boolean isMouseOver() {
        return p.mouseX > x && p.mouseY > y && p.mouseX < x + squareSize && p.mouseY < y + squareSize;
    }

    private void sendDataCreation() {
        p.textSize(p.round(squareSize *0.03f));
        p.text("send data",x+ squareSize /5f, y+ halfSquare + insideSpace *4 );
    }

    private void squareCreation(){
        p.textSize(p.round(squareSize *0.15f));
        p.fill(0);
        p.strokeWeight(5);
        p.stroke(255);
        p.square(x, y, squareSize);

    }

    private void valueTextCreation(){
        p.fill(255);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.text(varValue, x+ halfSquare, y+ halfSquare - insideSpace *2);
        p.strokeWeight(4);
    }

    private void varNameCreation(){
        p.textSize(p.round(squareSize *0.1f));
        p.text(varName, x+ halfSquare, y+ halfSquare + insideSpace *2);
    }

    private void sendDataFieldCreation(){
        p.fill(0);
        p.stroke(255);
        p.rect(x+ squareSize/3f, y+ halfSquare + insideSpace *3.7f, halfSquare, p.round(squareSize *0.06f), p.round(squareSize *0.06f));
        p.fill(255);
        p.text(sendValue, x+ squareSize/3f, y+ halfSquare + insideSpace *3.7f, halfSquare, p.round(squareSize *0.06f));
    }

    private void drawSendDataHighlight() {
        int textSize = p.round(squareSize *0.03f);
        p.noFill();
        p.stroke(255); // Verde se o mouse estiver em cima
        p.strokeWeight(2);
        // Usamos os mesmos cálculos do campo de entrada
        p.rect(x + squareSize /5f - (3 * textSize), y+ halfSquare + insideSpace *4 - (textSize),squareSize /5f, halfSquare/10f);
    }

    public boolean isMouseOverSendData() {
        int textSize = p.round(squareSize *0.03f);

        float x1 = x + squareSize /5f - (3 * textSize);
        float y1 = y+ halfSquare + insideSpace *4 - (textSize);
        float w = squareSize /5f;
        float h = halfSquare/10f;

        return (p.mouseX >= x1 && p.mouseX <= x1 + w &&
                p.mouseY >= y1 && p.mouseY <= y1 + h);
    }

    public void sendDataClicked(){
        int textSize = p.round(squareSize *0.03f);
        p.fill(255);
        p.stroke(255);
        p.strokeWeight(2);
        p.rect(x + squareSize /5f - (3 * textSize), y+ halfSquare + insideSpace *4 - (textSize), squareSize /5f, halfSquare/10f);

        p.fill(0);
        p.textSize(p.round(squareSize *0.03f));
        p.text("send data",x+ squareSize /5f, y+ halfSquare + insideSpace *4 );
        p.delay(200);
    }

}
