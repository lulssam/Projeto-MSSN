package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe base para um boid (agente autónomo) com steering behaviors.
 *
 * Um code Boid herda de Body, mantendo propriedades físicas (posição, velocidade, massa e raio),
 * e adiciona uma camada de comportamento autónomo baseada em "behaviors" ponderados.
 *
 * A cada atualização, o boid:
 *  - consulta todos os comportamentos ativos (wander, pursuit, seek, avoid,...)
 *  - calcula as velocidades desejadas e aplica pesos (weight)
 *  - combina os resultados numa força média
 *  - limita a força pelo máximo definido no DNA e aplica-a ao corpo
 *
 * O boid usa um modelo de limites do tipo wrap-around no eixo x.
 *
 * Esta classe não gere colisões nem regras de gameplay, apenas calcula movimento e desenha o agente.
 */

public class Boid extends Body {

    protected DNA dna;
    protected Eye eye;
    protected List<Behavior> behaviors;
    protected PApplet p;
    protected float phiWander;
    private float speed;

    protected Boid(PVector pos, float mass, float radius, int color, PApplet p, Type type) {
        super(pos, new PVector(0, 0), mass, radius, color);
        this.p = p;
        this.dna = new DNA();
        this.behaviors = new ArrayList<>();
        speed = 1f;
    }

    //adicionar um comportamento à lista
    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }


    //remover um dado comportamento da lista
    public void removeBehavior(Behavior behavior) {
        if (behaviors.contains(behavior)) {  //confirmar se há na lista
            behaviors.remove(behavior);
        }
    }
    
   //combina comportamentos ativos por media ponderada
    public void applyBehaviors(float dt) {
        PVector sumForces = new PVector();
        float sumWeights = 0;

        //percorrer todos os comportamentos ativos
        for (Behavior behavior : behaviors) {
            PVector desired = behavior.getDesiredVelocity(this);

            //aplicar peso do comportamento
            //ignora comportamentos que nao produzem velocidade desejada neste frame
            if (desired != null) {
            	 PVector f = desired.copy();
                 f.mult(behavior.getWeight());
                 sumForces.add(f);
                 sumWeights += behavior.getWeight();
            }
        }


        //se houver forças -> aplicar
        if (sumWeights > 0) {
        	sumForces.div(sumWeights);  //normaliza pela soma dos pesos
            sumForces.limit(dna.maxForce);  //limita forca para evitar acelerações irrealistas
            applyForce(sumForces);
        }

        super.move(dt);
        //vel.y = 0;

        checkBounds();
    }


    //definir limites do mundo (modelo wrap around)
    //wrap-around no eixo x (sai de um lado, entra do outro)
    private void checkBounds() {
        if (pos.x > p.width + radius) { pos.x = -radius;}
        else if (pos.x < -radius) { pos.x = p.width + radius;}
    }

    @Override
    public void display(PApplet p) {
        p.pushMatrix();
        p.translate(pos.x, pos.y);

        float angle = vel.heading(); //orienta o boid pela direcao do movimento
        p.rotate(angle);

        p.fill(color);
        p.noStroke();

        p.beginShape();
        p.vertex(radius, 0);
        p.vertex(-radius, radius / 2);
        p.vertex(-radius, -radius / 2);
        p.endShape(PConstants.CLOSE);

        p.popMatrix();
    }

    public DNA getDna() {
        return dna;
    }

    public float getPhiWander() {
        return phiWander;
    }

    public void setPhiWander(float phiWander) {
        this.phiWander = phiWander;
    }

    public float getSpeed() {
        return speed;
    }

    public void setTarget(Body target, List<Body> trackingList) {
        this.eye = new Eye(this, trackingList);
        this.eye.setTarget(target);
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }
}
