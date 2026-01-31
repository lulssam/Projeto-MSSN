package aa;

import physics.Body;
import processing.core.PVector;

/**
 * Classe que retrata o comportamento flee simples para fugir do alvo (move-se na direção oposta)
 */

public class Flee extends Behavior {
    public Flee(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        Body bodyTarget = me.eye.target; //vetor do boid até o alvo
        PVector vd = PVector.sub(bodyTarget.getPos(), me.getPos());
        return vd.mult(-1); //retorna a direção oposta para fugir
    }
}
