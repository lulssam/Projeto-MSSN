package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um corpo celeste com interação gravitacional e rasto de movimento.
 *
 * Um CelestialBody herda de Mover, mantendo a componente fisica base
 * (posição, velocidade, massa e raio), e acrescenta:
 *  - cálculo de força gravitacional simplificada para atrair outros corpos
 *  - um rasto (trail) com as posições recentes para visualização do movimento
 *  - desenho com sprite opcional ou fallback para um círculo
 *
 * O rasto é atualizado a cada chamada de "move(float)" e limitado por trailSize
 * para evitar crescimento infinito de memória.
 *
 * A constante G e a escala do sistema podem ser ajustadas ao contexto do jogo,
 * não tendo de corresponder a valores físicos reais.
 */

public class CelestialBody extends Mover {

	private int color;
	private PImage sprite;
	private static double G = 6.67e-11; //constante base, ajustada a escala do jogo
	private List<PVector> trail;
	private int trailSize = (int) 1e2;  //numero maximo de pontos no rasto

	protected CelestialBody(PVector pos, PVector vel, float mass, float radius, int color, PImage sprite) {
		super(pos, vel, mass, radius);
		this.color = color;
		this.sprite = sprite;
		this.trail = new ArrayList<>();
	}

	//atualizar o rasto ao mover
	@Override
	public void move(float dt) {
		super.move(dt); //atualizar fisica base

		//adicionar posição atual ao rasto
		trail.add(pos.copy());

		//remover pontos antigos quando excede limite
		//limita o tamanho do rasto para evitar crescimento infinito
		if (trail.size() > trailSize) {
			trail.remove(0);
		}
	}

	//força de atração gravitica entre este corpo e outro
	public PVector attraction(Mover m) {
		PVector r = PVector.sub(pos, m.getPos());  //vetor direcao do outro corpo (aponta para o outro)
		
		float dist = r.mag();
		float strength = (float) (G * mass * m.getMass() / Math.pow(dist, 2));

		return r.normalize().mult(strength);
	}

	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();

		//desenhar o rasto do movimento
		p.noFill();
		p.stroke(color, 150);  //cor do planeta, transparência
		p.strokeWeight(1);
		p.beginShape();
		
		for (PVector v : trail) {
			float[] pp = plt.getPixelCoord(v.x, v.y); //converter coords mundo -> pixeis
			p.vertex(pp[0], pp[1]);
		}
		
		p.endShape();

		//desenhar o corpo principal
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] r = plt.getDimInPixel(radius, radius);

		if (sprite != null) {
			p.imageMode(PApplet.CENTER);
			p.image(sprite, pp[0], pp[1], 2 * r[0], 2 * r[0]);
		} else {
			p.noStroke();
			p.fill(color);
			p.circle(pp[0], pp[1], 2 * r[0]);

		}
		p.popStyle();

	}

}
