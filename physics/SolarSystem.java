package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;
import ui.AssetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por desenhar o background do Nivel 2 (Sistema Solar)
 */

public class SolarSystem {
    private static final double G = 6.67e-11;
    private final float speedUp = 60 * 15 * 24 * 30 * 2;

    //SOL
    private final float sunMass = 1.989e30f;

    //MERCURIO
    private final float distMercurySun = 0.5791e11f;
    private final float mercurySpeed = 49e3f;
    private final float mercuryMass = 0.33e24f;

    //VENUS
    private final float distVenusSun = 1.082e11f;
    private final float venusSpeed = 35e3f;
    private final float venusMass = 4.87e24f;

    //TERRA
    private final float distEarthSun = 1.496e11f;
    private final float earthSpeed = 3e4f;
    private final float earthMass = 5.97e24f;

    //MARTE
    private final float distMarsSun = 2.279e11f;
    private final float marsSpeed = 24e3f;
    private final float marsMass = 0.64e24f;

    //JUPITER
    private final float distJupiterSun = 778330000000f;
    private final float jupiterSpeed = 1.307e4f;
    private final float jupiterMass = 1.898e27f;

    //SATURNO
    private final float distSaturnSun = 1429400000000f;
    private final float saturnSpeed = 0.969e4f;
    private final float saturnMass = 5.683e25f;

    //URANO
    private final float distUranusSun = 2.9338e12f;
    private final float uranusSpeed = 6810f;
    private final float uranusMass = 8.6810e25f;

    //NEPTUNO
    private final float distNeptuneSun = 4.4726e12f;
    private final float neptuneSpeed = 5430f;
    private final float neptuneMass = 1.0243e26f;

    //ASTEROIDES
    private final int numAsteroids = 600;
    private final float beltInner = distMarsSun * 1.15f;
    private final float beltOuter = distJupiterSun * 0.75f;
    private final float minAstRadius = 1e9f;
    private final float maxAstRadius = 5e9f;
    private final float asteroidMass = 1e15f;

    private final float[] viewport = {0, 0, 1, 1};

    private float zoom = 5f;
    private double[] window = {-zoom * distEarthSun, zoom * distEarthSun, -zoom * distEarthSun, zoom * distEarthSun};
    private SubPlot plt;
    private CelestialBody sun, mercury, venus, earth, mars, jupiter, saturn, ringSaturn1, ringSaturn2, uranus, neptune;
    private List<CelestialBody> planets;
    private List<Asteroid> asteroids;


    // fundo de ecrã estrelas
    private PImage starsBg;

    public SolarSystem() {
        planets = new ArrayList<>();
        asteroids = new ArrayList<>();
    }

