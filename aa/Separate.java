package aa;

import physics.Body;
import processing.core.PVector;

/**
 * Classe Separate cria um comportamento de separação, afastando o boid de vizinhos demasiado próximos
 */

public class Separate extends Behavior {

    public Separate(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector vd = new PVector(); //vetor acumulado da direção de separação

        //itera pelos corpos próximos detetados
        for (Body b : me.eye.getNearSight()) {
            PVector r = PVector.sub(me.getPos(), b.getPos()); //vetor que aponta do vizinho para o boid
            float d = r.mag(); //distância ao vizinho
            r.div(d * d); //inverso do quadrado da distância -> mais forte quando estão colados
            vd.add(r);
        }
        return vd;
    }
}
