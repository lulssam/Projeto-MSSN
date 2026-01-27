package setup;

import game.GameApp;

import processing.core.PApplet;

public class ProcessingSetup extends PApplet {

    public static IProcessingApp app;
    private int lastUpdate;

    @Override
    public void settings() {
        size(800, 600);
        pixelDensity(1);
    }

    @Override
    public void setup() {
    	surface.setTitle("SPACE INVADERS");
        app.setup(this);
        lastUpdate = millis();
    }

    @Override
    public void draw() {
        int now = millis();
        float dt = (now - lastUpdate) / 1000f;
        lastUpdate = now;
        app.draw(this, dt);
    }

    @Override
    public void mousePressed() {
        app.mousePressed(this);
    }

    @Override
    public void mouseReleased() {
        app.mouseReleased(this);
    }

    @Override
    public void mouseDragged() {
        app.mouseDragged(this);
    }

    @Override
    public void keyPressed() {
        app.keyPressed(this);
    }
    
    @Override
    public void keyReleased() {
        app.keyReleased(this);
    }

    public static void main(String[] args) {
        app = new GameApp();
        PApplet.main(ProcessingSetup.class);
    }
}
