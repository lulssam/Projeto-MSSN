package game;

import processing.core.PApplet;
import ui.Buttons;


/**
 * Classe que trata dos creditos no final do jogo. Implementa GameState.
 */
public class Credits implements GameState {
    private final GameApp app;
    private final int score;
    private Buttons toMenu;
    private float textY;

    public Credits(GameApp app, int score) {
        this.app = app;
        this.score = score;
    }

    @Override
    public void onEnter(PApplet p) {
        //app.sound().playMusic(); TODO por musica

        float bw = 260, bh = 60;
        toMenu = new Buttons((p.width - bw) / 2f, p.height * 0.85f, bw, bh, "Go to Menu");

        textY = p.height * 0.80f;
    }

    @Override
    public void onExit(PApplet p) {

    }

    @Override
    public void update(PApplet p, float dt) {
        float speed = 40 * dt;
        textY -= speed;
        creditsText(p);
    }

    private void creditsText(PApplet p) {
        String creditos = """
                Foi uma batalha dura,\s
                mas a vitoria foi alcancada!\s

                PROJETO DESENVOLVIDO POR:\s

                Daniela Cafum\s
                &
                Luisa Sampaio\s
                
                No ambito da cadeira\s
                 Modelacao e Simulacao de Sistemas Naturais\s""";

        // TODO por fundo das estrelas
        p.background(0);

        // texto scroll
        p.fill(0, 255, 0);
        p.textSize(24);
        p.textAlign(PApplet.CENTER, PApplet.TOP);
        p.text(creditos, p.width / 2f, textY);

        // mascaras
        p.pushStyle();

        p.rectMode(PApplet.CORNER);
        p.noStroke();
        p.fill(0);

        float headerHeigh = p.height * 0.35f;   // altura limite no topo
        float footerY = p.height * 0.75f;       // altura limite do botão

        p.rect(0, 0, p.width, headerHeigh);                  // tapar o texto que ja subiu
        p.rect(0, footerY, p.width, p.height - footerY);    // barra preta inferior para texto so aparecer DEPOIS do botão

        p.popStyle();

        // titulo fixo
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.fill(255);
        p.textSize(30);
        p.text("GAME FINISHED -  Score: " + score, p.width / 2f, 50);

        toMenu.display(p);
    }

    @Override
    public void display(PApplet p) {

    }

    @Override
    public void keyPressed(PApplet p) {

    }

    @Override
    public void mousePressed(PApplet p) {
        if (toMenu.isClicked(p)) {
            app.settings().lastScore = score;
            app.setState(new MenuState(app, score), p);
        }

    }

    @Override
    public void mouseReleased(PApplet p) {

    }

    @Override
    public void mouseDragged(PApplet p) {

    }

    @Override
    public void keyReleased(PApplet p) {

    }
}
