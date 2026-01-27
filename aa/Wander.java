package aa;

import processing.core.PVector;

/**
 * Implementa movimento errático suave, criando uma trajetória imprevisivel mas
 * fluida. Gera um ponto num circulo a frente do boid e avança na direção desse ponto
 */
public class Wander extends Behavior {
    public Wander(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector centro = me.getPos().copy();//centro da projeção à frente
        centro.add(me.getVel().copy().mult(me.dna.deltaTWander)); //avança o centro ao longo da direção atual

        PVector target = new PVector(
                me.dna.radiusWander * (float) Math.cos(me.phiWander),
                me.dna.radiusWander * (float) Math.sin(me.phiWander)
        );

        target.add(centro); //move o ponto do círculo para o centro de projeção
        me.phiWander += (float) (2 * (Math.random() - 0.5) * me.dna.deltaPhiWander); //altera o ângulo aleatoriamente

        return PVector.sub(target, me.getPos());
    }
}
