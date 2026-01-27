package physics;

import aa.Type;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe que é a base para corpos genéricos com massa e aparência simples,
 * que podem ser desenhados e têm um tipo associado
 */
public class Body extends Mover {

    protected Type type;
    protected int color;

    protected Body(PVector pos, PVector vel, float mass, float radius, int color) {
        super(pos, vel, mass, radius);
        this.color = color;
    }

    /**
     * Obter o tipo do corpo.
     *
     * @return type - tipo do corpo
     */
    public Type getType() {
        return type;
    }

    /**
     * definir o tipo do corpo
     *
     * @param type tipo do corpo a definir
     */
    public void setType(Type type) {
        this.type = type;
    }

    public void display(PApplet p){
        p.pushStyle();

        p.noStroke(); // sem traço
        p.fill(0); // preto
        p.circle(pos.x, pos.y, 2 * radius);

        p.popStyle();

    }}
