package aa;

import physics.Body;
import processing.core.PVector;

//Classe reponsável por representar o comportamento pursuit do boid, detetando alvos

public class Pursuit extends Behavior {
    public Pursuit(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        if (me.eye == null) {
            System.out.println("eye esta null");
            return new PVector(0, 0);
        }

        Body bodyTarget = me.eye.target; //obtem o alvo detetado pelo boid
        
        //previsão da pos do player
        PVector future = bodyTarget.getVel().copy().mult(me.dna.deltaTPursuit);
        PVector target = PVector.add(bodyTarget.getPos(), future);
        
        //desired velocity
        PVector desired = PVector.sub(target, me.getPos());
        
        //desired.y = 0;
        
        //steering
        if (desired.magSq() > 0.00001f) {
            desired.normalize();
            desired.mult(me.dna.maxSpeed); //maxSpeed
        }
        
        //steering = desired - vel atual
        PVector steer = PVector.sub(desired, me.getVel());
        return steer;
    }
}