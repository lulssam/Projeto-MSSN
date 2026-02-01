package game;

import aa.Behavior;
import aa.Wander;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Inimigo do Nível 1.
 *
 * O EnemyLevel1 representa o inimigo mais simples do jogo:
 *  - possui apenas 1 ponto de vida
 *  - utiliza exclusivamente o comportamento aa.Wander
 *  - movimenta-se de forma errática, sem perseguir o jogador
 *
 * Serve como inimigo introdutório, permitindo ao jogador
 * aprender os controlos e mecânicas básicas.
 */

public class EnemyLevel1 extends Enemy {
    protected EnemyLevel1(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, radius, sprite, p);
        this.hp = 1;  //é necessário apenas 1 tiro para matar 
    }

    @Override
    protected void initBehaviors() {
        Behavior wander = new Wander(0.5f);  //movimento erratico simples
        addBehavior(wander);
    }
}