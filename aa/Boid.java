package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

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

    public void applyBehaviors(float dt) {
        PVector sumForces = new PVector();
        float sumWeights = 0;

        //percorrer todos os comportamentos ativos
        for (Behavior behavior : behaviors) {
            PVector desired = behavior.getDesiredVelocity(this);

            //aplicar peso do comportamtno
            if (desired != null) {
            	 PVector f = desired.copy();
                 f.mult(behavior.getWeight());
                 sumForces.add(f);
                 sumWeights += behavior.getWeight();
            }
        }


        //se houver forças -> aplicar
        if (sumWeights > 0) {
        	sumForces.div(sumWeights);
            sumForces.limit(dna.maxForce);
            applyForce(sumForces);
        }

        super.move(dt);
        //vel.y = 0;

        checkBounds();
    }


    //definir limites do mundo (modelo wrap around)
    private void checkBounds() {
        if (pos.x > p.width + radius) pos.x = -radius;
        else if (pos.x < -radius) pos.x = p.width + radius;
    }

    @Override
    public void display(PApplet p) {
        p.pushMatrix();
        p.translate(pos.x, pos.y);

        float angle = vel.heading();
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
