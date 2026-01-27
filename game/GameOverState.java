package game;

import processing.core.PApplet;
import ui.Buttons;

/**
 * Estado que representa o ecrã de fim de jogo
 * 
 * O GameOverState é apresentado quando o jogador perde todas as vidas.
 * 
 * Este estado é responsável por:
 *  - Mostrar a mensagem "Game Over"
 *  - Apresentar a pontuação final do jogador
 *  - Disponibilizar uma opção para regressar ao menu principal
 *  - Tratar input do utilizador para sair deste estado
 * 
 * Não contém lógica de jogo ativa, funcionando apenas como
 * um ecrã informativo e de transição.
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
        p.text("GAME OVER", p.width / 2f, p.height * 0.25f);

        p.textSize(22);
        p.text("Score: " + score, p.width / 2f, p.height * 0.40f);

        back.display(p);
    }

    @Override
    public void keyPressed(PApplet p) {
        if (p.key == 'm' || p.key == 'M') app.setState(new MenuState(app, score), p);
    }

    @Override
    public void mousePressed(PApplet p) {
        if (back.isClicked(p)) app.setState(new MenuState(app), p);
    }

	@Override
	public void mouseReleased(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(PApplet p) {
		// TODO Auto-generated method stub
		
	}
}
