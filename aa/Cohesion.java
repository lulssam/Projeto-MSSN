package aa;

import java.util.List;

import physics.Body;
import processing.core.PVector;

/**
 * Comportamento de Cohesion (coesão de grupo).
 *
 * Este comportamento faz com que o boid se desloque na direção
 * do centro de massa (média das posições) dos vizinhos próximos,
 * promovendo movimento em grupo e formação de enxames.
 *
 * Apenas os vizinhos dentro de neighborRadius são considerados.
 * O resultado é devolvido como steering (desired - vel),
 * sendo depois ponderado e combinado no Boid.
 */

public class Cohesion extends Behavior {

    private final List<? extends Body> neighbors;  //lista de vizinhos considerados
    private final float neighborRadius;   //raio de influencia dos vizinhos

    public Cohesion(List<? extends Body> neighbors, float neighborRadius, float weight) {
        super(weight);
        this.neighbors = neighbors;
        this.neighborRadius = neighborRadius;
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
    	PVector center = new PVector(0, 0);
        int count = 0;

        for (Body b : neighbors) {
            if (b == me) {continue;}  //ignora o proprio boid

            float d = PVector.dist(me.getPos(), b.getPos());
            if (d > 0 && d < neighborRadius) {
                center.add(b.getPos());  //acumula posições dos vizinhos
                count++;
            }
        }

        if (count == 0) { return new PVector(0, 0);} //sem vizinhos proximos, não ha força de coesão

        center.div(count); //centro de massa dos vizinhos

        //desired velocity aponta para o centro do grupo
        PVector desired = PVector.sub(center, me.getPos());
        if (desired.magSq() > 0.00001f) {
            desired.normalize();
            desired.mult(me.dna.maxSpeed);
        }

        //steering = desired - velocidade atual
        PVector steer = PVector.sub(desired, me.getVel());
        if (steer.mag() > me.dna.maxForce) { steer.setMag(me.dna.maxForce);} //limita a forca maxima

        return steer;
    }
}
