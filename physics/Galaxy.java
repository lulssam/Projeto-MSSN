package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import ui.AssetManager;

/**
 * Classe responsável por desenhar o background do Nivel 3 (Galáxia)
 */

public class Galaxy {
    private ParticleSystem arm1, arm2, core;
    private float angle = 0;
    private PVector centro;
    private int color1, color2;
    private PImage starsBg;

    public void onEnter(PApplet p) {

        starsBg = AssetManager.get().img("stars");

        centro = new PVector((float) p.width / 2, (float) p.height / 2);
        angle = 0;

        // braços da galaxia
        float[] velParams = {0, 0.5f, 60f, 120f};
        float[] lifetime = {3.0f, 4.5f};
        float[] radius = {2f, 5f};                      // tamanho das estrelas
        float flow = 400f;                               // estrelas por segundo

        // cores da via lactea -> azul e roxo
        color1 = p.color(200, 50, 255); // roxo
        color2 = p.color(50, 200, 255); // azul clarinho

        // braço 1
        PSControl psc1 = new PSControl(velParams, lifetime, radius, flow, color1);
        arm1 = new ParticleSystem(centro.copy(), new PVector(), 1f, 1f, psc1);

        // braço 2
        float[] velParams2 = {(float) Math.PI, 0.5f, 60f, 120f};
        PSControl psc2 = new PSControl(velParams2, lifetime, radius, flow, color2);
        arm2 = new ParticleSystem(centro.copy(), new PVector(), 1f, 1f, psc2);

        // nucleo brilhante
        float[] velCore = {0, (float) (Math.PI * 2), 10f, 30f};
        float[] lifetimeCore = {2f, 3f};
        float[] radiusCore = {4f, 8f};
        PSControl pscCore = new PSControl(velCore, lifetimeCore, radiusCore, 30f, -1); // -1 é brancoooo
        core = new ParticleSystem(centro.copy(), new PVector(), 1f, 1f, pscCore);
    }

    public void update(PApplet p, float dt) {
        // atualizar a fisica das particulas existentes
        arm1.move(dt);
        arm2.move(dt);
        core.move(dt);

        // rotação da galaxia
        float rotationSpeed = 1.0f;
        angle += rotationSpeed * dt;

        // rodar emissores para criar efieto espiral
        updateEmissores(arm1, angle);
        updateEmissores(arm2, (float) (angle + Math.PI));
    }

    private void updateEmissores(ParticleSystem ps, float currentAngle) {
        PSControl psc = ps.getPSControl();
        // preservar outros paramentros, mudar so o angulo médio
        float[] newVelParams = {currentAngle, psc.getDispersionAngle(), psc.getMinVelocity(), psc.getMaxVelocity()};
        psc.setVelParams(newVelParams);
    }

    public void display(PApplet p) {

        p.background(10);
        p.imageMode(PApplet.CORNER);
        if (starsBg != null) p.image(starsBg, 0, 0, p.width, p.height);

        p.blendMode(PApplet.ADD);

        arm1.display(p);
        arm2.display(p);
        core.display(p);

        p.blendMode(PApplet.BLEND);
    }

}
