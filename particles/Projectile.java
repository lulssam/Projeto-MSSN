package particles;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe que representa um projétil ativo no jogo.
 *
 * Um Projectile corresponde a um disparo lançado pelo jogador ou inimigos e combina:
 *  - movimento simples (posição e velocidade)
 *  - dano associado
 *  - efeitos visuais baseados em partículas
 *
 * Visualmente, o projétil é representado como uma "bola de fogo", composta por:
 *  - núcleo luminoso
 *  - glow em camadas
 *  - partículas de aura e rasto geradas durante o movimento
 *
 * As partículas são atualizadas e removidas automaticamente quando expiram, e a lista é limitada
 * para evitar crescimento excessivo e garantir desempenho estável.
 *
 * Esta classe não resolve colisões: a deteção e aplicação de dano são feitas por sistemas externos.
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
        int auraCount = 3; //quantidade de particulas por frame no glow (mais = mais fogo)
        for (int i = 0; i < auraCount; i++) {
            float ang = (float)(Math.random() * Math.PI * 2);
            float r = (float)(Math.random() * radius * 1.2f);  //espalha spawn ligeiramente fora do raio para aura parecer viva

            PVector spawn = new PVector(pos.x + (float)Math.cos(ang) * r, pos.y + (float)Math.sin(ang) * r);

            //velocidade pequena aleatória
            PVector pv = new PVector((float)(Math.random() * 50 - 25), (float)(Math.random() * 50 - 25));
            pv.mult(0.02f); //escala para manter jitter pequeno (efeito de chama)

            particleProjectiles.add(new ParticleProjectile(spawn, pv, radius * 2.2f, 0.18f, this.color));
        }

        //rasto (trail) leve para nao sobrecarregar
        int trailCount = 1;
        for (int i = 0; i < trailCount; i++) {
            PVector spawn = pos.copy();

            //direção oposta da vel
            PVector back = vel.copy();
            if (back.mag() > 0.001f){back.normalize();}  //evita normalizar vetor quase zero
            back.mult(-0.08f * radius);  //offset para tras do projetil (rasto nasce atras do nucleo)

            spawn.add(back);

            PVector pv = new PVector((float)(Math.random() * 30 - 15), (float)(Math.random() * 80 + 40));
            pv.mult(0.01f);  //rasto mais lento/suave que a aura

            particleProjectiles.add(new ParticleProjectile(spawn, pv, radius * 2.0f, 0.25f, this.color));
        }
        
        //remove particulas expiradas
        for (int i = particleProjectiles.size() - 1; i >= 0; i--) {
            ParticleProjectile par = particleProjectiles.get(i);
            par.update(dt);
            if (par.dead()) particleProjectiles.remove(i);
        }

        //limite hard para evitar lista gigante e quedas de fps
        if (particleProjectiles.size() > 120) {
            particleProjectiles.subList(0, particleProjectiles.size() - 120).clear();
        }
        
    }
    
    public void display(PApplet p) {
    	
    	//particulas primeiro
        for (ParticleProjectile sp : particleProjectiles) sp.display(p);

        //core glow
        p.pushStyle();
        p.blendMode(PApplet.ADD);  //blend aditivo para glow mais intenso
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
