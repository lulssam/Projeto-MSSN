package ui;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

/**
 * Classe responsável pela gestão e acesso centralizado aos recursos do jogo.
 *
 * O AssetManager carrega e armazena:
 *  - imagens (sprites, fundos, elementos de UI)
 *  - fontes utilizadas na interface
 *
 * Esta classe segue o padrão Singleton, garantindo que os recursos
 * são carregados apenas uma vez durante a execução do jogo
 * e reutilizados em todos os estados.
 *
 * O acesso aos assets é feito através de chaves (String),
 * evitando múltiplos carregamentos e facilitando a organização do código.
 *
 * Nota: o método "load(PApplet)" deve ser chamado uma única vez
 * durante a inicialização da aplicação.
 */

public class AssetManager {
	
	//instancia unica (singleton)
    private static final AssetManager INSTANCE = new AssetManager();
    public static AssetManager get() { return INSTANCE; }
    
    //maps de assets carregados, indexados por chave
    private final Map<String, PImage> images = new HashMap<>();
    private final Map<String, PFont> fonts = new HashMap<>();

    private AssetManager() { }

    public void load(PApplet p) {
        //imagens na pasta data
    	//imagens de interface e background
        images.put("title", p.loadImage("title.png"));
        images.put("stars", p.loadImage("stars.png"));
        
        images.put("player", p.loadImage("player.png"));  //player
        
        //inimigos
        images.put("enemy1", p.loadImage("ufo1.png"));
        images.put("enemy2", p.loadImage("ufo2.png"));
        images.put("enemy3", p.loadImage("ufo3.png"));
        
        //nivel terra/lua
        images.put("earth", p.loadImage("earth.png"));
        images.put("moon", p.loadImage("moon.png"));
        
        //ui vidas
        images.put("heart_full", p.loadImage("heartFilled.png"));
        images.put("heart_empty", p.loadImage("heartEmpty.png"));

        //planetas (nivel sistema solar)
        images.put("sun", p.loadImage("sun.png"));
        images.put("mercury", p.loadImage("mercury.png"));
        images.put("venus", p.loadImage("venus.png"));
        images.put("mars", p.loadImage("mars.png"));
        images.put("jupiter", p.loadImage("jupiter.png"));
        images.put("saturn", p.loadImage("saturn.png"));
        images.put("uranus", p.loadImage("uranus.png"));
        images.put("neptune", p.loadImage("neptune.png"));

        //asteroides
        images.put("asteroid", p.loadImage("asteroid.png"));
        
        //fonte principal da interface
        fonts.put("ui", p.createFont("space_invaders.ttf", 24, true));
    }
    
    //obter imagem pelo identificador
    public PImage img(String key) {
        return images.get(key);
    }
    
    //obter fonte pelo identificador
    public PFont font(String key) {
        return fonts.get(key);
    }
}
