package game;

import processing.core.PApplet;

/**
 * Interface que define um estado do jogo.
 * 
 * Um GameState representa um ecrã ou modo específico do jogo,
 * como:
 *  - Menu principal
 *  - Opções
 *  - Jogo em sí
 *  - Game Over
 * 
 * Cada estado é responsável por:
 *  - Inicializar e libertar os seus próprios recursos
 *  - Atualizar a sua lógica interna
 *  - Desenhar o seu conteúdo no ecrã
 *  - Reagir aos inputs do utilizador
 * 
 * O GameApp delega todos os eventos e ciclos de atualização
 * ao estado atualmente ativo
 */

public interface GameState {
    void onEnter(PApplet p);
    void onExit(PApplet p);

    void update(PApplet p, float dt);
    void display(PApplet p);

    void keyPressed(PApplet p);
    void mousePressed(PApplet p);
    void mouseReleased(PApplet p);
    void mouseDragged(PApplet p); 
	void keyReleased(PApplet p);
}
