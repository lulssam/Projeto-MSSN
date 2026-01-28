package ui;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

/**
 * Classe responsável pela gestão e acesso aos recursos do jogo
 * 
 * O AssetManager carrega e armazena:
 *  - Imagens (sprites, fundos, UI)
 *  - Fontes utilizadas na interface
 * 
 * Esta classe segue o padrão Singleton, garantindo que os recursos
 * são carregados apenas uma vez e reutilizados em todo o jogo.
 * 
 * O acesso aos assets é feito através de chaves (Strings),
 * evitando múltiplos loads e facilitando a organização do código
 */

public class AssetManager {

    private static final AssetManager INSTANCE = new AssetManager();
    public static AssetManager get() { return INSTANCE; }

    private final Map<String, PImage> images = new HashMap<>();
    
    private final Map<String, PFont> fonts = new HashMap<>();

    private AssetManager() { }

    public void load(PApplet p) {
        //imagens na pasta data
        images.put("title", p.loadImage("title.png"));
        images.put("stars", p.loadImage("stars.png"));
        
        images.put("player", p.loadImage("player.png"));
        
        images.put("enemy1", p.loadImage("ufo1.png"));
        images.put("enemy2", p.loadImage("ufo2.png"));
        images.put("enemy3", p.loadImage("ufo3.png"));
        
        images.put("earth", p.loadImage("earth.png"));
        images.put("moon", p.loadImage("moon.png"));
        
        images.put("heart_full", p.loadImage("heartFilled.png"));
        images.put("heart_empty", p.loadImage("heartEmpty.png"));

        // planetas
        images.put("sun", p.loadImage("sun.png"));
        images.put("mercury", p.loadImage("mercury.png"));
        images.put("venus", p.loadImage("venus.png"));
        images.put("mars", p.loadImage("mars.png"));
        images.put("jupiter", p.loadImage("jupiter.png"));
        images.put("saturn", p.loadImage("saturn.png"));
        images.put("uranus", p.loadImage("uranus.png"));
        images.put("neptune", p.loadImage("neptune.png"));

        // asteroide
        images.put("asteroid", p.loadImage("asteroid.png"));

        fonts.put("ui", p.createFont("space_invaders.ttf", 24, true));
    }

    public PImage img(String key) {
        return images.get(key);
    }
    
    public PFont font(String key) {
        return fonts.get(key);
    }
}
