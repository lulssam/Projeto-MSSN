package aa;

import processing.core.PVector;

/**
 * Comportamento de Wander (movimento errático suave).
 *
 * Este comportamento gera uma trajetória imprevisível mas fluida ao:
 *  - projetar um "centro" à frente do boid (na direção do movimento atual)
 *  - escolher um ponto-alvo num círculo em torno desse centro
 *  - variar gradualmente o ângulo phiWander para produzir mudança suave
 *
 * O resultado é devolvido como steering (desired - vel), que depois é limitado
 * e aplicado pelo boid.
 */

public class Wander extends Behavior {
    public Wander(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector centro = me.getPos().copy();  //centro da projeção à frente
        
        //projecao na direcao do movimento para evitar wander "parado"
        if (me.getVel().magSq() > 0.00001f) {
            PVector forward = me.getVel().copy().normalize();
            forward.mult(me.dna.deltaTWander); //distancia do circulo a frente
            centro.add(forward);
        }
        
        //offset no circulo de wander (raio fixo, angulo phiWander)
        PVector offset = new PVector(me.dna.radiusWander * (float) Math.cos(me.phiWander), me.dna.radiusWander * (float) Math.sin(me.phiWander));

        PVector target = PVector.add(centro, offset);
        
        //perturbacao pequena do angulo para manter transicao suave
        me.phiWander += (float)(2 * (Math.random() - 0.5) * me.dna.deltaPhiWander);
        
        //desired velocity aponta do boid para o target, com maxSpeed
        PVector desired = PVector.sub(target, me.getPos());
        if (desired.magSq() > 0.00001f) {
            desired.normalize();
            desired.mult(me.dna.maxSpeed);
        }
        
        //steering force = desired - velocidade atual
        return PVector.sub(desired, me.getVel());
    }
}
