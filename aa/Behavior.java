package aa;

import processing.core.PVector;

/**
 * Classe abstrata que é a base para todos os comportamentos.
 * Guarda o peso e define a interface
 */
public abstract class Behavior implements IBehaviour {
    protected float weight;

    public Behavior(float weight) {
        this.weight = weight;
    }

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