    public void onEnter(PApplet p) {
        // background
        starsBg = AssetManager.get().img("stars");

        float maxDist = distMarsSun * 3.5f; // ajustar zoom se quiser ver mais planetas
        window = new double[]{-maxDist, maxDist, -maxDist, maxDist};
        plt = new SubPlot(window, viewport, p.width, p.height);

        PImage sunImg = AssetManager.get().img("sun");
        PImage mercuryImg = AssetManager.get().img("mercury");
        PImage venusImg = AssetManager.get().img("venus");
        PImage earthImg = AssetManager.get().img("earth");
        PImage marsImg = AssetManager.get().img("mars");
        PImage jupiterImg = AssetManager.get().img("jupiter");
        PImage saturnImg = AssetManager.get().img("saturn");
        PImage uranusImg = AssetManager.get().img("uranus");
        PImage neptuneImg = AssetManager.get().img("neptune");

        sun = new CelestialBody(new PVector(), new PVector(), sunMass, distEarthSun / 5, p.color(255, 128, 0), sunImg);
        mercury = new CelestialBody(new PVector(0, distMercurySun), new PVector(mercurySpeed, 0), mercuryMass, distMercurySun / 10, p.color(183, 184, 185), mercuryImg);
        venus = new CelestialBody(new PVector(0, distVenusSun), new PVector(venusSpeed, 0), venusMass, distVenusSun / 10, p.color(165, 124, 27), venusImg);
        earth = new CelestialBody(new PVector(0, distEarthSun), new PVector(earthSpeed, 0), earthMass, distEarthSun / 10, p.color(159, 193, 100), earthImg);
        mars = new CelestialBody(new PVector(0, distMarsSun), new PVector(marsSpeed, 0), marsMass, distMarsSun / 10, p.color(193, 68, 14), marsImg);
        jupiter = new CelestialBody(new PVector(0, distJupiterSun), new PVector(jupiterSpeed, 0), jupiterMass, distJupiterSun / 10, p.color(201, 144, 57), jupiterImg);
        saturn = new CelestialBody(new PVector(0, distSaturnSun), new PVector(saturnSpeed, 0), saturnMass, distSaturnSun / 10, p.color(206, 184, 184), saturnImg);
        uranus = new CelestialBody(new PVector(0, distUranusSun), new PVector(uranusSpeed, 0), uranusMass, distUranusSun / 10, p.color(10, 50, 126), uranusImg);
        neptune = new CelestialBody(new PVector(0, distNeptuneSun), new PVector(neptuneSpeed, 0), neptuneMass, distNeptuneSun / 10, p.color(50, 50, 200), neptuneImg);

        planets.add(sun);
        planets.add(mercury);
        planets.add(venus);
        planets.add(earth);
        planets.add(mars);
        planets.add(jupiter);
        planets.add(saturn);
        planets.add(uranus);
        planets.add(neptune);

        asteroides(p);

        //System.out.println("sunImg" + sunImg.toString());

    }

    private void asteroides(PApplet p) {
        asteroids = new ArrayList<>(numAsteroids);
        for (int i = 0; i < numAsteroids; i++) {
            float r = rand(beltInner, beltOuter);
            float theta = rand(0f, (float) (2 * Math.PI));
            //posição do anel
            float x = (float) (r * Math.cos(theta));
            float y = (float) (r * Math.sin(theta));
            PVector pos = new PVector(x, y);

            //velocidade orbital circular v = sqrt(G*M/r), tangente ao raio
            float v = (float) Math.sqrt(G * sunMass / r);

            //vetor tangente para a orbita no sentido contrario ponteiros do relogio
            float vx = (float) (-v * Math.sin(theta));
            float vy = (float) (v * Math.cos(theta));

            //ligeira variação para espalhar
            vx *= rand(0.98f, 1.02f);
            vy *= rand(0.98f, 1.02f);

            PVector vel = new PVector(vx, vy);

            float radius = rand(minAstRadius, maxAstRadius);
            int color = p.color(200, 200, 200);

            PImage asteroidImg = AssetManager.get().img("asteroid");

            Asteroid a = new Asteroid(pos, vel, asteroidMass, radius, color, asteroidImg);
            asteroids.add(a);
        }
    }

    public void update(PApplet p, float dt) {

        // atualizar planetas (atração sol -> planeta)
        for (CelestialBody planet : planets) {
            if (planet == sun) continue; // sol não se mexe
            PVector f = sun.attraction(planet);
            planet.applyForce(f);
            planet.move(dt * speedUp);
        }

        // atualizar asteriodes
        for (Asteroid a : asteroids) {
            PVector f = sun.attraction(a);
            a.applyForce(f);
            a.move(dt * speedUp);
        }
    }

    public void display(PApplet p) {
        // estrelas background
        p.background(10);
        p.imageMode(PApplet.CORNER);
        if (starsBg != null) p.image(starsBg, 0, 0, p.width, p.height);

        // desenhar tudo
        for (CelestialBody planet : planets) planet.display(p, plt);

        for (Asteroid a : asteroids) a.display(p, plt);
    }

    private float rand(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }
}
