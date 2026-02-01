package physics;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gere um sistema de partículas associado a um corpo no mundo.
 *
 * Um ParticleSystem herda de Body para poder ter posição/velocidade próprias,
 * funcionando como "emissor" de partículas. As partículas geradas são instâncias de
 *  ParticlePhysics, que:
 *  - são criadas com parâmetros fornecidos por PSControl
 *  - são atualizadas a cada frame (movimento + timer)
 *  - são removidas automaticamente quando excedem o seu tempo de vida
 *
 * A emissão é baseada num fluxo (partículas/segundo) e utiliza uma componente fracionária
 * (probabilidade) para manter um fluxo médio estável sem depender do framerate.
 *
 * Esta classe é focada em efeitos visuais e não contém lógica de colisões ou gameplay.
 */

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
        super.move(dt);   //atualizar estado mover
        addParticles(dt); //criação baseada no dt do frame
        
        //atualizar particulas e remover as que expiraram
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

    //devolve o controlador do sistema
    public PSControl getPSControl() {
        return psc;
    }
    
    //cria uma particula nova usando os parametros aleatorios do controlador
    protected void addOneParticle() {
        ParticlePhysics particlePhysics = new ParticlePhysics(pos, psc.getRndVel(), psc.getRndRadius(), psc.getColor(), psc.getRndLifetime());
        this.particlePhysics.add(particlePhysics);
    }
    
    //cria uma particula com parametros definidos (spawn manual)
    protected void addOneParticle(PVector spawnPos, PVector spawnVel, float radius, int color, float lifetime) {
        ParticlePhysics particlePhysics = new ParticlePhysics(spawnPos, spawnVel, radius, color, lifetime);
        this.particlePhysics.add(particlePhysics);
    }
    
    //emitir partículas proporcionalmente ao fluxo
    public void addParticles(float dt) {
    	
        float particlesPerFrame = psc.getFlow() * dt;  //multiplica-se por dt para obter particulas deste frame
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