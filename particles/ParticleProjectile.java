package particles;

import processing.core.PApplet;
import processing.core.PVector;


/**
 * Classe que representa uma partícula visual temporária associada a projéteis.
 *
 * Uma ParticleProjectile é utilizada exclusivamente para efeitos visuais como:
 *  - fogo dos disparos
 *  - trilhos de energia
 *  - explosões simples e dissipação
 *
 * Cada partícula possui:
 *  - posição e velocidade próprias
 *  - tempo de vida limitado
 *  - tamanho base com variação visual (flicker)
 *
 * Ao longo do tempo, a partícula:
 *  - perde velocidade (efeito de dissipação/exponencial)
 *  - diminui de tamanho
 *  - perde opacidade progressivamente
 *  - desaparece quando o tempo de vida termina
 *
 * Esta classe é puramente visual e não contém lógica de colisão ou gameplay.
 */


public class ParticleProjectile {
    PVector pos, vel;
    float life, maxLife;  //tempo de vida atual e total (segundos)
    private float baseSize;
    private float seed;
    private int color;

    public ParticleProjectile(PVector pos, PVector vel, float size, float life, int color) {
        this.pos = pos.copy();
        this.vel = vel.copy();
        this.baseSize = size;
        this.life = life;
        this.maxLife = life;
        this.seed = (float) Math.random() * 1000f;  //seed para flicker visual nao sincronizado
        this.color = color;
    }

    public void update(float dt) {
        pos.add(PVector.mult(vel, dt)); //movimento simples da particula

        //dissipacao exponencial da velocidade (fogo/fumo a "morrer")
        vel.mult((float) Math.exp(-3f * dt));

        life -= dt;
    }

    public boolean dead() {
        return life <= 0;
    }

    public void display(PApplet p) {
        float t = PApplet.constrain(life / maxLife, 0, 1); //fator de vida normalizado (1 -> 0)

        //flicker sinusoidal para efeito de chama instavel
        float flicker = 0.75f + 0.25f * (float) Math.sin(seed + p.frameCount * 0.35f);

        //tamanho diminui progressivamente com o tempo de vida
        float s = baseSize * (0.6f + 0.8f * t) * flicker;

        float alpha = 255 * t;  //fade-out progressivo

        p.pushStyle();
        p.noStroke();
        p.fill(color, alpha);
        p.circle(pos.x, pos.y, s);
        p.popStyle();
    }
}