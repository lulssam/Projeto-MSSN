package game;

import aa.Boid;
import aa.Eye;
import aa.Type;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Classe base abstrata para inimigos do jogo.
 *
 * Um Enemy estende aa.Boid e representa um inimigo com:
 *  - sprite (ou fallback para desenho do boid)
 *  - pontos de vida (hp) e gestão de dano
 *  - limites verticais de movimento (mantém o inimigo na zona superior)
 *  - sistema de visão (aa.Eye) para perseguir/alvo
 *  - feedback visual ao levar dano (flash/piscar via tint)
 *
 * Cada tipo concreto de inimigo deve implementar "initBehaviors()"
 * para definir os comportamentos (pursuit, separation, ...).
 */

public abstract class Enemy extends Boid {

    protected PImage sprite; //imagem do inimigo
    protected int hp; //pontos de vida
    protected float minX, maxX, minY, maxY; //limites de movimento (usado sobretudo no eixo y)
    
    //timers do efeito visual de dano
    protected float damageFlashTimer = 0f;
    protected float damageFlashDuration = 0.15f; //150ms

    protected Enemy(PVector pos, float radius, PImage sprite, PApplet p) {
    	//boid base: massa 1, cor default, tipo predator
        super(pos, 1.0f, radius, p.color(255), p, Type.PREDATOR);
        
        this.sprite = sprite;
        
        //limites verticais para manter inimigos na zona superior
        this.minY = radius;
        this.maxY = p.height / 2.5f;

        this.vel = PVector.random2D().mult(100);  //velocidade inicial aleatoria para dar movimento ao spawn

        //eye inicializado com lista vazia; o target e tracking list sao definidos fora
        this.eye = new Eye(this, new ArrayList<>());

    }

    //metodo abstrato que cada inimigo implementa para definir os seus comportamentos
    protected abstract void initBehaviors();

    @Override
    public void applyBehaviors(float dt) {
        super.applyBehaviors(dt);
        
        keepVertical(); //impede que saia da zona vertical permitida
        
        if (damageFlashTimer > 0f) { damageFlashTimer -= dt;}  //timer do flash de dano
    }
    
    //reflete a velocidade no eixo y quando bate nos limites
    private void keepVertical() {
        if (pos.y < minY) {
            pos.y = minY;
            vel.y *= -1;
        } else if (pos.y > maxY) {
            pos.y = maxY;
            vel.y *= -1;
        }
    }

    @Override
    public void display(PApplet p) {
    	//se houver sprite, desenha com tint para efeito de dano
        if (sprite != null) {
            p.pushMatrix();
            p.translate(pos.x, pos.y);

            p.imageMode(PConstants.CENTER);
            
            //se levou dano desenha com alpha mais baixo (ou a piscar)
            p.pushStyle();
            if (damageFlashTimer > 0f) {
            	//efeito piscar: alterna alpha para criar feedback de hit
            	float blink;
            	if (((int)(damageFlashTimer * 60) % 2) == 0) {
            	    blink = 80f;
            	} else {
            	    blink = 255f;
            	}
                p.tint(255, blink);
            } else {
                p.tint(255, 255);
            }

            p.image(sprite, 0, 0, radius * 2, radius * 2);
            p.popStyle();
            p.popMatrix();
            
        } else { 
            super.display(p); //se não houver sprite usa o display do boid (triangulo)
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
        damageFlashTimer = damageFlashDuration; //ativa animacao de hit
    }

    public boolean isDead() {
        return hp <= 0;
    }
    
    //inimigo é considerado fora se descer abaixo do ecra
    public boolean isOffscreen(PApplet p) {
        return pos.y > p.height + radius;
    }
    
    
}