package game;

import aa.Behavior;
import aa.Wander;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class EnemyLevel1 extends Enemy {
    protected EnemyLevel1(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, radius, sprite, p);
        this.hp = 1;  //é necessário apenas 1 tiro para matar 
    }

    @Override
    protected void initBehaviors() {
        Behavior wander = new Wander(0.5f);
        addBehavior(wander);
    }
}