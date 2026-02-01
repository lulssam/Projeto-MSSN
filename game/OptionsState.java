package game;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import ui.AssetManager;
import ui.Buttons;

/**
 * Estado que representa o ecrã de opções do jogo.
 *
 * Este estado permite ajustar definições globais que afetam o jogo em tempo real:
 *  - slider para controlo do volume global
 *  - checkbox para mute total (ignora o volume)
 *  - botão para reset do score com confirmação (pop-up)
 *  - botão para regressar ao menu principal
 *
 * As alterações atualizam diretamente Settings e são aplicadas ao
 * audio.SoundManager imediatamente, garantindo feedback instantâneo.
 *
 * Quando o pop-up de confirmação está ativo, apenas os seus botões
 * recebem input, evitando cliques acidentais nos controlos por trás.
 */

public class OptionsState implements GameState {

    private final GameApp app;
    
    private PImage starsBg;
    private float starsOffset = 0;
    
    private Buttons back;
    
    //reset score e pop-up
    private Buttons resetScore;
    private boolean showResetPopup = false;
    private Buttons confirmReset;
    private Buttons cancelReset;

    //slider volume
    private float sliderX, sliderY, sliderW, sliderH;
    private boolean draggingVolume = false;
    
    //checkbox mute
    private float cbX, cbY, cbSize;

    public OptionsState(GameApp app) {
        this.app = app;
    }

    @Override
    public void onEnter(PApplet p) {
    	starsBg = AssetManager.get().img("stars");
    	layout(p); //calcula posicoes e dimensoes em funcao da resolucao
    }
    
    private void layout(PApplet p) {
        float bw = 320, bh = 60;

        back = new Buttons((p.width - bw) / 2f, p.height * 0.82f, bw, bh, "BACK");

        //slider volume
        sliderW = p.width * 0.55f;
        sliderH = 14;
        sliderX = (p.width - sliderW) / 2f;
        sliderY = p.height * 0.40f;

        //checkbox de mute (posicionada com base no tamanho do texto)
        cbSize = 22;
        float labelWidth = 50;
        float spacing = 12;     
        float totalW = cbSize + spacing + labelWidth;

        cbX = (p.width - totalW) / 2f;
        cbY = p.height * 0.56f;
        
        //botão reset score
        float rbw = 150, rbh = 40;
        resetScore = new Buttons((p.width - rbw) / 2f, p.height * 0.72f, rbw, rbh, "RESET SCORE");
        
        resetScore.setTextSize(13);
        resetScore.setHoverEnabled(true);

        //botões do pop-up de confirmação
        float pw = 240, ph = 56;
        float popY = p.height * 0.62f;   //y base dentro do pop-up
        confirmReset = new Buttons(p.width / 2f - pw - 18, popY, pw, ph, "CONFIRM");
        cancelReset  = new Buttons(p.width / 2f + 18,      popY, pw, ph, "CANCEL");
        
    }

    @Override
    public void onExit(PApplet p) { }
 
    @Override
    public void update(PApplet p, float dt) {
    	
    	//scroll do background
    	starsOffset += 20 * dt;
    	if (starsBg != null && starsOffset > starsBg.height) {starsOffset = 0;}

        back.update(p, dt);
        resetScore.update(p, dt);
        
        //animação dos botões do pop-up (hover)
        if (showResetPopup) {
            confirmReset.update(p, dt);
            cancelReset.update(p, dt);
        }
        
        //arrastar volume apenas quando nao esta muted
        if (!app.settings().muted && draggingVolume) {
            float t = (p.mouseX - sliderX) / sliderW;
            t = PApplet.constrain(t, 0, 1);
            app.settings().volume = t;
            app.sound().applyVolume(app.settings().volume, app.settings().muted);
        }
    }
    
    @Override
    public void display(PApplet p) {
    	
    	//background stars
    	p.background(10);
    	p.imageMode(PApplet.CORNER);

    	if (starsBg != null) {
    	    float h = starsBg.height;
    	    starsOffset = starsOffset % h;

    	    for (float y = -h; y < p.height + h; y += h) {
    	        p.image(starsBg, 0, y + starsOffset, p.width, h);
    	    }
    	}

    	//overlay escuro para dar contraste aos controlos
    	p.noStroke();
    	p.fill(0, 170);
    	p.rect(0, 0, p.width, p.height);

    	p.imageMode(PApplet.CENTER);
        
        //titulo
        p.fill(255);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(36);
        p.text("OPTIONS", p.width / 2f, p.height * 0.2f);
        
        //volume
        p.fill(200);
        p.textSize(18);
        p.text("VOLUME", p.width / 2f, p.height * 0.30f);

        drawVolumeSlider(p);

        //volume label
        p.fill(160);
        p.textSize(14);
        if (app.settings().muted) {
            p.text("MUTED", p.width / 2f, p.height * 0.47f);
        } else {
            p.text((int)(app.settings().volume * 100) + "%", p.width / 2f, p.height * 0.47f);
        }

        //mute checkbox
        drawMuteCheckbox(p);
        
        //mostrar score atual + botão reset
        p.fill(200);
        p.textSize(16);
        p.text("CURRENT SCORE: " + app.settings().lastScore, p.width / 2f, p.height * 0.66f);

        resetScore.display(p);
        back.display(p);
        
        //popup desenhado por cima de tudo quando ativo
        if (showResetPopup) { drawResetPopup(p);}
    }
    
    
    private void drawVolumeSlider(PApplet p) {
        float t = app.settings().volume;

        p.pushStyle();
        
        //track
        p.noStroke();
        p.fill(40);
        p.rect(sliderX, sliderY, sliderW, sliderH, 8);

        //fill do slider fica mais escuro quando muted para indicar estado inativo
        if (app.settings().muted) { p.fill(0, 255, 0, 60);}
        else p.fill(0, 255, 0, 180);
        p.rect(sliderX, sliderY, sliderW * t, sliderH, 8);

        //knob
        float kx = sliderX + sliderW * t;
        if (app.settings().muted) { p.fill(0, 255, 0, 90);}
        else p.fill(0, 255, 0);
        p.rect(kx - 6, sliderY - 6, 12, sliderH + 12, 6);

        //outline
        p.noFill();
        p.stroke(0, 255, 0, app.settings().muted ? 80 : 140);
        p.strokeWeight(2);
        p.rect(sliderX, sliderY, sliderW, sliderH, 8);

        p.popStyle();
    }
    
