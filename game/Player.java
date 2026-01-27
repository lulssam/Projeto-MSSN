package game;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Classe que representa a nave controlada pelo jogador.
 * 
 * O Player é uma entidade que:
 *  - Responde a input do teclado
 *  - Move-se apenas no eixo horizontal
 *  - Não pode sair dos limites do ecrã
 *  - Serve como origem para os disparos do jogador
 * 
 * A lógica de disparo é gerida externamente (PlayState),
 * sendo esta classe responsável apenas por fornecer a posição
 * correta do canhão da nave.
 */

public class Player extends Entity {

    private float speed = 420f; //px/s
    private float friction = 10f; //parar suave

    private boolean left = false;
    private boolean right = false;

    public Player(PVector pos, float radius, PImage sprite) {
        super(pos, radius, sprite);
    }

    public void setLeft(boolean v) {left = v;}
    public void setRight(boolean v) {right = v;}

    public void update(float dt, PApplet p) {
        float dir = 0;
        if (left) dir -= 1;
        if (right) dir += 1;

        //define velocidade horizontal, mas com suavidade
        float targetVx = dir * speed;
        vel.x += (targetVx - vel.x) * (1f - (float)Math.exp(-12f * dt));

        //friction quando não há input
        if (dir == 0) {
            vel.x *= (1f - (float)Math.exp(-friction * dt));
        }

        pos.x += vel.x * dt; //atualiza posição (só x)

        //clamp no ecrã (não sai)
        float minX = radius;
        float maxX = p.width - radius;
        if (pos.x < minX) pos.x = minX;
        if (pos.x > maxX) pos.x = maxX;

        
        pos.y = p.height * 0.85f; //y fixo
    }
    
    //posição de onde sai o tiro
    public PVector gunMuzzle() {
        return new PVector(pos.x, pos.y - radius);
    }
    
}
