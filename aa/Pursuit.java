package aa;

import physics.Body;
import processing.core.PVector;

/**
 * Comportamento de Pursuit (perseguição preditiva).
 *
 * Este comportamento faz com que o boid persiga um alvo antecipando
 * a sua posição futura, em vez de seguir apenas a posição atual.
 *
 * A previsão é calculada com base:
 *  - na velocidade atual do alvo
 *  - num horizonte temporal deltaTPursuit definido no DNA
 *
 * O resultado é devolvido como steering (desired - vel),
 * sendo posteriormente limitado e aplicado pelo boid.
 */

public class Pursuit extends Behavior {
    public Pursuit(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
    	
    	//sem sistema de visao, nao ha alvo a perseguir
        if (me.eye == null) {
            System.out.println("eye esta null");
            return new PVector(0, 0);
        }

        Body bodyTarget = me.eye.target; //obtem o alvo detetado atualmente pelo boid
        
        //previsao da posicao futura do alvo com base na sua velocidade
        PVector future = bodyTarget.getVel().copy().mult(me.dna.deltaTPursuit);
        PVector target = PVector.add(bodyTarget.getPos(), future);
        
        //desired velocity aponta para a posicao prevista
        PVector desired = PVector.sub(target, me.getPos());
        
        //desired.y = 0;
        
        //steering
        if (desired.magSq() > 0.00001f) {
            desired.normalize();
            desired.mult(me.dna.maxSpeed); //maxSpeed
        }
        
        //steering = desired - velocidade atual
        PVector steer = PVector.sub(desired, me.getVel());
        return steer;
    }
}