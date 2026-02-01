package physics;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe que representa uma partícula física com tempo de vida limitado.
 *
 * Um {@code ParticlePhysics} herda de Mover, utilizando a sua
 * posição e velocidade, mas ignora interações físicas complexas
 * (massa é considerada nula).
 *
 * A partícula possui:
 *  - um tempo de vida finito (lifeSpan)
 *  - um temporizador interno (timer)
 *  - um efeito de fade-out visual baseado no tempo de vida
 *
 * Esta classe é usada como base para efeitos visuais (ex: poeira,
 * estrelas, highlights), não contendo lógica de colisões ou gameplay.
 */

public class ParticlePhysics extends Mover {

    private float lifeSpan;
    private int color;
    private float timer;

    protected ParticlePhysics(PVector pos, PVector vel, float radius, int color, float lifeSpan) {
        super(pos, vel, 0f, radius);  //massa zero: particula nao influencia fisica global
        this.color = color;
        this.lifeSpan = lifeSpan;
        timer = 0;
    }

    @Override
    public void move(float dt) {
        super.move(dt);
        timer += dt;  //avanca o tempo de vida da particula
    }

    //indicar se a partícula já excedeu o tempo de vida
    public boolean isDead() {
        return timer > lifeSpan;
    }

    public void display(PApplet p) {
        p.pushStyle();

        //fade-out progressivo baseado no tempo de vida
        float alpha = PApplet.map(timer, 0, lifeSpan, 255, 0);
        p.fill(color, alpha);

        p.noStroke();
        p.circle(pos.x, pos.y, 2 * radius);
        p.popStyle();
    }

}