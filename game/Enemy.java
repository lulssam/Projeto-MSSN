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
    
    //valores para a animação de damage
    protected float damageFlashTimer = 0f;
    protected float damageFlashDuration = 0.15f; //150ms

    protected Enemy(PVector pos, float radius, PImage sprite, PApplet p) {
        //inicializar boid
        super(pos, 1.0f, radius, p.color(255), p, Type.PREDATOR);
        
        this.sprite = sprite;

        this.minY = radius;
        this.maxY = p.height / 2.5f;

        this.vel = PVector.random2D().mult(100);

        //inicializar eye
        this.eye = new Eye(this, new ArrayList<>());

    }

    //metodo abstrato que cada inimigo implementa para definir os seus comportamentos
    protected abstract void initBehaviors();

    @Override
    public void applyBehaviors(float dt) {
        super.applyBehaviors(dt);
        keepVertical();
        
        if (damageFlashTimer > 0f) { damageFlashTimer -= dt;}
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

            p.imageMode(PConstants.CENTER);
            
            //se levou dano desenha com alpha mais baixo (ou a piscar)
            p.pushStyle();
            if (damageFlashTimer > 0f) {
                //efeito "piscar": alterna entre 80 e 255
                float blink = ((int)(damageFlashTimer * 60) % 2 == 0) ? 80f : 255f;
                p.tint(255, blink);
            } else {
                p.tint(255, 255);
            }

            p.image(sprite, 0, 0, radius * 2, radius * 2);
            p.popStyle();
            p.popMatrix();
            
        } else {
            super.display(p); //se não houver sprite desenha triangulo normal
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        damageFlashTimer = damageFlashDuration; //ativa o flash (animação de levar damage)
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isOffscreen(PApplet p) {
        return pos.y > p.height + radius;
    }
}