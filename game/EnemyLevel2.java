package game;

import aa.Pursuit;
import aa.Seek;
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
        this.hp = 75;
        this.maxY = p.height / 2f; // para descer mais um bocadinho

        this.seekWeight = (float) (0.4f + Math.random() * 1f);
    }

    @Override
    protected void initBehaviors() {
    }

    public void setupTarget(Body target, List<Body> trackingList) {
        setTarget(target, trackingList);

        addBehavior(new Pursuit(seekWeight));
        //System.out.println("Level 2:" + this.getBehaviors());
        //addBehavior(new Wander(0.1f));
    }
}
