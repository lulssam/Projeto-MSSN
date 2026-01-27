package aa;

import processing.core.PVector;

/**
 * interface base dos comportamentos, método de obter a velocidade desejada e gerir pesos
 */
public interface IBehaviour {
    /**
     * Velocidade que o comportamento quer que o boid siga
     *
     * @param me Boid onde comportamento é aplicado
     * @return PVector com velocidade desejada
     */
    PVector getDesiredVelocity(Boid me);

    /**
     * Get peso do comportamento
     *
     * @return float com peso do comportamento.
     */
    float getWeight();

    /**
     * Set peso do comportamento
     *
     * @param weight peso a aplicar
     */
    void setWeight(float weight);
}
