package game;

import processing.core.PApplet;
import setup.IProcessingApp;
import ui.AssetManager;
import audio.SoundManager;

/**
 * Classe principal da aplicação do jogo.
 *
 * O GameApp é responsável por:
 *  - inicializar recursos globais (assets, definições, som)
 *  - gerir o estado atual do jogo (menu, opções, jogo, game over, ...)
 *  - encaminhar inputs do utilizador (teclado e rato) para o estado ativo
 *  - controlar o ciclo principal de atualização e desenho (update + render)
 *
 * A aplicação segue o padrão State, onde cada ecrã do jogo é implementado
 * como um GameState independente. A transição de estados é feita
 * através de "#setState(GameState, PApplet)", chamando onExit
 * no estado anterior e onEnter no novo estado.
 *
 * O GameApp atua como ponto central de partilha de recursos comuns,
 * fornecendo acesso às Settings e ao SoundManager.
 */

public class GameApp implements IProcessingApp {

    private GameState state;
    
    private final Settings settings = new Settings();
    public Settings settings() {return settings;}
    
    private final SoundManager sound = new SoundManager();
    public SoundManager sound() {return sound;}

    @Override
    public void setup(PApplet p) {
        AssetManager.get().load(p); //carrega recursos globais uma vez
        
        p.getSurface().setSize(settings.width, settings.height); //aplica resolução inicial definido nas settings
        
        setState(new MenuState(this), p); //estado inicial do jogo
    }
    
   //transicao de estados: limpa estado anterior e inicializa o novo
    public void setState(GameState newState, PApplet p) {
        if (state != null) state.onExit(p);
        state = newState;
        if (state != null) state.onEnter(p);
    }

    @Override
    public void draw(PApplet p, float dt) {
        if (state == null) return;
        state.update(p, dt); //logica do estado
        state.display(p); //desenho do estado
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
