package aa;

/**
 * Classe DNA onde se encontram parâmetros genéticos do boid (velocidade, força, visão, etc.)
 */

public class DNA {
    public float maxSpeed, maxForce, visionDistance, visionSafeDistance, visionAngle, deltaTPursuit, radiusArrive,
            deltaTWander, radiusWander, deltaPhiWander;

    public DNA() {
        maxSpeed = random(3, 5);
        maxForce = random(4, 7);

        visionDistance = random(1, 1);
        visionSafeDistance = 0.25f * visionDistance;
        visionAngle = (float) Math.PI * 0.8f;

        deltaTPursuit = random(0.5f, 1f);

        radiusArrive = random(3, 5);

        deltaTWander = random(0.5f, 1.2f);
        radiusWander = random(2, 3);
        deltaPhiWander = (float) Math.PI / 8;

    }

    public static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random()); //gera um valor aleatório entre min e max
    }

}