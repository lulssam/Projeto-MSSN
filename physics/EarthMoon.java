package physics;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.core.PImage;

import fractals.Fractals;
import fractals.LSystem;
import fractals.Turtle;
import java.util.ArrayList;
import java.util.Random;

import ui.AssetManager;

/**
 * Classe responsável por desenhar o background do Nível 1 (Terra e Lua)
 *
 * Implementa um cenário visual composto por:
 *  - Fundo de estrelas (imagem esticada ao ecrã)
 *  - Terra fixa no espaço, com rotação própria e efeito de "glow"
 *  - Lua a orbitar a Terra através de uma simulação física simplificada (gravidade ficticia)
 *  - Árvores fractais (L-Systems) colocadas na superfície da Terra, a rodar com o planeta
 *
 * A órbita da Lua é calculada por uma força gravitacional fictícia (constante G ajustada à escala do jogo),
 * integrando o movimento com um passo de simulação escalado por timeScale.
 * Para estabilidade, dt é limitado para evitar picos quando há lag.
 *
 * Esta classe é puramente visual (background): não contém lógica de gameplay,
 * colisões, inimigos, score ou input do jogador.
 */

public class EarthMoon {
	
	private PImage starsBg;
    private PImage earthImg;
    private PImage moonImg;
    
    private float timeScale = 15f;  //escala de tempo, acelera a simulacao da orbita
    
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
    private float earthSpinSpeed = PApplet.TWO_PI / 60f;   //1 rotação completa em 60s
     
    //fractais
    private LSystem[] treeSystems;
    private Turtle[] treeTurtles;

    private final ArrayList<Float> treeAngles = new ArrayList<>();
    private final ArrayList<Integer> treeColors = new ArrayList<>();
    private final ArrayList<Integer> treeTypes  = new ArrayList<>();
    private final ArrayList<Float> treeScales   = new ArrayList<>();
    
    private final Random rng = new Random();

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
        //para orbita quase circular
        float v = (float)Math.sqrt((G * earthMass) / orbitR);

        //tangente (para cima para orbitar)
        PVector moonVel = new PVector(0, -v);

        moon = new SimpleMover(moonPos, moonVel, moonMass, moonR);
        
        earthSpin = 0f;
        
        //fractais
        treeSystems = new LSystem[] {
            Fractals.tree1(4),
            Fractals.tree2(4),
            Fractals.tree3(4),
            Fractals.tree4(3)
        };

        treeTurtles = new Turtle[] {
            Fractals.turtleTree1(),
            Fractals.turtleTree2(),
            Fractals.turtleTree3(),
            Fractals.turtleTree4()
        };

        treeAngles.clear();
        treeColors.clear();
        treeTypes.clear();
        treeScales.clear();

        int treeCount = 12; //número de árvores
        for (int i = 0; i < treeCount; i++) {

            float theta = (float)(i * (PApplet.TWO_PI / treeCount) + rng.nextGaussian() * 0.06);
            treeAngles.add(theta);

            int type = rng.nextInt(treeSystems.length);
            treeTypes.add(type);

            // escala pequena (isto resolve o “super grandes”)
            float sc = 0.22f + rng.nextFloat() * 0.18f; // 0.22..0.40
            treeScales.add(sc);

            // cores por tipo (com ligeira variação)
            int col;
            switch (type) {
                case 0: // tree1 - verde
                    col = p.color(40 + rng.nextInt(30), 180 + rng.nextInt(60), 40 + rng.nextInt(30), 200);
                    break;
                case 1: // tree2 - verde mais amarelo
                    col = p.color(80 + rng.nextInt(30), 200 + rng.nextInt(40), 60 + rng.nextInt(30), 200);
                    break;
                case 2: // tree3 - verde mais azulado
                    col = p.color(40 + rng.nextInt(30), 170 + rng.nextInt(50), 90 + rng.nextInt(40), 200);
                    break;
                default: // tree4 - “neon”/turquesa suave
                    col = p.color(20 + rng.nextInt(30), 220 + rng.nextInt(30), 140 + rng.nextInt(50), 200);
                    break;
            }
            treeColors.add(col);
        }
        
    }

    public void update(PApplet p, float dt) {
    	
    	dt = Math.min(dt, 1f / 30f);  //limita dt para evitar instabilidade quando ha lag
    	
    	float simDt = dt * timeScale;
    	
    	//rotação da terra
    	earthSpin += earthSpinSpeed * dt;
    	earthSpin %= PApplet.TWO_PI;

    	//força gravitica Terra -> Lua
    	PVector r = PVector.sub(earth.getPos(), moon.getPos());
    	float dist = Math.max(r.mag(), earthR + moonR + 5f);
    	r.normalize();

    	//evita forcas absurdas quando ficam muito proximas
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
    	
        //Terra e árvores fractais
        p.pushMatrix();
        p.translate(ep.x, ep.y);
        p.rotate(earthSpin);

        //desenhar a Terra (centro (0,0))
        if (earthImg != null) {
            p.imageMode(PConstants.CENTER);
            p.image(earthImg, 0, 0, earthR * 2, earthR * 2);
        }

        //desenhar as árvores
        if (treeSystems != null && treeTurtles != null) {
            p.pushStyle();
            p.strokeWeight(1.1f);
            p.noFill();

            float attachR = earthR * 0.90f;  //ligeiramente dentro da superficie da terra

            for (int i = 0; i < treeAngles.size(); i++) {
                float theta = treeAngles.get(i);

                //posição relativa ao centro (0,0)
                float x = PApplet.cos(theta) * attachR;
                float y = PApplet.sin(theta) * attachR;

                int type = treeTypes.get(i);
                LSystem ls = treeSystems[type];
                Turtle tt  = treeTurtles[type];

                p.pushMatrix();
                p.translate(x, y);

                p.rotate(theta);  //rotacao alinhada com a normal da superficie

                p.translate(-2f, 0f);
                p.scale(treeScales.get(i));
                p.stroke(treeColors.get(i));

                tt.renderPixels(ls, p);

                p.popMatrix();
            }

            p.popStyle();
        }

        p.popMatrix();

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

