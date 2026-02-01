package aa;

import processing.core.PVector;

/**
 * Classe abstrata base para todos os comportamentos de steering.
 *
 * A classe Behavior implementa IBehaviour e fornece:
 *  - um campo comum weight para ponderação do comportamento
 *  - métodos de acesso ao peso
 *
 * As subclasses devem sobrescrever "getDesiredVelocity(Boid)"
 * para definir a velocidade desejada específica do comportamento
 * (ex: wander, seek, pursue, avoid).
 *
 * Esta classe não implementa lógica de movimento concreta.
 */

public abstract class Behavior implements IBehaviour {
    protected float weight;

    public Behavior(float weight) {
        this.weight = weight;
    }
    
    //comportamento abstrato: subclasses devem fornecer implementacao concreta
    @Override
    public PVector getDesiredVelocity(Boid me) {
        return null;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
