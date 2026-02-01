package physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import ui.AssetManager;

/**
 * Classe responsável por desenhar o background do Nível 3 (Galáxia) através de partículas.
 *
 * Esta classe gera uma galáxia em espiral de forma procedimental, composta por três camadas:
 *  - haze: poeira difusa e suave (base)
 *  - highlights: estrelas/partículas mais definidas (brilho)
 *  - denseHighlights: braços mais densos ("tentáculos") para dar estrutura à espiral
 *
 * A emissão de partículas é contínua (flow em partículas/segundo) e depende de dt,
 * criando um efeito de rotação global através de globalAngle. A distribuição espacial
 * segue uma espiral logarítmica (controlada por spiralTightness) e a galáxia é
 * achatada no eixo vertical através de flatten.
 *
 * O núcleo é desenhado com um efeito de glow/pulso (corePulse).
 *
 * Esta classe é puramente visual: não contém colisões, inimigos, nem lógica de gameplay.
 */


public class Galaxy {

    private final List<ParticlePhysics> haze = new ArrayList<>();    //particulas menos definidas de "poeira"
    private final List<ParticlePhysics> highlights = new ArrayList<>();
    private final List<ParticlePhysics> denseHighlights = new ArrayList<>();   //"tentaculos"

    private PVector center;
    private float globalAngle = 0f;

    private PImage starsBg; 

    //cores
    private int purple;
    private int blue;
    private int pink;

    //tamanho e forma
    private int arms = 5;  //numero de bracos da espiral
    private float flatten = 0.75f;  //achatamento vertical
    private float galaxyRadius;
    private float coreRadius = 22f;  //raio base do nucleo

    //espiral
    private float spiralTightness = 3.2f;  //quao "fechada" é a espiral
    private float armSpreadHaze = 0.55f;  //desvio angular grande para a poeira
    private float armSpreadStars = 0.20f;  //desvio angular menor para estrelas mais definidas
    private float armSpreadDense = 0.10f;  //desvio angular minimo para bracos mais densos ("tentaculos")

    //rotação
    private float rotationSpeed = 0.16f; //rotacao global (rad/s)
    private float corePulse = 0f;
    
    //particulas/segundo
    private float hazeFlow = 380f; 
    private float starFlow = 140f;
    private float denseFlow = 90f;   //braços
    
    private int denseArmsCount = 3;  //quantos bracos ficam densos por ciclo
    private int[] denseArms;    //indices dos braços mais densos (escolhido no onEnter)

    public void onEnter(PApplet p) {
        starsBg = AssetManager.get().img("stars");  //background de estrelas

        center = new PVector(p.width / 2f, p.height / 2f);
        globalAngle = 0f;
        corePulse = 0f;

        galaxyRadius = Math.min(p.width, p.height) * 0.38f;  //raio da galaxia
        purple = p.color(190, 70, 255);
        blue = p.color(70, 200, 255);
        pink = p.color(255, 110, 170);

        haze.clear();
        highlights.clear();
        denseHighlights.clear();

        //escolher aleatoriamente quais braços vão ser mais densos
        denseArmsCount = Math.min(denseArmsCount, arms);
        denseArms = pickRandomArms(denseArmsCount, arms);
    }

    public void update(PApplet p, float dt) {
        globalAngle += rotationSpeed * dt;
        corePulse += dt * 2.0f;

        //camadas normais
        emitLayer(p, dt, hazeFlow, 0);
        emitLayer(p, dt, starFlow, 1);

        //camada tentaculos (denso)
        emitLayer(p, dt, denseFlow, 2);

        updateList(haze, dt);
        updateList(highlights, dt);
        updateList(denseHighlights, dt);
    }

    private void updateList(List<ParticlePhysics> list, float dt) {
        for (int i = list.size() - 1; i >= 0; i--) {
            ParticlePhysics par = list.get(i);
            par.move(dt);
            
            if (par.isDead()) { 
            	list.remove(i);
            }
        }
    }