    private void drawMuteCheckbox(PApplet p) {
        boolean hover = isOverRect(p.mouseX, p.mouseY, cbX, cbY, cbSize, cbSize);

        p.pushStyle();

        //box outline
        p.noFill();
        p.stroke(0, 255, 0, hover ? 200 : 140);
        p.strokeWeight(2);
        p.rect(cbX, cbY, cbSize, cbSize, 4);

        //checked fill
        if (app.settings().muted) {
            p.fill(0, 255, 0, 160);
            p.noStroke();
            p.rect(cbX + 4, cbY + 4, cbSize - 8, cbSize - 8, 3);
        }

        p.fill(200);
        p.textAlign(PApplet.LEFT, PApplet.CENTER);
        p.textSize(16);
        p.text("MUTE", cbX + cbSize + 12, cbY + cbSize / 2f);

        p.popStyle();
    }
    
    private void drawResetPopup(PApplet p) {
        float w = p.width * 0.72f;
        w = PApplet.constrain(w, 420, 700);
        float h = 220;
        float x = (p.width - w) / 2f;
        float y = (p.height - h) / 2f;

        p.pushStyle();

        //escurecer por tras
        p.noStroke();
        p.fill(0, 200);
        p.rect(0, 0, p.width, p.height);

        //caixa do pop-up
        int neon = p.color(0, 255, 0);

        p.fill(0, 0, 0, 210);
        p.stroke(neon, 180);
        p.strokeWeight(3);
        p.rect(x, y, w, h, 14);

        //glow
        p.noFill();
        p.stroke(neon, 70);
        p.strokeWeight(12);
        p.rect(x, y, w, h, 14);

        //texto
        PFont ui = AssetManager.get().font("ui");
        if (ui != null) { p.textFont(ui);}

        p.fill(200, 255, 200);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(20);
        p.text("Reset score?", p.width / 2f, y + 70);

        p.fill(170);
        p.textSize(14);
        p.text("This will set your current score to 0", p.width / 2f, y + 105);

        //botões dentro do popup
        float pad = 24f;  //margem
        float gap = 18f;  //espaço entre botões
        float bh = 52f;   //altura dos botões
        float by = y + h - pad - bh;

        float bw = (w - 2 * pad - gap) / 2f;
        float bx1 = x + pad;
        float bx2 = x + pad + bw + gap;

        //atualiza bounds
        confirmReset.setBounds(bx1, by, bw, bh);
        cancelReset.setBounds(bx2, by, bw, bh);

        confirmReset.display(p);
        cancelReset.display(p);

        p.popStyle();
    }
    
    private boolean isOverRect(float mx, float my, float x, float y, float w, float h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public void keyPressed(PApplet p) {
        if (p.key == 'm' || p.key == 'M') app.setState(new MenuState(app), p); 
        
        //esc fecha apenas o pop-up (e impede processing de fechar a app)
        if (p.key == PApplet.ESC && showResetPopup) {
            p.key = 0;
            showResetPopup = false;
            draggingVolume = false;
        }
        
    }

    @Override
    public void mousePressed(PApplet p) {
    	
    	//quando o pop-up esta aberto, só ele recebe clicks
        if (showResetPopup) {
            if (confirmReset.isClicked(p)) {
                app.settings().lastScore = 0;
                showResetPopup = false;
                return;
            }
            if (cancelReset.isClicked(p)) {
                showResetPopup = false;
                return;
            }
            return; //clicar fora não faz nada
        }
    	
    	
    	//clicar no slider começa a arrastar (se não muted)
        if (!app.settings().muted &&
            p.mouseX >= sliderX && p.mouseX <= sliderX + sliderW &&
            p.mouseY >= sliderY - 12 && p.mouseY <= sliderY + sliderH + 12) {
            draggingVolume = true;
            return;
        }

        //clicar na checkbox toggle mute
        if (isOverRect(p.mouseX, p.mouseY, cbX, cbY, cbSize, cbSize)) {
            app.settings().muted = !app.settings().muted;
            app.sound().applyVolume(app.settings().volume, app.settings().muted);
            draggingVolume = false; //não fica preso
            return;
        }
        
        //reset score abre pop-up
        if (resetScore.isClicked(p)) {
            showResetPopup = true;
            draggingVolume = false;
            return;
        }

        //botão back
        if (back.isClicked(p)) app.setState(new MenuState(app), p);
    }
    
   
    public void mouseReleased(PApplet p) {
        draggingVolume = false;
    }

    @Override
    public void mouseDragged(PApplet p) { }

    @Override
    public void keyReleased(PApplet p) { }
}

