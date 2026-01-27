package game;

import processing.core.PApplet;
import setup.IProcessingApp;
import ui.AssetManager;
import audio.SoundManager;

/**
 * Classe principal da aplicação do jogo.
 * 
 * O GameApp é responsável por:
 *  - Inicializar recursos globais (assets, som, definições)
 *  - Gerir o estado atual do jogo (menu, opções, jogo, game over, etc...)
 *  - Encaminhar inputs do utilizador (teclado e rato) para o estado ativo
 *  - Controlar o ciclo principal de atualização e desenho
 * 
 * Esta classe segue o padrão State, permitindo que cada ecrã do jogo
 * seja implementado como um GameState independente.
 * 
 * O GameApp atua como ponto central de comunicação entre estados,
 * fornecendo acesso partilhado às Settings e ao SoundManager.
 */

public class GameApp implements IProcessingApp {

    private GameState state;
    
    private final Settings settings = new Settings();
    public Settings settings() {return settings;}
    
    private final SoundManager sound = new SoundManager();
    public SoundManager sound() {return sound;}

    @Override
    public void setup(PApplet p) {
        AssetManager.get().load(p); //imagens/fonts
        
        p.getSurface().setSize(settings.width, settings.height); //aplica tamanho inicial definido nas settings
        
        setState(new MenuState(this), p);
    }

    public void setState(GameState newState, PApplet p) {
        if (state != null) state.onExit(p);
        state = newState;
        if (state != null) state.onEnter(p);
    }

    @Override
    public void draw(PApplet p, float dt) {
        if (state == null) return;
        state.update(p, dt);
        state.display(p);
    }

    @Override
    public void keyPressed(PApplet p) {
        if (state != null) state.keyPressed(p);
    }
    
    @Override
    public void keyReleased(PApplet p) {
        if (state != null) state.keyReleased(p);
    }

    @Override
    public void mousePressed(PApplet p) {
        if (state != null) state.mousePressed(p);
    }

    @Override
    public void mouseReleased(PApplet p) {
    	if (state != null) state.mouseReleased(p);
    }

    @Override
    public void mouseDragged(PApplet p) {
    	if (state != null) state.mouseDragged(p);
    }
}
