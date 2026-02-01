package game;

import java.util.List;

import aa.Pursuit;
import aa.Wander;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Inimigo do nível 3.
 *
 * O EnemyLevel3 é a variante mais difícil:
 *  - possui 3 pontos de vida
 *  - movimenta-se com wander mais agressivo do que nos níveis anteriores
 *  - pode ser promovido a "chaser" através de makeChaser, ativando
 *    perseguição ao jogador com aa.Pursuit
 *
 * A seleção de quais inimigos se tornam chasers é feita externamente
 * (EnemyManager).
 */

public class EnemyLevel3 extends Enemy {
	
	private float seekWeight = 1.25f; //peso do pursuit
    private boolean isPursuer = false; //flag para indicar se tem pursuit ativo

    protected EnemyLevel3(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, radius, sprite, p);
        
        this.hp = 3;  //é necessário 3 tiros do player para matar
        this.maxY = p.height / 2f; //permite descer mais
   
        //tuning do movimento (mais agressivo do que nivel 2)
        this.dna.maxSpeed = 200;
        this.dna.maxForce = 70;
    }
    
	@Override
	protected void initBehaviors() {
		 addBehavior(new Wander(1.3f)); //comportamento base
	}
    
	//ativa pursuit para transformar este inimigo num chaser
    public void makeChaser(Body target, List<Body> trackingList) {
        isPursuer = true;

        setTarget(target, trackingList); //liga eye ao target e lista de tracking
        
        //chaser: mais rapido e mais reativo
        this.dna.maxSpeed *= 1.30f;
        this.dna.maxForce *= 1.20f;

        addBehavior(new Pursuit(seekWeight));
    }

    public boolean isChaser() {
        return isPursuer;
    }


}
