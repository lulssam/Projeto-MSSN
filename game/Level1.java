package game;

import physics.EarthMoon;
import processing.core.PApplet;

/**
 * Implementação do Nível 1 do jogo.
 *
 * O Level1 utiliza o background physics.EarthMoon,
 * representando a Terra e a Lua com animação orbital.
 *
 * Este nível:
 *  - não contém inimigos
 *  - serve como introdução visual/ambiental ao jogo
 *
 * A música associada é definida através do método "music()".
 */

public class Level1 extends Level {
	private EarthMoon bg; //background visual do nivel

    public Level1() {
        bg = new EarthMoon();
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
        return "/level1.wav";
    }
      
}
