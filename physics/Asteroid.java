package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

//Classe Asteroid que representa um asteroide simples que se move no espaço e é desenhado no ecrã

public class Asteroid extends Mover {

    private final int color;
    private PImage sprite;

    protected Asteroid(PVector pos, PVector vel, float mass, float radius, int color, PImage sprite) {
        super(pos, vel, mass, radius);
        this.color = color;
        this.sprite = sprite;
    }

    public void display(PApplet p, SubPlot plt) {
        //obter coordenadas em pixeis
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);

        // mostrar sprite
        if (sprite != null) {
            p.imageMode(PApplet.CENTER);
            p.image(sprite, pp[0], pp[1], 12 * r[0], 12 * r[0]);
        } else {
            p.noStroke();
            p.fill(color);
            p.circle(pp[0], pp[1], 2 * r[0]);

        }

    }
}
