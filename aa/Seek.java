package aa;

import processing.core.PVector;

/**
 * Classe Seek representa um comportamento simples onde o boid se desloca diretamente para o alvo visto pelos seus "olhos"
 */

public class Seek extends Behavior {

    public Seek(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        if (me.eye == null || me.eye.getTarget() == null) return new PVector(0, 0);

        float targetX = me.eye.getTarget().getPos().x;
        float myX = me.getPos().x;
        float diffX = targetX - myX;

        if (Math.abs(diffX) < 1e-6f) return new PVector(0, 0);

        // velociade desejada no eixo x
        PVector desired = new PVector(diffX, 0);
        float d = desired.mag();
        if (d < 1e-6f) {
            return new PVector(0, 0); //evita divisão por zero quando já está praticamente em cima do alvo
        }

        desired.normalize(); //normaliza para ficar com direção mas sem magnitude
        //desired speed = maxSpeed * speed modifier
        desired.mult(me.dna.maxSpeed * me.getSpeed()); //ajusta a velocidade final com base no DNA e estado atual do boid
        return desired;
    }
}