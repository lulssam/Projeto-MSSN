package game;

import java.util.List;

import aa.Pursuit;
import aa.Wander;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class EnemyLevel3 extends Enemy {

    private boolean isPursuer = false; //flag para saber se é pursuer

    protected EnemyLevel3(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, radius, sprite, p);
        
        this.hp = 3;  //é necessário 3 tiros do player para matar
        this.maxY = p.height / 2f; //para descer mais
   
        //ligeiramente mais rápidos doque nivel 2
        this.dna.maxSpeed = 200;
        this.dna.maxForce = 70;
    }
    
    //para tornar alguns inimigos “caçadores”
    public void makeChaser(Body target, List<Body> trackingList) {
        isPursuer = true;

        setTarget(target, trackingList);
        
        //os pursuers perseguem o player mais rápido 
        this.dna.maxSpeed *= 1.30f;
        this.dna.maxForce *= 1.20f;

        float seekWeight = 1.25f;
        addBehavior(new Pursuit(seekWeight));
    }

    public boolean isChaser() {
        return isPursuer;
    }

	@Override
	protected void initBehaviors() {
		 addBehavior(new Wander(1.3f)); //comportamento base
	}
}
