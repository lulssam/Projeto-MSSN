package physics;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

import java.util.ArrayList;
import java.util.List;

//Classe ParticleSystem que gere um sistema de partículas que são adicionadas, movidas e destruídas automaticamente

public class ParticleSystem extends Body {

    private final List<Particle> particles;
    private final PSControl psc;

    public ParticleSystem(PVector pos, PVector vel, float mass, float radius, PSControl psc) {
        super(pos, vel, mass, radius, 0);
        this.particles = new ArrayList<Particle>();
        this.psc = psc;
    }

    @Override
    public void move(float dt) {
        super.move(dt); //atualizar estado mover
        addParticles(dt); //adicionar partículas
        
        //atualizar partículas e remover as mortas
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.move(dt);
            if (p.isDead()) particles.remove(i);
        }
    }

    //método alternativo para separar dt da física e dt da emissão
    public void move(float dtPhysics, float dtEmission) {
		super.move(dtPhysics);
        addParticles(dtEmission);
        for (int i = particles.size() -1; i >= 0 ; i--) {
            Particle p = particles.get(i);
            p.move(dtPhysics);
            if (p.isDead()) particles.remove(i);
        }
    }

    //obter controlador do sistema
    public PSControl getPSControl() {
        return psc;
    }
    
    //criar uma nova partícula com parâmetros aleatórios
    private void addOneParticle() {
        Particle particle = new Particle(pos, psc.getRndVel(), psc.getRndRadius(), psc.getColor(), psc.getRndLifetime());
        particles.add(particle);
    }
    
    //emitir partículas proporcionalmente ao fluxo
    public void addParticles(float dt) {
        float particlesPerFrame = psc.getFlow() * dt;
        int n = (int) particlesPerFrame;
        float f = particlesPerFrame - n;
        
        //emitir partículas inteiras
        for (int i = 0; i < n; i++) {
            addOneParticle();
        }
        
        //probabilidade de emitir uma extra (para suavizar o fluxo)
        if (Math.random() < f) {
            addOneParticle();
        }
    }

    public void display(PApplet p) {
        for (Particle particle : particles) {
            particle.display(p);
        }
    }
}
