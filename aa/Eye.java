package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o sistema de visão de um boid.
 *
 * O Eye filtra, a cada frame, os corpos rastreados em duas listas:
 *  - farSight: dentro da distância de visão e do cone frontal (limitado por ângulo)
 *  - nearSight: dentro de uma distância de segurança (zona próxima), com ângulo livre
 *
 * Os parâmetros de visão são obtidos a partir do DNA do boid
 * (distância, distância segura e ângulo de visão).
 *
 * A classe inclui também um método de desenho para visualização/debug,
 * representando o cone de farSight e o círculo de nearSight.
 *
 * Esta classe apenas calcula perceção/filtragem, a escolha de alvo e o comportamento
 * associado (pursue, avoid, ...) é tratado pelos behaviors do boid.
 */


public class Eye {
    private final float visionDistance, visionSafeDistance, visionAngle;
    protected Body target;
    private List<Body> allTrackingBodies, farSight, nearSight;
    private Boid me;
    private float displayScaleFar = 1.4f;
    private float displayScaleNear = 1.2f;

    public Eye(Boid me, List<Body> allTrackingBodies) {
        this.me = me;
        this.allTrackingBodies = allTrackingBodies;
        
        if (!allTrackingBodies.isEmpty()) {
            target = allTrackingBodies.get(0);
        }
        
        this.visionAngle = me.dna.visionAngle;
        this.visionDistance = me.dna.visionDistance;
        this.visionSafeDistance = me.dna.visionSafeDistance;

    }

    public void setDisplayScale(float farScale, float nearScale) {
        this.displayScaleFar = farScale;
        this.displayScaleNear = nearScale;
    }


    public List<Body> getFarSight() {
        return farSight;
    }

    public List<Body> getNearSight() {
        return nearSight;
    }

    public void look() {
        //reconstroi as listas de far e near sight a cada frame
        farSight = new ArrayList<Body>();
        nearSight = new ArrayList<Body>();
        for (Body b : allTrackingBodies) {
            if (farSight(b.getPos())) {
                farSight.add(b);
            }
            if (nearSight(b.getPos())) {
                nearSight.add(b);
            }
        }
    }

    private boolean inSight(PVector target, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(target, me.getPos());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVel());  //angulo entre direcao para o alvo e direcao de movimento do boid
        
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle)); //em vista se estiver dentro da distancia e do angulo
    }
    
    //cone frontal: distancia de visao + angulo limitado
    private boolean farSight(PVector target) {
        return inSight(target, me.dna.visionDistance, me.dna.visionAngle);
    }
    
    //zona segura: distancia curta com angulo livre (360 graus)
    private boolean nearSight(PVector target) {
        return inSight(target, me.dna.visionSafeDistance, (float) Math.PI);
    }

    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.pushMatrix();
        float[] pp = plt.getPixelCoord(me.getPos().x, me.getPos().y); //mundo -> pixel
        p.translate(pp[0], pp[1]);
        p.rotate(-me.getVel().heading()); //alinha o cone com a direcao do boid

        float[] farPix = plt.getDimInPixel(visionDistance * displayScaleFar,
                visionDistance * displayScaleFar);
        float[] nearPix = plt.getDimInPixel(visionSafeDistance * displayScaleNear,
                visionSafeDistance * displayScaleNear);

        //desenha o cone de far sight (sem alterar estilo global)
        p.fill(0, 200, 0, 30);
        p.stroke(0, 200, 0, 90);
        p.strokeWeight(2.2f);
        p.arc(0, 0, 2 * farPix[0], 2 * farPix[0], -visionAngle, visionAngle); //far sight: cone (arc) + linhas de limite

        //linhas que limitam o cone
        p.pushMatrix();
        p.rotate(visionAngle);
        p.line(0, 0, farPix[0], 0);
        p.rotate(-2 * visionAngle);
        p.line(0, 0, farPix[0], 0);
        p.popMatrix();

        //desenha a área de near sight
        p.fill(255, 0, 0, 40);
        p.stroke(255, 0, 0, 110);
        p.strokeWeight(2);
        p.circle(0, 0, 2 * nearPix[0]); //near sight: circulo a volta do boid (zona de segurança)

        p.popMatrix();
        p.popStyle();

    }

    public Body getTarget() {
        return target;
    }

    public void setTarget(Body target) {
        this.target = target;
    }
}
