package aa;


import java.util.List;

import physics.Body;
import processing.core.PVector;

/**
 * Classe Separation cria um comportamento de separação, afastando o boid de vizinhos demasiado próximos
 */

public class Separation extends Behavior {

    private final List<? extends Body> neighbors;  //lista de outros inimigos para evitar
    private final float desiredSeparation;        //distância minima entre inimigos

    
    public Separation(List<? extends Body> neighbors, float desiredSeparation, float weight) {
    	super(weight);
        this.neighbors = neighbors;
        this.desiredSeparation = desiredSeparation;
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector steer = new PVector(0, 0);
        int count = 0;

        for (Body b : neighbors) {
            if (b == me) continue;

            float d = PVector.dist(me.getPos(), b.getPos());

            if (d > 0 && d < desiredSeparation) {
                PVector diff = PVector.sub(me.getPos(), b.getPos());
                diff.normalize();
                diff.div(d);     //quanto mais perto, mais forte
                steer.add(diff);
                count++;
            }
        }

        if (count > 0) steer.div(count);

        //steering
        if (steer.magSq() > 0.00001f) {
            steer.normalize();
            steer.mult(me.dna.maxSpeed);
            steer.sub(me.getVel());
        }

        return steer; //weight é aplicado no Boid.applyBehaviors()
    }
}
