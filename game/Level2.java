package game;

import physics.SolarSystem;
import processing.core.PApplet;

/**
 * Implementação do Nível 2 do jogo.
 *
 * O Level2 utiliza o background physics.SolarSystem,
 * representando o sistema solar com planetas, asteroides e órbitas
 * simuladas através de forças gravitacionais simplificadas.
 *
 * Este nível é maioritariamente visual, funcionando como
 * ambientação/interlúdio entre níveis de gameplay mais ativos.
 *
 * A música de fundo é definida através do método "music()".
 */

public class Level2 extends Level {
    private SolarSystem bg; //background visual do nivel

    public Level2() {
        bg = new SolarSystem();
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
        return "/level2.wav";
    }
    
}
