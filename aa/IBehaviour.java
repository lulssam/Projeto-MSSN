package aa;

import processing.core.PVector;

/**
 * Interface base dos comportamentos, método de obter a velocidade desejada e gerir pesos
 */
public interface IBehaviour {
    PVector getDesiredVelocity(Boid me); //velocidade que o comportamento quer que o boid siga
    float getWeight(); //retorna o peso do comportamento
    void setWeight(float weight); //set do peso do comportamento
}