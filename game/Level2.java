package game;

import physics.SolarSystem;
import processing.core.PApplet;

public class Level2 extends Level {
    private SolarSystem bg;

    public Level2() {
        bg = new SolarSystem();
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
        return "/level2.wav";
    }


}
