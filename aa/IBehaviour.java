package aa;

import processing.core.PVector;

/**
 * Interface base para comportamentos de steering de um boid.
 *
 * Um IBehaviour define:
 *  - a velocidade desejada que o comportamento pretende aplicar ao boid
 *  - um peso associado, usado na combinação com outros comportamentos
 *
 * Os comportamentos são avaliados a cada frame e combinados através
 * de uma média ponderada no Boid, permitindo movimento
 * emergente e modular (ex: wander, seek, pursue, avoid).
 */

public interface IBehaviour {
    PVector getDesiredVelocity(Boid me); //devolve a velocidade desejada para o boid neste comportamento
    float getWeight(); //retorna o peso do comportamento
    void setWeight(float weight); //define o peso do comportamento
}