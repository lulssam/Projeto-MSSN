package physics;

import processing.core.PVector;

/**
 * Classe abstrata que representa um objeto físico genérico com movimento
 * <p>
 * Um Mover define as propriedades e comportamentos básicos necessários
 * para simular movimento no espaço, nomeadamente:
 * - Posição, velocidade e aceleração
 * - Massa e raio
 * - Aplicação de forças externas
 * - Integração temporal do movimento
 * <p>
 * O movimento é calculado através de uma integração simples
 * (Euler), adequada para simulações leves e efeitos visuais
 * no contexto do jogo.
 * <p>
 * Esta classe serve de base para entidades físicas concretas
 * (por exemplo, corpos celestes ou partículas)
 */

public abstract class Mover {
    protected PVector pos;
    protected PVector vel;
    protected PVector acc;
    protected float mass;
    protected float radius;

    protected Mover(PVector pos, PVector vel, float mass, float radius) {
        this.pos = pos.copy();
        this.vel = vel;
        this.mass = mass;
        this.radius = radius;
        acc = new PVector();
    }

    //aplicar força ao objeto
    public void applyForce(PVector force) {
        acc.add(PVector.div(force, mass));
    }

    //atualizar posição e velocidade no tempo
    public void move(float dt) {
        vel.add(PVector.mult(acc, dt));
        pos.add(PVector.mult(vel, dt));
        acc.mult(0);
    }

    public PVector getPos() {
        return pos;
    }

    public void setPos(PVector pos) {
        this.pos = pos;
    }

    public PVector getVel() {
        return vel;
    }

    public void setVel(PVector vel) {
        this.vel = vel;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
