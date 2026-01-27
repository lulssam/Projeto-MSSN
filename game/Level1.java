package game;

import physics.EarthMoon;
import processing.core.PApplet;

public class Level1 extends Level {
    private EarthMoon bg;

    public Level1() {
        bg = new EarthMoon();
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
        return "/level1.wav";
    }


}
