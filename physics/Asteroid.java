package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

/**
 * Classe que representa um asteroide simples no espaço
 *
 * Um asteroide é uma entidade fisica que herda de Mover,
 * possuindo posição, velocidade, massa e raio, e cujo movimento
 * é tratado externamente pela lógica de simulação.
 *
 * A classe é responsável apenas pela representação visual do asteroide,
 * permitindo o seu desenho no ecrã:
 *  - através de um sprite, se disponível
 *  - ou como um círculo simples, caso não exista sprite
 *
 * A conversão de coordenadas do espaço lógico para píxeis é realizada
 * com recurso à classe SubPlot.
 *
 * Esta classe não contém lógica de colisões, destruição ou gameplay.
 */

public class Asteroid extends Mover {

    private final int color;
    private PImage sprite;

    protected Asteroid(PVector pos, PVector vel, float mass, float radius, int color, PImage sprite) {
        super(pos, vel, mass, radius);
        this.color = color;
        this.sprite = sprite;
    }

    public void display(PApplet p, SubPlot plt) {
    	//converter coordenadas do espaco logico para pixeis
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] r = plt.getDimInPixel(radius, radius);

        //desenhar sprite se existir, caso contrario desenhar forma simples
        if (sprite != null) {
            p.imageMode(PApplet.CENTER);
            p.image(sprite, pp[0], pp[1], 12 * r[0], 12 * r[0]);  //escala visual do sprite
        } else {
            p.noStroke();
            p.fill(color);
            p.circle(pp[0], pp[1], 2 * r[0]);

        }

    }
}
