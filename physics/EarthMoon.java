package physics;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.core.PImage;
import ui.AssetManager;

/**
 * Classe responsável por desenhar o background do Nivel 1 (Terra e Lua)
 * 
 * Esta classe implementa um cenário visual composto por:
 *  - Fundo estático de estrelas (imagem)
 *  - Terra fixa no espaço, com rotação própria e efeito de "glow"
 *  - Lua a orbitar a Terra através de uma simulação física simplificada
 * 
 * A órbita da Lua é calculada com base numa força gravitica ficticia,
 * utilizando massas e uma constante gravitacional ajustadas à escala do jogo.
 * O tempo de simulação pode ser acelerado ou desacelerado através de um
 * fator de escala temporal.
 * 
 * Esta classe é puramente visual (background) e não contém lógica
 * de gameplay, colisões ou inimigos
 */

public class EarthMoon {
	
	private PImage starsBg;
    private PImage earthImg;
    private PImage moonImg;
    
    private float timeScale = 15f;  //escala de tempo
    
    //corpos fisicos
    private Mover earth;  //fixo
    private Mover moon;   //orbita com gravidade
    
    //tamanhos (px)
    private float earthR = 95f;
    private float moonR = 26f;
    
    //parâmetros 
    private float earthMass = 1.0f;   //massas falsas
    private float moonMass  = 0.01f;
    private float G = 450f;           //constante gravitica

    //setup da orbita
    private float orbitR = 170f;

    //rotação da Terra
    private float earthSpin = 0f;     //rad
    private float earthSpinSpeed = PApplet.TWO_PI / 60f;   //1 volta em 60s

    public EarthMoon() {}

    public void onEnter(PApplet p) {
    	starsBg = AssetManager.get().img("stars");
    	earthImg = AssetManager.get().img("earth");
        moonImg  = AssetManager.get().img("moon");
        
        PVector earthPos = new PVector(p.width * 0.5f, p.height * 0.42f);

        //earth: vel zero, massa grande
        earth = new SimpleMover(earthPos, new PVector(0, 0), earthMass, earthR);

        //moon: começa à direita e com velocidade para orbita
        PVector moonPos = new PVector(earthPos.x + orbitR, earthPos.y);

        //velocidade circular aproximada: v = sqrt(G*M/r)
        float v = (float)Math.sqrt((G * earthMass) / orbitR);

        //tangente (para cima para orbitar)
        PVector moonVel = new PVector(0, -v);

        moon = new SimpleMover(moonPos, moonVel, moonMass, moonR);
        
        earthSpin = 0f;
    }

    public void update(PApplet p, float dt) {
    	
    	dt = Math.min(dt, 1f / 30f);  //evita spikes de lag
    	
    	float simDt = dt * timeScale;
    	
    	//rotação da terra
    	earthSpin += earthSpinSpeed * dt;
    	earthSpin %= PApplet.TWO_PI;

    	//força gravitica Terra -> Lua
    	PVector r = PVector.sub(earth.getPos(), moon.getPos());
    	float dist = Math.max(r.mag(), earthR + moonR + 5f);
    	r.normalize();

    	//evitar exageros numéricos
    	float minDist = earthR + moonR + 5f;
    	dist = Math.max(dist, minDist);
        
    	float strength = (G * earth.getMass() * moon.getMass()) / (dist * dist);
    	PVector F = r.mult(strength);

    	moon.applyForce(F); 
    	moon.move(simDt);
    	
    }

    public void display(PApplet p) {
        //estrelas background
    	p.background(10);
    	p.imageMode(PApplet.CORNER);
    	if (starsBg != null) {
    	    //estica-se para o ecrã inteiro
    	    p.image(starsBg, 0, 0, p.width, p.height);
    	}
    	
    	PVector ep = earth.getPos(); //posição da terra
    	PVector mp = moon.getPos(); //posição da lua
    	
    	//glow da Terra
    	p.pushStyle();
    	p.noStroke();
    	p.fill(0, 120, 255, 40);
    	p.circle(ep.x, ep.y, earthR * 2.6f);
    	p.popStyle();
    	
    	//linha da orbita
    	p.pushStyle();
        p.noFill();
        p.stroke(255, 30);
        p.strokeWeight(1);
        p.circle(ep.x, ep.y, orbitR * 2);
        p.popStyle();
    	
    	//Terra
        if (earthImg != null) {
            p.pushMatrix();
            p.translate(ep.x, ep.y);
            p.rotate(earthSpin);
            p.imageMode(PConstants.CENTER);
            p.image(earthImg, 0, 0, earthR * 2, earthR * 2);
            p.popMatrix();
        }
        
        //Lua 
        if (moonImg != null) {
            p.imageMode(PConstants.CENTER);
            p.image(moonImg, mp.x, mp.y, moonR * 2, moonR * 2);
        }
    }
    
    //Mover simples (só px)
    private static class SimpleMover extends Mover {
        public SimpleMover(PVector pos, PVector vel, float mass, float radius) {
            super(pos, vel, mass, radius);
        }
    }
    
}

