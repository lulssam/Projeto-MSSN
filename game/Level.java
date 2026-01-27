package game;

import processing.core.PApplet;

/**
 * Classe abstrata que representa um nivel do jogo.
 * 
 * Um Level é responsável por:
 *  - Inicializar o conteúdo específico do nivel (background, inimigos, scores, etc...)
 *  - Atualizar a lógica do nivel ao longo do tempo
 *  - Desenhar o cenário do nivel no ecrã
 *  - Definir a música associada ao nivel
 * 
 * Cada nivel concreto (Level1, Level2, Level3)
 * implementa esta classe e fornece o seu próprio comportamento
 * e visualização.
 * 
 * O PlayState utiliza esta classe para trocar de niveis
 * sem depender de implementações concretas
 */

public abstract class Level {
	public abstract void onEnter(PApplet p);
    public abstract void update(PApplet p, float dt);
    public abstract void display(PApplet p);
    public abstract String music();
    
}