    /**
    * layerType:
    * 0 = haze (poeria)
    * 1 = highlights normais
    * 2 = highlights densos
    */
    private void emitLayer(PApplet p, float dt, float flow, int layerType) {
    	
        float particlesPerFrame = flow * dt; //multiplica-se por dt para obter particulas deste frame
        
        int n = (int) particlesPerFrame;
        float frac = particlesPerFrame - n;

        for (int i = 0; i < n; i++) { addGalaxyParticle(p, layerType);}
        if (Math.random() < frac) { addGalaxyParticle(p, layerType);}
    }

    private void addGalaxyParticle(PApplet p, int layerType) {
        boolean isHaze = (layerType == 0);
        boolean isDense = (layerType == 2);

        //raio
        float t = (float) Math.random();
        float bias;
        if(isHaze) {
        	bias = 0.55f;
        } else if (isDense) {
        	bias = 0.82f;
        } else {
        	bias = 0.70f;
        }
        
        //bias controla onde nasce mais: haze mais para fora, dense mais perto do braco
        float r = coreRadius + (float) Math.pow(t, bias) * (galaxyRadius - coreRadius);

        //braço
        int armIndex;
        if (isDense) {
            //forçar que os densos pertencem só a 2-3 braços escolhidos
            armIndex = denseArms[(int) (Math.random() * denseArms.length)];
        } else {
            armIndex = (int) (Math.random() * arms);
        }
        float armOffset = armIndex * (PConstants.TWO_PI / arms);

        //espiral logaritmica: theta cresce com log(r), dando a aparência de espiral
        float theta = spiralTightness * (float) Math.log(r / coreRadius + 1e-4f);
        theta += armOffset;

        //spread
        float spread;
        if (isHaze) { 
        	spread = armSpreadHaze;
        } else if (isDense) { 
        	spread = armSpreadDense;
        } else {
        	spread = armSpreadStars;
        }

        theta += ((float) Math.random() * 2f - 1f) * spread; //adiciona "ruido" angular à volta do braco para espessura

        theta += globalAngle; //rotação global

        //posição
        float x = center.x + r * PApplet.cos(theta);
        float y = center.y + r * PApplet.sin(theta) * flatten;
        PVector spawnPos = new PVector(x, y);

        //vetor tangencial para movimento circular à volta do centro
        PVector tang = new PVector(-PApplet.sin(theta), PApplet.cos(theta));
        tang.y *= flatten;
        tang.normalize();

        float vInner, vOuter;
        if (isHaze) {
            vInner = 22f; 
            vOuter = 7f;
            
        } else if (isDense) {
            //tentaculos: um bocadinho mais rapidos para "puxarem" o braço
            vInner = 85f; 
            vOuter = 28f;
            
        } else {
            vInner = 65f; 
            vOuter = 22f;
        }
        
        //lerp: mais rapido perto do centro, mais lento na periferia
        float v = PApplet.lerp(vInner, vOuter, r / galaxyRadius); 

        //drift radial pequeno para evitar criações demasiado perfeitas
        float drift;
        if (isHaze) { 
        	drift = 1.6f;
        } else if (isDense) { 
        	drift = 2.8f;
        } else {
        	drift = 2.4f;
        }
        
        PVector radial = PVector.sub(spawnPos, center);
        if (radial.magSq() > 0.0001f) { radial.normalize();}
        radial.mult(drift);

        //noise baixo
        float noiseAmp;
        if (isHaze) { 
        	noiseAmp = 0.8f;
        } else if (isDense) { 
        	noiseAmp = 1.0f;
        } else {
        	noiseAmp = 2.0f;
        }
        
        //noise baixo para quebrar padroes repetitivos
        PVector noise = PVector.random2D().mult(noiseAmp);
        noise.y *= flatten;

        PVector spawnVel = tang.mult(v).add(radial).add(noise);

        //cor base roxo -> azul
        float mix = r / galaxyRadius;
        int base = p.lerpColor(purple, blue, mix);

        //perto do centro mais claro
        float bright = PApplet.constrain(1f - (r / (coreRadius * 3.0f)), 0f, 1f);
        base = p.lerpColor(base, p.color(255), bright * 0.35f);

        //params por camada
        float pr, life;
        int c;

        if (isHaze) {
            pr = lerp(2.6f, 6.2f, (float) Math.random());
            life = lerp(2.2f, 4.2f, (float) Math.random());

            float a = lerp(10f, 24f, (float) Math.random()) + bright * 18f;
            c = withAlpha(p, base, a);

            haze.add(new ParticlePhysics(spawnPos, spawnVel, pr, c, life));
            return;
        }

        //highlights
        if (isDense) {
            pr = lerp(1.0f, 2.2f, (float) Math.random());
            life = lerp(1.6f, 3.2f, (float) Math.random());
        } else {
            pr = lerp(1.0f, 2.4f, (float) Math.random());
            life = lerp(1.2f, 2.8f, (float) Math.random());
        }

        //cor dos highlights
        float pinkChance;
        if (isDense) {
            pinkChance = 0.45f;
        } else {
            pinkChance = 0.28f;
        }
        
        //cor do resto
        int starCol;
        if (Math.random() < pinkChance) {
            starCol = p.lerpColor(pink, p.color(255), 0.20f);
        } else {
            if (isDense) {
                starCol = p.lerpColor(base, p.color(255), 0.65f);
            } else {
                starCol = p.lerpColor(base, p.color(255), 0.55f);
            }
        }

        //alpha: braços mais densos
        float a;
        if (isDense) {
            a = lerp(170f, 255f, (float) Math.random());
        } else {
            a = lerp(120f, 235f, (float) Math.random());
        }

        c = withAlpha(p, starCol, a);

        if (isDense) { 
        	denseHighlights.add(new ParticlePhysics(spawnPos, spawnVel, pr, c, life));
        } else { 
        	highlights.add(new ParticlePhysics(spawnPos, spawnVel, pr, c, life));
        }
    }

