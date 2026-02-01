package aa;

/**
 * Classe DNA que agrega os parâmetros genéticos de um boid.
 *
 * O DNA define limites e características comportamentais do boid, como:
 *  - velocidade máxima e força máxima
 *  - parâmetros de visão (distância, ângulo e zona segura)
 *  - parâmetros temporais usados em behaviors (pursuit, wander, arrive)
 *
 * Os valores são inicializados de forma aleatória dentro de intervalos
 * pré-definidos, permitindo diversidade de comportamento entre boids.
 *
 * Esta classe não contém lógica de movimento; apenas fornece parâmetros
 * usados pelos diferentes Behavior associados ao boid.
 */

public class DNA {
	
    public float maxSpeed, maxForce;  //limites fisicos
    public float visionDistance, visionSafeDistance, visionAngle;  //parametros de visao
    public float deltaTPursuit;  //pursuit
    public float radiusArrive;  //arrive
    public float deltaTWander, radiusWander, deltaPhiWander;  //wander

    public DNA() {
        maxSpeed = random(3, 5);
        maxForce = random(4, 7);

        visionDistance = random(1, 1); //fixo, mas mantido aleatorio por consistencia
        visionSafeDistance = 0.25f * visionDistance;
        visionAngle = (float) Math.PI * 0.8f;

        deltaTPursuit = random(0.5f, 1f);

        radiusArrive = random(3, 5);

        deltaTWander = random(0.5f, 1.2f);
        radiusWander = random(2, 3);
        deltaPhiWander = (float) Math.PI / 8;

    }
    
    //gera um valor aleatorio no intervalo [min, max]
    public static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }

}