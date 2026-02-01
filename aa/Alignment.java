package aa;

import java.util.List;

import physics.Body;
import processing.core.PVector;

/**
 * Comportamento de Alignment (alinhamento).
 *
 * Este comportamento faz com que o boid alinhe a sua direção
 * e velocidade com a média das velocidades dos vizinhos próximos,
 * promovendo movimento coordenado do grupo.
 *
 * Apenas os vizinhos dentro de neighborRadius são considerados.
 * O resultado é devolvido como steering (desired - vel),
 * sendo posteriormente ponderado e combinado no Boid.
 */

public class Alignment extends Behavior {

    private final List<? extends Body> neighbors; //lista de vizinhos considerados
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
            if (b == me) {continue;}  //ignora o proprio boid

            float d = PVector.dist(me.getPos(), b.getPos());
            if (d > 0 && d < neighborRadius) {
                avgVel.add(b.getVel());  //acumula velocidades dos vizinhos
                count++;
            }
        }

        if (count == 0) {return new PVector(0, 0);}  //sem vizinhos proximos, nao ha forca de alinhamento

        avgVel.div(count); //velocidade media do grupo

        //desired velocity segue a direcao media dos vizinhos
        if (avgVel.magSq() > 0.00001f) {
            avgVel.normalize();
            avgVel.mult(me.dna.maxSpeed);
        }

        //steering = desired - velocidade atual
        PVector steer = PVector.sub(avgVel, me.getVel());

        //limita a forca para manter movimento suave
        if (steer.mag() > me.dna.maxForce) { steer.setMag(me.dna.maxForce);}

        return steer;
    }
}
