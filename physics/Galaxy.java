package physics;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import ui.AssetManager;


public class Galaxy {

    private final List<Particle> haze = new ArrayList<>(); //particulas menos definidas de "poeira"
    private final List<Particle> highlights = new ArrayList<>();
    private final List<Particle> denseHighlights = new ArrayList<>(); //"tentaculos"

    private PVector center;
    private float globalAngle = 0f;

    private PImage starsBg; 

    //cores
    private int purple;
    private int blue;
    private int pink;

    //tamanho e forma
    private int arms = 5;
    private float flatten = 0.75f;  //achatado
    private float galaxyRadius;
    private float coreRadius = 22f;

    //espiral
    private float spiralTightness = 3.2f;
    private float armSpreadHaze = 0.55f;
    private float armSpreadStars = 0.20f;

    private float armSpreadDense = 0.10f;  //tentaculos

    //rotação
    private float rotationSpeed = 0.16f;
    private float corePulse = 0f;

    private float hazeFlow = 380f;
    private float starFlow = 140f;

    private float denseFlow = 90f;    //highlights
    private int denseArmsCount = 3; 
    private int[] denseArms;    //indices dos braços mais densos (escolhido no onEnter)

    public void onEnter(PApplet p) {
        starsBg = AssetManager.get().img("stars");

        center = new PVector(p.width / 2f, p.height / 2f);
        globalAngle = 0f;
        corePulse = 0f;

        galaxyRadius = Math.min(p.width, p.height) * 0.38f;

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

        //camada tentáculos (denso)
        emitLayer(p, dt, denseFlow, 2);

        updateList(haze, dt);
        updateList(highlights, dt);
        updateList(denseHighlights, dt);
    }

    private void updateList(List<Particle> list, float dt) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Particle par = list.get(i);
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
        float particlesPerFrame = flow * dt;
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
        float bias = isHaze ? 0.55f : (isDense ? 0.82f : 0.70f);

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

        //ângulo espiral
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

        theta += ((float) Math.random() * 2f - 1f) * spread;

        theta += globalAngle; //rotação

        //posição
        float x = center.x + r * PApplet.cos(theta);
        float y = center.y + r * PApplet.sin(theta) * flatten;
        PVector spawnPos = new PVector(x, y);

        //velocidade tangencial lenta
        PVector tang = new PVector(-PApplet.sin(theta), PApplet.cos(theta));
        tang.y *= flatten;
        tang.normalize();

        float vInner, vOuter;
        if (isHaze) {
            vInner = 22f; 
            vOuter = 7f;
            
        } else if (isDense) {
            //tentáculos: um bocadinho mais rápidos para “puxarem” o braço
            vInner = 85f; 
            vOuter = 28f;
            
        } else {
            vInner = 65f; 
            vOuter = 22f;
        }
        
        float v = PApplet.lerp(vInner, vOuter, r / galaxyRadius);

        //drift radial pequeno
        float drift = isHaze ? 1.6f : (isDense ? 2.8f : 2.4f);
        PVector radial = PVector.sub(spawnPos, center);
        if (radial.magSq() > 0.0001f) { radial.normalize();}
        radial.mult(drift);

        //noise baixo
        float noiseAmp = isHaze ? 0.8f : (isDense ? 1.0f : 2.0f);
        PVector noise = PVector.random2D().mult(noiseAmp);
        noise.y *= flatten;

        PVector spawnVel = tang.mult(v).add(radial).add(noise);

        //cor base roxo->azul
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

            haze.add(new Particle(spawnPos, spawnVel, pr, c, life));
            return;
        }

        //highlights
        pr = isDense ? lerp(1.0f, 2.2f, (float) Math.random())
                     : lerp(1.0f, 2.4f, (float) Math.random());

        life = isDense ? lerp(1.6f, 3.2f, (float) Math.random())
                       : lerp(1.2f, 2.8f, (float) Math.random());

        //cor dos highlights
        float pinkChance = isDense ? 0.45f : 0.28f;
        int starCol = (Math.random() < pinkChance)
                ? p.lerpColor(pink, p.color(255), 0.20f)
                : p.lerpColor(base, p.color(255), isDense ? 0.65f : 0.55f);

        //alpha: braços mais fortes
        float a = isDense ? lerp(170f, 255f, (float) Math.random())
                          : lerp(120f, 235f, (float) Math.random());

        c = withAlpha(p, starCol, a);

        if (isDense) { 
        	denseHighlights.add(new Particle(spawnPos, spawnVel, pr, c, life));
        } else { 
        	highlights.add(new Particle(spawnPos, spawnVel, pr, c, life));
        }
    }

    public void display(PApplet p) {
        p.background(10);
        p.imageMode(PApplet.CORNER);
        if (starsBg != null) { p.image(starsBg, 0, 0, p.width, p.height);}

        //haze suave (base)
        p.blendMode(PApplet.BLEND);
        for (Particle par : haze) par.display(p);

        //highlights + tentaculos (brilho/definição)
        p.blendMode(PApplet.ADD);
        for (Particle par : highlights) { par.display(p);}
        for (Particle par : denseHighlights) { par.display(p);}

        drawCoreGlow(p);

        p.blendMode(PApplet.BLEND);
    }

    private void drawCoreGlow(PApplet p) {
        float pulse = 0.5f + 0.5f * PApplet.sin(corePulse);

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
        java.util.Collections.shuffle(idx);

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
