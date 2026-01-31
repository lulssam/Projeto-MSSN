package particles;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe que representa um "projétil" ativo no jogo
 * 
 * Um Projectile corresponde a um disparo lançado pelo jogador ou inimigos e combina:
 *  - Movimento fisico simples (posição e velocidade)
 *  - Dano associado
 *  - Efeitos visuais baseados em particulas
 * 
 * O projétil é visualmente representado como uma "bola de fogo",
 * composta por:
 *  - Um núcleo luminoso
 *  - Um efeito de glow em camadas
 *  - Um conjunto de particulas de fogo e rasto
 * 
 * As particulas são geradas continuamente durante o movimento
 * do projétil e removidas automaticamente quando a sua vida termina,
 * garantindo um efeito visual dinâmico sem crescimento excessivo
 * da lista de particulas.
 * 
 * Esta classe não gere colisões, essa responsabilidade pertence
 * a sistemas externos (CollisionSystem)
 */

public class Projectile {
    private PVector pos;
    private PVector vel;
    private float radius;
    private int damage;
    private int color;
    
    private List<ParticleProjectile> particleProjectiles = new ArrayList<>();

    public Projectile(PVector pos, PVector vel, float radius, int damage, int color) {
        this.pos = pos.copy();
        this.vel = vel.copy();
        this.radius = radius;
        this.damage = damage;
        this.color = color;
    }
    
    public void update(float dt) {
    	
    	//movimento do tiro
        pos.add(PVector.mult(vel, dt));
        
        //partículas de fogo 
        int auraCount = 3; //mais = mais fogo
        for (int i = 0; i < auraCount; i++) {
            float ang = (float)(Math.random() * Math.PI * 2);
            float r = (float)(Math.random() * radius * 1.2f);

            PVector spawn = new PVector(pos.x + (float)Math.cos(ang) * r, pos.y + (float)Math.sin(ang) * r);

            //velocidade pequena aleatória
            PVector pv = new PVector((float)(Math.random() * 50 - 25), (float)(Math.random() * 50 - 25));
            pv.mult(0.02f);

            particleProjectiles.add(new ParticleProjectile(spawn, pv, radius * 2.2f, 0.18f, this.color));
        }

        //trail para tras
        int trailCount = 1;
        for (int i = 0; i < trailCount; i++) {
            PVector spawn = pos.copy();

            //direção oposta da vel
            PVector back = vel.copy();
            if (back.mag() > 0.001f){back.normalize();}
            back.mult(-0.08f * radius);

            spawn.add(back);

            PVector pv = new PVector((float)(Math.random() * 30 - 15), (float)(Math.random() * 80 + 40));
            pv.mult(0.01f);

            particleProjectiles.add(new ParticleProjectile(spawn, pv, radius * 2.0f, 0.25f, this.color));
        }
        
        //atualizar/remover particulas
        for (int i = particleProjectiles.size() - 1; i >= 0; i--) {
            ParticleProjectile sp = particleProjectiles.get(i);
            sp.update(dt);
            if (sp.dead()){
                particleProjectiles.remove(i);}
        }

        //evita lista gigante
        if (particleProjectiles.size() > 120) {
            particleProjectiles.subList(0, particleProjectiles.size() - 120).clear();
        }
        
    }
    
    public void display(PApplet p) {
    	
    	//particulas primeiro
        for (ParticleProjectile sp : particleProjectiles) sp.display(p);

        //core glow
        p.pushStyle();
        p.blendMode(PApplet.ADD);
        p.noStroke();

        //camadas de glow
        p.fill(this.color, 80);
        p.circle(pos.x, pos.y, radius * 10);

        p.fill(this.color, 150);
        p.circle(pos.x, pos.y, radius * 6);

        //nucleo
        p.fill(255, 240, 200, 120);
        p.circle(pos.x, pos.y, radius * 3);

        p.blendMode(PApplet.BLEND);
        p.popStyle();
    }
    
    //verificar se a particula saiu do ecrã para a poder eliminar
    public boolean isOffscreen(PApplet p) {
        return (pos.y < -radius || pos.y > p.height + radius || pos.x < -radius || pos.x > p.width + radius);
    }
    
    //getters
    public PVector getPos() { return pos; }
    public float getRadius() { return radius; }
    public int getDamage() { return damage; }
}

