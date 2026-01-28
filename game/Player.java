package game;

import aa.Type;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Classe que representa a nave controlada pelo jogador.
 * <p>
 * O Player é uma entidade que:
 * - Responde a input do teclado
 * - Move-se apenas no eixo horizontal
 * - Não pode sair dos limites do ecrã
 * - Serve como origem para os disparos do jogador
 * <p>
 * A lógica de disparo é gerida externamente (PlayState),
 * sendo esta classe responsável apenas por fornecer a posição
 * correta do canhão da nave.
 */

public class Player extends Body {

    private float speed = 420f; //px/s
    private float friction = 10f; //parar suave

    private boolean left = false;
    private boolean right = false;
    private PImage sprite;

    public Player(PVector pos, float radius, PImage sprite) {
        super(pos, new PVector(), 1.0f, radius, 0);
        this.sprite = sprite;

        // testar
        this.type = Type.PREY;
    }

    public void setLeft(boolean v) {
        left = v;
    }

    public void setRight(boolean v) {
        right = v;
    }

    public void update(float dt, PApplet p) {
        float dir = 0;
        if (left) dir -= 1;
        if (right) dir += 1;

        //define velocidade horizontal, mas com suavidade
        float targetVx = dir * speed;
        vel.x += (targetVx - vel.x) * (1f - (float) Math.exp(-12f * dt));

        //friction quando não há input
        if (dir == 0) {
            vel.x *= (1f - (float) Math.exp(-friction * dt));
        }

        pos.x += vel.x * dt; //atualiza posição (só x)

        //clamp no ecrã (não sai)
        float minX = radius;
        float maxX = p.width - radius;
        if (pos.x < minX) pos.x = minX;
        if (pos.x > maxX) pos.x = maxX;


        pos.y = p.height * 0.85f; //y fixo

        // System.out.println("dt:" + dt + " | frameRate: " + p.frameRate);
    }

    //posição de onde sai o tiro
    public PVector gunMuzzle() {
        return new PVector(pos.x, pos.y - radius);
    }

    @Override
    public void display(PApplet p) {
        if (sprite != null) {
            p.imageMode(PApplet.CENTER);
            p.image(sprite, pos.x, pos.y, radius * 2, radius * 2);
        } else {
            super.display(p);
        }
    }
}
