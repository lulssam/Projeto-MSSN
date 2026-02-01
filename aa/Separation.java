package aa;


import java.util.List;

import physics.Body;
import processing.core.PVector;

/**
 * Comportamento de Separation (separação entre agentes).
 *
 * Este comportamento faz com que o boid se afaste de vizinhos
 * demasiado próximos, evitando sobreposição e aglomeração excessiva.
 *
 * A força de separação é proporcional à proximidade:
 *  - quanto mais perto o vizinho, maior a contribuição para o steering
 *
 * O resultado é devolvido como steering (desired - vel),
 * sendo posteriormente ponderado e combinado com outros comportamentos
 * no Boid.
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
            if (b == me) { continue;}  //ignora o proprio boid

            float d = PVector.dist(me.getPos(), b.getPos());

            if (d > 0 && d < desiredSeparation) {
                PVector diff = PVector.sub(me.getPos(), b.getPos());  //vetor que aponta para longe do vizinho
                diff.normalize();
                diff.div(d);   //quanto mais perto, maior a forca
                steer.add(diff);
                count++;
            }
        }

        if (count > 0) { steer.div(count);}  //media das contribuicoes

        //steering
        if (steer.magSq() > 0.00001f) {
            steer.normalize();
            steer.mult(me.dna.maxSpeed);  //velocidade desejada
            steer.sub(me.getVel());  //steering = desired - vel
        }

        return steer;  //peso aplicado externamente no boid
    }
}
