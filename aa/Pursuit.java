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
            System.out.println("Eye está null!");
            return new PVector(0, 0);
        }

        Body bodyTarget = me.eye.target; //obtém o alvo detetado pelo boid
        //System.out.println("Enemy X: " + me.getPos().x + " | Player X: " + bodyTarget.getPos().x);
        PVector d = bodyTarget.getVel().copy().mult(me.dna.deltaTPursuit); //prevê a posição futura
        PVector target = PVector.add(bodyTarget.getPos(), d); //calcula o ponto antecipado
        PVector desired = PVector.sub(target, me.getPos());
        desired.y = 0;

        System.out.println("Pursuit desired: " + desired + " | weight: " + getWeight());
        return desired;
    }
}