package physics;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

//Classe ParticleSystem que gere um sistema de partículas que são adicionadas, movidas e destruídas automaticamente

public class ParticleSystem extends Body {

    private final List<ParticlePhysics> particlePhysics;
    private final PSControl psc;

    public ParticleSystem(PVector pos, PVector vel, float mass, float radius, PSControl psc) {
        super(pos, vel, mass, radius, 0);
        this.particlePhysics = new ArrayList<ParticlePhysics>();
        this.psc = psc;
    }

    @Override
    public void move(float dt) {
        super.move(dt); //atualizar estado mover
        addParticles(dt); //adicionar partículas
        
        //atualizar partículas e remover as mortas
        for (int i = particlePhysics.size() - 1; i >= 0; i--) {
            ParticlePhysics p = particlePhysics.get(i);
            p.move(dt);
            if (p.isDead()) particlePhysics.remove(i);
        }
    }

    //método alternativo para separar dt da física e dt da emissão
    public void move(float dtPhysics, float dtEmission) {
		super.move(dtPhysics);
        addParticles(dtEmission);
        for (int i = particlePhysics.size() -1; i >= 0 ; i--) {
            ParticlePhysics p = particlePhysics.get(i);
            p.move(dtPhysics);
            if (p.isDead()) particlePhysics.remove(i);
        }
    }

    //obter controlador do sistema
    public PSControl getPSControl() {
        return psc;
    }
    
    //criar uma nova partícula com parâmetros aleatórios
    protected void addOneParticle() {
        ParticlePhysics particlePhysics = new ParticlePhysics(pos, psc.getRndVel(), psc.getRndRadius(), psc.getColor(), psc.getRndLifetime());
        this.particlePhysics.add(particlePhysics);
    }
    
    //passar posição e velocidade
    protected void addOneParticle(PVector spawnPos, PVector spawnVel, float radius, int color, float lifetime) {
        ParticlePhysics particlePhysics = new ParticlePhysics(spawnPos, spawnVel, radius, color, lifetime);
        this.particlePhysics.add(particlePhysics);
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
        for (ParticlePhysics particlePhysics : this.particlePhysics) {
            particlePhysics.display(p);
        }
    }
}