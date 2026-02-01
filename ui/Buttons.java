package ui;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Classe responsável pela representação e comportamento de botões da interface.
 *
 * Cada botão gere:
 *  - posição e dimensões
 *  - deteção de hover do rato
 *  - animações suaves de hover (escala, glow e opacidade)
 *  - renderização visual consistente com o estilo do jogo
 *
 * As animações usam interpolação temporal (suavização exponencial) com base em dt,
 * garantindo transições fluidas e independentes do framerate.
 *
 * Esta classe é usada em vários estados do jogo (menu, options, game over),
 * promovendo reutilização e consistência visual na UI.
 */

public class Buttons {
    private float x, y, w, h;
    private String label;
    
    private float hoverT = 0f;
    
    private float textSize = 18;
    private boolean hoverEnabled = true;
    

    public Buttons(float x, float y, float w, float h, String label) {
        this.x = x; 
        this.y = y; 
        this.w = w; 
        this.h = h;
        
        this.label = label;
    }
    
    public void update(PApplet p, float dt) {
    	 if (!hoverEnabled) return;
    	
        boolean hover = isHover(p.mouseX, p.mouseY);
        float target = hover ? 1f : 0f;

        float k = 10f; //velocidade da animação (maior = mais rapido)
        hoverT += (target - hoverT) * (1f - (float)Math.exp(-k * dt));  //suavizacao exponencial para transicao independente do framerate
    }

    public void display(PApplet p) {
        float r = 10; //raio dos cantos arredondados
        int neon = p.color(0, 255, 0);  //cor base do estilo neon
     
        float scale = hoverEnabled ? (1f + 0.03f * hoverT) : 1f;
        float cx = x + w / 2f;
        float cy = y + h / 2f;
        
        p.pushStyle();
        p.rectMode(PApplet.CENTER);

        //glow em camadas para simular brilho (alpha e espessura aumentam com hoverT)
        if (hoverT > 0.01f) {
            p.noFill();
            p.stroke(neon, (int)(40 + 120 * hoverT));
            p.strokeWeight(6);
            p.rect(cx, cy, w * scale + 8, h * scale + 8, r);

            p.stroke(neon, (int)(25 + 80 * hoverT));
            p.strokeWeight(12);
            p.rect(cx, cy, w * scale + 14, h * scale + 14, r);

            p.stroke(neon, (int)(15 + 50 * hoverT));
            p.strokeWeight(18);
            p.rect(cx, cy, w * scale + 22, h * scale + 22, r);
        }

        //body
        p.noStroke();
        
        //quando hover, o body fica mais preenchido (aumenta alpha)
        p.fill(0, 0, 0, (int)(60 + 120 * hoverT));
        p.rect(cx, cy, w * scale, h * scale, r);

        //outline
        p.noFill();
        p.stroke(neon, 200);
        p.strokeWeight(3);
        p.rect(cx, cy, w * scale, h * scale, r);

        //texto
        PFont ui = AssetManager.get().font("ui");
        if (ui != null) p.textFont(ui);

        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(textSize); 

        p.fill(neon, (int)(180 + 75 * hoverT)); //texto mais brilhante no hover
        p.text(label, cx, cy - 1);

        p.popStyle();
        p.rectMode(PApplet.CORNER);
    }
    
    //apenas verifica se está em hover
    public boolean isClicked(PApplet p) {
        return isHover(p.mouseX, p.mouseY);
    }

    private boolean isHover(float mx, float my) {
        return (mx >= x && mx <= x + w && my >= y && my <= y + h);
    }
    
    public void setTextSize(float size) {
        this.textSize = size;
    }

    public void setHoverEnabled(boolean enabled) {
        this.hoverEnabled = enabled;
        if (!enabled) hoverT = 0f;  //reset imediato para evitar ficar preso em estado de hover
    }
    
    public void setBounds(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
