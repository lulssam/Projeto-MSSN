package game;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import ui.AssetManager;
import ui.Buttons;

/**
 * Estado que representa o menu principal do jogo.
 *
 * O code MenuState implementa a interface inicial e não contém lógica de gameplay.
 * As suas responsabilidades principais são:
 *  - tocar a música de menu ao entrar no estado
 *  - desenhar um background de estrelas com scroll vertical
 *  - apresentar o título do jogo (imagem ou fallback em texto)
 *  - disponibilizar botões para iniciar o jogo, abrir opções e sair
 *  - mostrar o último score obtido (quando aplicável)
 *
 * As interações de clique são tratadas em "#mousePressed(PApplet)",
 * fazendo transição para outros GameState através do GameApp.
 */

public class MenuState implements GameState {

    private final GameApp app;

    private PImage titleImg;
    private PImage starsBg;
    private float starsOffset = 0;
    
    private int lastScore = 0;
    
    private List<Buttons> buttons;

    public MenuState(GameApp app) {
    	this(app, 0);
    }
    
    public MenuState(GameApp app, int lastScore) {
        this.app = app;
        this.lastScore = lastScore;
    }

    @Override
    public void onEnter(PApplet p) {
    	
    	app.sound().playMusic("/menu_music.wav", app.settings().volume, app.settings().muted); //musica do menu
    	
        titleImg = AssetManager.get().img("title");
        starsBg = AssetManager.get().img("stars");
        
        buttons = new ArrayList<>();

        float bw = 260;
        float bh = 60;
        float x = (p.width - bw) / 2f;
        float y0 = p.height * 0.55f;
        
        //botoes do menu
        buttons.add(new Buttons(x, y0, bw, bh, "PLAY"));
        buttons.add(new Buttons(x, y0 + 80, bw, bh, "Options"));
        buttons.add(new Buttons(x, y0 + 160, bw, bh, "Exit"));
    }

    @Override
    public void onExit(PApplet p) { }

    @Override
    public void update(PApplet p, float dt) {
    	
    	//scroll vertical do background de estrelas
        starsOffset += 20 * dt; //velocidade do fundo
        if (starsBg != null && starsOffset > starsBg.height) {
            starsOffset = 0;
        }
        
        //atualiza animacao de hover dos botoes
        for (Buttons b : buttons) { 
        	b.update(p, dt);
        }
    }

    @Override
    public void display(PApplet p) {
        p.background(10);
        
        p.imageMode(PApplet.CORNER); 
        
        //background
        if (starsBg != null) {
            float h = starsBg.height;
            starsOffset = starsOffset % h;
            
            //background em tiles para preencher o ecrã durante o scroll
            for (float y = -h; y < p.height + h; y += h) {
                p.image(starsBg, 0, y + starsOffset, p.width, h);
            }
        }
        
        p.imageMode(PApplet.CENTER);
        
        //titulo como imagem
        if (titleImg != null) {
            p.imageMode(PApplet.CENTER);
            
            //escala o titulo para caber em 80% da largura
            float maxW = p.width * 0.8f;
            float scale = maxW / titleImg.width;
            
            float drawW = titleImg.width * scale;
            float drawH = titleImg.height * scale;

            p.image(titleImg, p.width / 2f, p.height * 0.25f, drawW, drawH);
        } else {
        	//fallback se a imagem do titulo nao existir
            p.fill(255);
            p.textAlign(PApplet.CENTER, PApplet.CENTER);
            p.textSize(48);
            p.text("SPACE INVADERS", p.width / 2f, p.height * 0.25f);
        }
        
        //score no menu
        p.pushStyle();
        if (AssetManager.get().font("ui") != null) p.textFont(AssetManager.get().font("ui"));
        p.fill(200, 255, 200);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(18);
        p.text("LAST SCORE: " + lastScore, p.width / 2f, p.height * 0.50f);
        p.popStyle();
        
        //botões
        for (Buttons b : buttons) {
        	b.display(p);
        }

        p.fill(180);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(14);
    }

    @Override
    public void keyPressed(PApplet p) {

    }

    @Override
    public void mousePressed(PApplet p) {
    	//apenas verifica se esta em hover
        if (buttons.get(0).isClicked(p)) {
            app.setState(new PlayState(app), p);
        } else if (buttons.get(1).isClicked(p)) {
            app.setState(new OptionsState(app), p);
        } else if (buttons.get(2).isClicked(p)) {
            p.exit();
        }
    }

    @Override
    public void mouseReleased(PApplet p) { }

    @Override
    public void mouseDragged(PApplet p) { }

    @Override
    public void keyReleased(PApplet p) { }
}
