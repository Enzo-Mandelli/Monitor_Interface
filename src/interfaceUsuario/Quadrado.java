package interfaceUsuario;
import processing.core.PApplet;
public class Quadrado {
    PApplet p;
    float parametro = 1;
    public int largura;
    public int altura;
    public int spacing = 3;
    public int dentroEspaco = 20;
    public int ladoQuadrado;
    public String valorVar = "valor";
    public String nomeVar = "Var";
    public String slotValor = "Place Holder";
    public String pointer;
    public String type;
    Quadrado(PApplet pa, float parametro) {
        this.p = pa;
        this.parametro = parametro;
        largura = PApplet.round(p.displayWidth*parametro) ;
        altura = PApplet.round(p.displayHeight*parametro);
        spacing = 3;
        this.ladoQuadrado = PApplet.round((largura*0.7f)/3);
    }

    public void display(int x, int y) {
        p.textSize(p.round(ladoQuadrado*0.15f));
        dentroEspaco = p.round(ladoQuadrado*0.1f);
        p.fill(0);
        p.strokeWeight(5);
        p.stroke(255);
        p.square(x, y, ladoQuadrado);
        p.fill(255);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.text(valorVar, x+ladoQuadrado/2, y+ladoQuadrado/2-dentroEspaco*2);
        p.strokeWeight(4);
        p.stroke(255);
        p.line(x+dentroEspaco,y+ladoQuadrado/2,x+ladoQuadrado - dentroEspaco,y+ladoQuadrado/2);
        p.textSize(p.round(ladoQuadrado*0.1f));
        p.text(nomeVar, x+ladoQuadrado/2, y+(ladoQuadrado/2)+dentroEspaco*2);
        p.textSize(p.round(ladoQuadrado*0.03f));
        p.text("envia dados",x+ladoQuadrado/5, y+(ladoQuadrado/2)+dentroEspaco*4 );
        p.fill(0);
        p.stroke(255);
        p.rect(x+ladoQuadrado/3, y+(ladoQuadrado/2)+dentroEspaco*3.7f, ladoQuadrado/2, p.round(ladoQuadrado*0.06f), p.round(ladoQuadrado*0.06f));
        p.fill(255);
        p.text(slotValor, x+p.round((ladoQuadrado/3 + (ladoQuadrado/2)/2)), y+p.round((ladoQuadrado/2)+dentroEspaco*3.7f + (ladoQuadrado*0.06f)/2 ));
    }



}
