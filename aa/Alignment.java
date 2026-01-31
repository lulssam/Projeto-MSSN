package aa;

import java.util.List;

import physics.Body;
import processing.core.PVector;

/**
 * Alignment: faz o boid alinhar a velocidade/direção com a média dos vizinhos
 */
public class Alignment extends Behavior {

    private final List<? extends Body> neighbors;
    private final float neighborRadius; //raio para considerar os vizinhos

    public Alignment(List<? extends Body> neighbors, float neighborRadius, float weight) {
        super(weight);
        this.neighbors = neighbors;
        this.neighborRadius = neighborRadius;
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
    	PVector avgVel = new PVector(0, 0);
        int count = 0;

        for (Body b : neighbors) {
            if (b == me) { continue;}

            float d = PVector.dist(me.getPos(), b.getPos());
            if (d > 0 && d < neighborRadius) {
                avgVel.add(b.getVel());
                count++;
            }
        }

        if (count == 0) { return new PVector(0, 0);}

        avgVel.div(count);

        //desired velocity (direção média)
        if (avgVel.magSq() > 0.00001f) {
            avgVel.normalize();
            avgVel.mult(me.dna.maxSpeed);
        }

        //steering = desired - current
        PVector steer = PVector.sub(avgVel, me.getVel());

        //limitar para ficar suave
        if (steer.mag() > me.dna.maxForce) { steer.setMag(me.dna.maxForce);}

        return steer;
    }
}
