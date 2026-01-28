package game;

import aa.Boid;
import aa.Eye;
import aa.Type;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class Enemy extends Boid {

    protected PImage sprite;
    protected int hp;
    protected float minX, maxX, minY, maxY;

    protected Enemy(PVector pos, float radius, PImage sprite, PApplet p) {
        // inicializar boid
        super(pos, 1.0f, radius, p.color(255), p, Type.PREY);
        this.sprite = sprite;

        //this.hp = 100;

        //this.minX = radius;
        //this.maxX = p.width - radius;
        this.minY = radius;
        this.maxY = p.height / 2.5f;

        //this.addBehavior(new Wander(0.5f));
        this.vel = PVector.random2D().mult(100);

        // inicializar eye
        this.eye = new Eye(this, new ArrayList<>());
    }

    /**
     * Método abstrato qie cada inigmo implementa para definir os seus comportamentos
     */
    protected abstract void initBehaviors();

    @Override
    public void applyBehaviors(float dt) {
        super.applyBehaviors(dt);
        keepVertical();
    }

    private void keepVertical() {
        if (pos.y < minY) {
            pos.y = minY;
            vel.y *= -1;
        } else if (pos.y > maxY) {
            pos.y = maxY;
            vel.y *= -1;
        }
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