    public void display(PApplet p) {
        p.background(10);
        p.imageMode(PApplet.CORNER);
        if (starsBg != null) { p.image(starsBg, 0, 0, p.width, p.height);}

        //blend normal para haze (base suave)
        p.blendMode(PApplet.BLEND);
        for (ParticlePhysics par : haze) par.display(p);

        //add para highlights: soma cores e cria brilho neon
        p.blendMode(PApplet.ADD);
        for (ParticlePhysics par : highlights) { par.display(p);}
        for (ParticlePhysics par : denseHighlights) { par.display(p);}

        drawCoreGlow(p);

        p.blendMode(PApplet.BLEND);
    }

    private void drawCoreGlow(PApplet p) {
        float pulse = 0.5f + 0.5f * PApplet.sin(corePulse); //pulso 0..1 para variar alpha e ter um nucleo mais "vivo"

        p.pushStyle();
        p.noStroke();

        //glow externo
        p.fill(255, 160, 210, 18 + 10 * pulse);
        p.circle(center.x, center.y, coreRadius * 4.6f);

        //glow médio
        p.fill(255, 70 + 50 * pulse);
        p.circle(center.x, center.y, coreRadius * 2.4f);

        //nucleo
        p.fill(255, 235);
        p.circle(center.x, center.y, coreRadius * 1.0f);

        p.popStyle();
    }

    //escolhe N braços aleatórios sem repetição
    private int[] pickRandomArms(int n, int totalArms) {
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < totalArms; i++) { idx.add(i);}
        Collections.shuffle(idx);

        int[] out = new int[n];
        for (int i = 0; i < n; i++) { out[i] = idx.get(i);}
        return out;
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private int withAlpha(PApplet p, int col, float alpha) {
        alpha = PApplet.constrain(alpha, 0, 255);
        return p.color(p.red(col), p.green(col), p.blue(col), alpha);
    }
}