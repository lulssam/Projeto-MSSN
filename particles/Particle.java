package particles;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe que representa uma particula visual temporária
 * 
 * Uma Particle é utilizada para efeitos visuais como:
 *  - Fogo dos disparos
 *  - Trilhos de energia
 *  - Explosões simples
 * 
 * Cada particula possui:
 *  - Posição e velocidade
 *  - Tempo de vida limitado
 *  - Tamanho base e variação visual (flicker)
 * 
 * Ao longo do tempo, a particula:
 *  - Perde velocidade (efeito de dissipação)
 *  - Diminui de tamanho
 *  - Transita de cor (amarelo → laranja → vermelho)
 *  - Desaparece quando a vida termina
 * 
 * Esta classe é puramente visual e não contém lógica de colisão
 * ou gameplay
 */

public class Particle {
    PVector pos, vel;
    float life, maxLife;  //segundos
    private float baseSize;
    private float seed;

    public Particle(PVector pos, PVector vel, float size, float life) {
    	 this.pos = pos.copy();
         this.vel = vel.copy();
         this.baseSize = size;
         this.life = life;
         this.maxLife = life;
         this.seed = (float)Math.random() * 1000f;
    }

    public void update(float dt) {
    	  pos.add(PVector.mult(vel, dt));

          //fumo/fogo
          vel.mult((float)Math.exp(-3f * dt));

          life -= dt;
    }

    public boolean dead() {
        return life <= 0;
    }

    public void display(PApplet p) {
    	float t = PApplet.constrain(life / maxLife, 0, 1); // 1 -> 0

        //flicker
        float flicker = 0.75f + 0.25f * (float)Math.sin(seed + p.frameCount * 0.35f);

        //tamanho diminui ao morrer
        float s = baseSize * (0.6f + 0.8f * t) * flicker;

        //cor: amarelo -> laranja -> vermelho
        int c;
        if (t > 0.66f) c = p.color(255, 220, 120);
        else if (t > 0.33f) c = p.color(255, 140, 40);
        else c = p.color(255, 60, 0);

        float alpha = 220 * t;

        p.pushStyle();
        p.noStroke();
        p.fill(c, alpha);
        p.circle(pos.x, pos.y, s);
        p.popStyle();
    }
}