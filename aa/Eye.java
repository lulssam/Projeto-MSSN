package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

//Classe que representa o sistema de visão do boid que determina farSight e nearSight e desenha o cone/área

public class Eye {
	private final float visionDistance, visionSafeDistance, visionAngle;
	private List<Body> allTrackingBodies, farSight, nearSight;
	private Boid me;
	protected Body target;
	private float displayScaleFar = 1.4f;
    private float displayScaleNear = 1.2f;

	public Eye(Boid me, List<Body> allTrackingBodies) {
		this.me = me;
		this.allTrackingBodies = allTrackingBodies;
		target = allTrackingBodies.get(0);
		this.visionAngle = me.dna.visionAngle;
        this.visionDistance = me.dna.visionDistance;
        this.visionSafeDistance = me.dna.visionSafeDistance;
        
        //se houver uma lista, define o primeiro como target
        if (!allTrackingBodies.isEmpty()) setTarget(allTrackingBodies.getFirst());
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
		float angle = PVector.angleBetween(r, me.getVel());
		//está em vista se estiver a uma distância positiva menor que maxDistance e dentro do ângulo
		return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
	}

	private boolean farSight(PVector target) {
		return inSight(target, me.dna.visionDistance, me.dna.visionAngle);
	}

	private boolean nearSight(PVector target) {
		return inSight(target, me.dna.visionSafeDistance, (float) Math.PI);
	}

	public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.pushMatrix();
        float[] pp = plt.getPixelCoord(me.getPos().x, me.getPos().y);
        p.translate(pp[0], pp[1]);
        p.rotate(-me.getVel().heading());

        float[] farPix = plt.getDimInPixel(visionDistance * displayScaleFar,
                visionDistance * displayScaleFar);
        float[] nearPix = plt.getDimInPixel(visionSafeDistance * displayScaleNear,
                visionSafeDistance * displayScaleNear);

        //desenha o cone de far sight (sem alterar estilo global)
        p.fill(0, 200, 0, 30);
        p.stroke(0, 200, 0, 90);
        p.strokeWeight(2.2f);
        p.arc(0, 0, 2 * farPix[0], 2 * farPix[0], -visionAngle, visionAngle);

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
        p.circle(0, 0, 2 * nearPix[0]);

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