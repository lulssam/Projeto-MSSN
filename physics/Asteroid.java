package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

//Classe Asteroid que representa um asteroide simples que se move no espaço e é desenhado no ecrã

public class Asteroid extends Mover {

    private final int color;

    protected Asteroid(PVector pos, PVector vel, float mass, float radius, int color) {
        super(pos, vel, mass, radius);
        this.color = color;
    }

    public void display(PApplet p, SubPlot plt) {
    	//obter coordenadas em pixeis
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);
        
        p.noStroke();
        p.fill(color);
        p.circle(pp[0], pp[1], 2 * r[0]);
    }
}
