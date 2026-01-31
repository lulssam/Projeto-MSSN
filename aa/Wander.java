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
        PVector centro = me.getPos().copy();  //centro da projeção à frente
        
        if (me.getVel().magSq() > 0.00001f) {
            PVector forward = me.getVel().copy().normalize();
            forward.mult(me.dna.deltaTWander);
            centro.add(forward);
        }
        
        //ponto no circulo do wander
        PVector offset = new PVector(me.dna.radiusWander * (float) Math.cos(me.phiWander), me.dna.radiusWander * (float) Math.sin(me.phiWander));

        PVector target = PVector.add(centro, offset);
        
        me.phiWander += (float)(2 * (Math.random() - 0.5) * me.dna.deltaPhiWander);
        
        //desired velocity
        PVector desired = PVector.sub(target, me.getPos());
        if (desired.magSq() > 0.00001f) {
            desired.normalize();
            desired.mult(me.dna.maxSpeed);
        }
        
        //steering
        return PVector.sub(desired, me.getVel());
    }
}
