package game;

import aa.Type;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Entidade jogável controlada pelo utilizador.
 *
 * O Player representa a nave do jogador e é responsável por:
 *  - ler estado de input (left/right) e converter em movimento horizontal
 *  - aplicar suavização de aceleração/desaceleração (interpolação exponencial)
 *  - aplicar fricção quando não existe input, evitando paragens bruscas
 *  - limitar a posição aos limites do ecrã (clamp em x)
 *  - fornecer a posição do canhão (gunMuzzle) para disparos
 *  - apresentar feedback visual ao levar dano (flash via tint)
 *
 * A lógica de disparo e colisões é gerida externamente (ex: PlayState e CollisionSystem).
 */

public class Player extends Body {

    private float speed = 420f; //velocidade horizontal (px/s)
    private float friction = 10f; //friccao para desacelerar sem input

    private boolean left = false; //input esquerda
    private boolean right = false; //input direita
    private PImage sprite; //sprite do jogador
    
    //valores para a animação de damage
    private float damageFlashTimer = 0f; //timer do flash de dano
    private final float damageFlashDuration = 0.15f; //duracao do flash

    public Player(PVector pos, float radius, PImage sprite) {
        super(pos, new PVector(), 1.0f, radius, 0);
        this.sprite = sprite;

        this.type = Type.PREY; //tipo usado pelo sistema eye
    }

    public void setLeft(boolean v) {
        left = v;
    }

    public void setRight(boolean v) {
        right = v;
    }
    
    public void flashDamage() {
        damageFlashTimer = damageFlashDuration;
    }

    public void update(float dt, PApplet p) {
        float dir = 0;
        if (left) dir -= 1;
        if (right) dir += 1; //-1 esquerda, +1 direita

        //suaviza mudanca de velocidade para evitar "snaps"
        float targetVx = dir * speed;
        vel.x += (targetVx - vel.x) * (1f - (float) Math.exp(-12f * dt));

        //quando nao ha input, aplica friccao para parar suavemente
        if (dir == 0) {
            vel.x *= (1f - (float) Math.exp(-friction * dt));
        }

        pos.x += vel.x * dt; //movimento apenas no eixo x

        //clamp para nao sair do ecrã
        float minX = radius;
        float maxX = p.width - radius;
        if (pos.x < minX) pos.x = minX;
        if (pos.x > maxX) pos.x = maxX;


        pos.y = p.height * 0.85f; //mantem o jogador numa linha fixa (no y)

        if (damageFlashTimer > 0f) { damageFlashTimer -= dt;} //timer do feedback visual de dano
    }

    //ponto de spawn do tiro (topo da nave)
    public PVector gunMuzzle() {
        return new PVector(pos.x, pos.y - radius);
    }
    
    

    @Override
    public void display(PApplet p) {
    	 p.pushStyle();
    	 
    	 //quando leva dano, baixa o alpha do sprite para dar feedback
    	 if (damageFlashTimer > 0f) { p.tint(255, 120);}
    	 else { p.tint(255, 255);}
    	
        if (sprite != null) {
            p.imageMode(PApplet.CENTER);
            p.image(sprite, pos.x, pos.y, radius * 2, radius * 2);
    
        } else {
            super.display(p);
        }
        
        p.popStyle();
    }
}
