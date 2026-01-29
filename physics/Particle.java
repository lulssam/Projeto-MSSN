package physics;

import processing.core.PApplet;
import processing.core.PVector;

//Classe Particle que representa uma partícula com tempo de vida limitado e que desaparece gradualmente

public class Particle extends Mover {

    private float lifeSpan;
    private int color;
    private float timer;

    protected Particle(PVector pos, PVector vel, float radius, int color, float lifeSpan) {
        super(pos, vel, 0f, radius);
        this.color = color;
        this.lifeSpan = lifeSpan;
        timer = 0;
    }

    @Override
    public void move(float dt) {
        super.move(dt);
        timer += dt; //atualizar timer da partícula
    }

    //indicar se a partícula já devia desaparecer
    public boolean isDead() {
        return timer > lifeSpan;
    }

    public void display(PApplet p) {
        p.pushStyle();

        //calcular transparência baseada no tempo de vida
        float alpha = PApplet.map(timer, 0, lifeSpan, 255, 0);
        p.fill(color, alpha);

        p.noStroke();
        p.circle(pos.x, pos.y, 2 * radius);
        p.popStyle();
    }

}
