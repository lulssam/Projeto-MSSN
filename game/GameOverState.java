package game;

import processing.core.PApplet;
import ui.Buttons;

/**
 * Estado que representa o ecrã de fim de jogo.
 *
 * O GameOverState é apresentado quando o jogador perde todas as vidas e é responsável por:
 *  - apresentar a mensagem de fim ("game over")
 *  - mostrar a pontuação final do jogador
 *  - disponibilizar uma transição de volta ao menu
 *  - guardar o último score nas settings
 *
 * Este estado não executa lógica de gameplay, funcionando apenas como
 * ecrã informativo e de transição.
 */

public class GameOverState implements GameState {

    private final GameApp app;
    private final int score;

    private Buttons back;

    public GameOverState(GameApp app, int score) {
        this.app = app;
        this.score = score;
    }

    @Override
    public void onEnter(PApplet p) {
    	app.sound().playMusic("/game_over.wav", app.settings().volume, app.settings().muted); //musica de game over
    	
        float bw = 260, bh = 60;
        back = new Buttons((p.width - bw) / 2f, p.height * 0.70f, bw, bh, "Back to Menu");
    }

    @Override
    public void onExit(PApplet p) { }

    @Override
    public void update(PApplet p, float dt) { }

    @Override
    public void display(PApplet p) {
        p.background(0);

        p.fill(255);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(44);
        p.text("GAME OVER", p.width / 2f, p.height * 0.25f); //titulo principal

        p.textSize(22); 
        p.text("Score: " + score, p.width / 2f, p.height * 0.40f); //pontuacao final

        back.display(p);
    }

    @Override
    public void keyPressed(PApplet p) {
        if (p.key == 'm' || p.key == 'M') { 
        	app.settings().lastScore = score; //guarda score para mostrar no menu
            app.setState(new MenuState(app, score), p);
        }
    }

    @Override
    public void mousePressed(PApplet p) {
        if (back.isClicked(p)) {
        	app.settings().lastScore = score; //guarda score para mostrar no menu
        	app.setState(new MenuState(app, score), p);
        }
    }

    @Override
    public void mouseReleased(PApplet p) { }

    @Override
    public void mouseDragged(PApplet p) { }

    @Override
    public void keyReleased(PApplet p) { }
}
