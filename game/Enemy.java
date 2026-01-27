package game;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Enemy extends Entity {


    public Enemy(PVector pos, float radius, PImage sprite) {
        super(pos, radius, sprite);
    }

    public void update(float dt) {
       
    }

    public boolean isOffscreen(PApplet p) {
        return pos.y > p.height + radius;
    }
}
