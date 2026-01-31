package game;

import aa.Pursuit;
import aa.Wander;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.List;

public class EnemyLevel2 extends Enemy {

    private float seekWeight;

    protected EnemyLevel2(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, radius, sprite, p);
        this.hp = 2;  //é necessário 2 tiros do player para matar
        this.maxY = p.height / 2f; //para descer mais

        this.seekWeight = 1.2f;
        
        this.dna.maxSpeed = 180;
        this.dna.maxForce = 60; 
    }

    @Override
    protected void initBehaviors() {
    	addBehavior(new Wander(1.0f)); //wander como base
    }

    public void setupTarget(Body target, List<Body> trackingList) {
    	  setTarget(target, trackingList);
    	  
    	  //os pursuers perseguem o player mais rápido 
    	  this.dna.maxSpeed *= 1.25f;   //+25% speed
    	  this.dna.maxForce *= 1.15f;   //+15% force
    	  
          addBehavior(new Pursuit(seekWeight)); //só os escolhidos no EnemyManager recebem pursuit
    }
}
