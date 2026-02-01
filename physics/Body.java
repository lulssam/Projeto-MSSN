package physics;

import aa.Type;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe base para corpos físicos genéricos no sistema
 *
 * Um Body representa uma entidade com massa, raio e posição,
 * herdando o comportamento físico básico da classe Mover.
 * Para além disso, possui:
 *  - um tipo lógico (Type), usado para identificação ou comportamento
 *  - uma aparência simples definida por uma cor
 *
 * Esta classe destina-se a ser estendida por entidades concretas
 * (ex: asteroides, inimigos, objetos do jogo), não contendo lógica
 * específica de gameplay.
 */

public class Body extends Mover {

    protected Type type;
    protected int color;

    protected Body(PVector pos, PVector vel, float mass, float radius, int color) {
        super(pos, vel, mass, radius);
        this.color = color;
    }

    //obter o tipo do corpo
    public Type getType() {
        return type;
    }

    //definir o tipo do corpo
    public void setType(Type type) {
        this.type = type;
    }

    public void display(PApplet p){
        p.pushStyle();
        
       //representacao visual simples do corpo
        p.noStroke();  //sem traço
        p.fill(color);
        p.circle(pos.x, pos.y, 2 * radius);

        p.popStyle();

    }}
