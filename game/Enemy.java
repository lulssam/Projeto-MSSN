package game;

import aa.Boid;
import aa.Type;
import aa.Wander;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Enemy extends Boid {

    private PImage sprite;
    private int hp;

    protected Enemy(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, 1.0f, radius, p.color(255), p, Type.PREY);

        this.sprite = sprite;
        this.hp = 100;
        this.vel = new PVector(100, 5);
        this.addBehavior(new Wander(0.5f));
    }

    @Override
    public void display(PApplet p) {
        if (sprite != null) {
            p.pushMatrix();
            p.translate(pos.x, pos.y);

            // rodar conforme a velocidade:
            // p.rotate(vel.heading() + PConstants.PI/2);

            p.imageMode(PConstants.CENTER);
            p.image(sprite, 0, 0, radius * 2, radius * 2);
            p.popMatrix();
        } else {
            // se não houvr sprite vai so o triangulo
            super.display(p);
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isOffscreen(PApplet p) {
        return pos.y > p.height + radius;
    }
}
