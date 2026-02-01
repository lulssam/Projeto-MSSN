package game;

import physics.Galaxy;
import processing.core.PApplet;

/**
 * Implementação do Nível 3 do jogo.
 *
 * O Level3 utiliza o background physics.Galaxy,
 * representando uma galáxia em espiral com múltiplas camadas de partículas,
 * rotação global e efeitos visuais dinâmicos.
 *
 * Este nível funciona como o clímax visual do jogo, combinando
 * densidade gráfica e movimento contínuo.
 *
 * A música associada ao nível é definida através do método "music()".
 */

public class Level3 extends Level {
    private Galaxy bg; //background visual do nivel

    public Level3() {
        bg = new Galaxy();
    }

    @Override
    public void onEnter(PApplet p) {
        bg.onEnter(p); //inicializa recursos e estado do background
    }

    @Override
    public void update(PApplet p, float dt) {
        bg.update(p, dt); //atualiza animacao orbital
    }

    @Override
    public void display(PApplet p) {
        bg.display(p);
    }

    @Override
    public String music() {
    	return "/level3.wav";
    }
    
}