package game;

import aa.Pursuit;
import aa.Wander;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.List;

/**
 * Inimigo do Nível 2.
 *
 * O EnemyLevel2 é mais resistente e agressivo do que o inimigo do nível 1:
 *  - possui 2 pontos de vida
 *  - pode ocupar uma zona vertical maior (desce mais no ecrã)
 *  - tem wander como movimento base
 *  - alguns inimigos podem ser configurados como pursuers através de setupTarget,
 *    passando a perseguir o jogador com aa.Pursuit
 *
 * A ativação de pursuit é opcional e depende da seleção efetuada no EnemyManager.
 */

public class EnemyLevel2 extends Enemy {

    private float seekWeight; //peso do pursuit quando ativado

    protected EnemyLevel2(PVector pos, float radius, PImage sprite, PApplet p) {
        super(pos, radius, sprite, p);
        this.hp = 2;  //é necessário 2 tiros do player para matar
        this.maxY = p.height / 2f;  //permite descer mais do que no nivel 1

        this.seekWeight = 1.2f;
        
        //tuning do movimento (mais rapido/agressivo)
        this.dna.maxSpeed = 180;
        this.dna.maxForce = 60; 
    }

    @Override
    protected void initBehaviors() {
    	addBehavior(new Wander(1.0f)); //comportamento base
    }

    public void setupTarget(Body target, List<Body> trackingList) {
    	  setTarget(target, trackingList); //liga o eye ao target e lista de tracking
    	  
    	  //os pursuers perseguem o player mais rápido 
    	  this.dna.maxSpeed *= 1.25f;
    	  this.dna.maxForce *= 1.15f;
    	  
          addBehavior(new Pursuit(seekWeight)); //só os escolhidos no EnemyManager recebem pursuit
    }
    

    
}
