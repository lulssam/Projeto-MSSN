package game;

import physics.Galaxy;
import processing.core.PApplet;

public class Level3 extends Level {
    private Galaxy bg;

    public Level3() {
        bg = new Galaxy();
    }

    @Override
    public void onEnter(PApplet p) {
        bg.onEnter(p);
    }

    @Override
    public void update(PApplet p, float dt) {
        bg.update(p, dt);
    }

    @Override
    public void display(PApplet p) {
        bg.display(p);
    }

    @Override
    public String music() {
    	return "/level3.wav";
    }

}
