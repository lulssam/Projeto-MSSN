package game;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Classe abstrata base para todas as entidades do jogo.
 * 
 * Uma Entity representa um objeto com:
 *  - Posição, velocidade e aceleração
 *  - Raio de colisão
 *  - Representação visual através de um sprite
 * 
 * Esta classe fornece métodos genéricos de:
 *  - Aplicação de forças
 *  - Atualização do movimento ao longo do tempo
 *  - Desenho da entidade no ecrã
 * 
 * Entidades concretas como Player e Enemy herdam desta classe
 * e adicionam comportamento específico (input, ataques, etc...).
 */

public abstract class Entity {
    protected PVector pos;
    protected PVector vel;
    protected PVector acc;

    protected float radius;
    protected PImage sprite;

    public Entity(PVector pos, float radius, PImage sprite) {
        this.pos = pos.copy();
        this.vel = new PVector(0, 0);
        this.acc = new PVector(0, 0);
        this.radius = radius;
        this.sprite = sprite;
    }

    public void applyForce(PVector f) {
        acc.add(f);
    }

    public void update(float dt) {
        vel.add(PVector.mult(acc, dt));
        pos.add(PVector.mult(vel, dt));
        acc.mult(0);
    }

    public void display(PApplet p) {
        if (sprite == null) return;

        p.pushMatrix();
        p.imageMode(PConstants.CENTER);
        p.translate(pos.x, pos.y);
        p.image(sprite, 0, 0, radius * 2, radius * 2); //tamanho base
        p.popMatrix();
    }

    public PVector getPos() {return pos;}
    public PVector getVel() {return vel;}
    public float getRadius() {return radius;}

    public void setPos(float x, float y) { pos.set(x, y); }
}
