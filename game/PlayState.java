package game;

import particles.ProjectileManager;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import ui.AssetManager;

/**
 * Estado principal de gameplay.
 *
 * O code PlayState coordena a execução do jogo durante uma sessão, sendo responsável por:
 *  - gerir o jogador (movimento, disparo, feedback visual de dano)
 *  - gerir inimigos e ondas por nível (spawn, update, draw)
 *  - gerir projéteis (player e inimigos) e respetivos efeitos visuais
 *  - executar deteção de colisões e aplicar consequências (dano, mortes, score)
 *  - controlar progressão entre níveis e respetivas transições (intro overlay, música)
 *  - tratar estados terminais: game over e créditos (com fade out)
 *
 * A lógica de cada nível é abstraída por Level, permitindo trocar o background
 * e parâmetros de onda sem duplicar o ciclo principal de gameplay.
 */

public class PlayState implements GameState {

    private final GameApp app;
    private Player player;
    private Level level;
    private int currentLevel;

    private EnemyManager enemies;

    private int lives = 3;  //vidas do player
    private int score = 0; //pontuacao atual

    private PImage heartFull;
    private PImage heartEmpty;

    //projeteis
    private ProjectileManager projectiles;
    private float shootCooldown = 0.18f;  //cadencia de tiro do jogador
    private float shootTimer = 0f; //timer do cooldown
    private boolean shootHeld = false;

    private float hitCooldown = 0.75f; //invencibilidade curta apos levar hit
    private float hitTimer = 0f;

    //level intro
    private float levelIntroTimer = 0f;
    private float levelIntroDuration = 3.5f; //duracao do overlay do nivel
    private String levelIntroText = "LEVEL 1";

    // fade out
    private boolean isFading;
    private float fadeAlpha;
    
    public PlayState(GameApp app) {
        this.app = app;

        isFading = false;
        fadeAlpha = 0;
    }

    @Override
    public void onEnter(PApplet p) {

        //corações das vidas
        heartFull = AssetManager.get().img("heart_full");
        heartEmpty = AssetManager.get().img("heart_empty");

        lives = 3; //vidas
        score = 0;

        //nivel inicial (nivel 1)
        currentLevel = 1;
        level = new Level1();
        level.onEnter(p);

        //musica do nivel atual
        app.sound().playMusic(level.music(), app.settings().volume, app.settings().muted);

        //player no fundo do ecrã
        PImage playerSprite = AssetManager.get().img("player");
        player = new Player(new PVector(p.width / 2f, p.height * 0.85f), 32, playerSprite);

        projectiles = new ProjectileManager(); //inicializa os projeteis

        //spawns iniciais
        enemies = new EnemyManager();
        enemies.spawnWaveLevel1(p, 15);

        //overlay de introducao do nivel
        levelIntroText = "LEVEL 1";
        levelIntroTimer = levelIntroDuration;

        shootTimer = 0f;
        hitTimer = 0f;
        shootHeld = false;
    }

    @Override
    public void onExit(PApplet p) {
    }

    @Override
    public void update(PApplet p, float dt) {
    	
    	//timer do overlay do nivel (o jogo arranca depois de uma pequena pausa)
        if (levelIntroTimer > 0f) {
            levelIntroTimer -= dt;
        }

        level.update(p, dt); // background continua em animação
        player.update(dt, p); //player pode mexer durante o intro

        if (levelIntroTimer > 1.5f) { return;} //bloqueia combate durante a fase inicial do overlay

        //cooldown de dano para evitar hits seguidos
        if (hitTimer > 0f) {
            hitTimer -= dt;
        }

        //disparo continuo enquanto space estiver pressionado
        shootTimer -= dt;
        if (shootHeld && shootTimer <= 0f) {
            //spawn na ponta da nave
            projectiles.spawnPlayerShot(p, player.gunMuzzle());
            shootTimer = shootCooldown;
        }
        
        //atualiza projeteis e inimigos
        projectiles.update(p, dt);
        enemies.update(p, dt, projectiles); //inimigos podem disparar

        //colisão: tiros do player vs inimigos
        int kills = CollisionSystem.shotsVsEnemies(projectiles.getPlayerShots(), enemies.getEnemies());
        if (kills > 0) {
            addScore(kills * 10);
            app.settings().lastScore = score;  //guarda valor para menu e options
        }

        //colisão: tiros dos inimigos vs player
        if (hitTimer <= 0f && CollisionSystem.enemyShotsVsPlayer(projectiles.getEnemyShots(), player)) {
            lives--;
            player.flashDamage(); //ativa a animação de damage
            hitTimer = hitCooldown;

            //se ficar sem vidas passa para GameOver e guarda-se o score
            if (lives <= 0) {
                app.settings().lastScore = score;
                app.setState(new GameOverState(app, score), p);
                return;
            }
        }
        

        //progressao de niveis quando a wave termina (quando os inimigos estão todos mortos)
        if (enemies.getEnemies().isEmpty()) {
            currentLevel++; //aumentar nivel

            switch (currentLevel) {
                //transicao para nivel 2
                case 2:
                    level = new Level2();
                    level.onEnter(p);

                    levelIntroText = "LEVEL 2";
                    levelIntroTimer = levelIntroDuration;

                    //musica
                    app.sound().playMusic(level.music(), app.settings().volume, app.settings().muted);

                    enemies.spawnWaveLevel2(p, 20, player);
                    projectiles.clear(); //limpa tiros antigos entre niveis
                    break;
                    
                //transicao para nivel 3
                case 3:
                    level = new Level3();
                    level.onEnter(p);

                    levelIntroText = "LEVEL 3";
                    levelIntroTimer = levelIntroDuration;

                    //musica
                    app.sound().playMusic(level.music(), app.settings().volume, app.settings().muted);

                    enemies.spawnWaveLevel3(p, 25, player);
                    projectiles.clear();
                    break;
                    
                //fim do jogo: inicia fade e vai para creditos    
                case 4:
                    isFading = true;
            }
        }
        
        //fade out antes de ir para os creditos
        if (isFading) {
            fadeAlpha += 150 * dt;
            if (fadeAlpha >= 255) {
                fadeAlpha = 255;
                app.settings().lastScore = score;
                app.setState(new Credits(app, score), p);
            }
        }
    }

    @Override
    public void display(PApplet p) {
    	
    	//ordem de desenho: background -> inimigos -> player -> projeteis -> ui
        level.display(p);
        enemies.display(p);
        player.display(p);
        projectiles.display(p);

        //instruções
        p.noStroke();
        p.fill(80);
        p.textAlign(PApplet.CENTER, PApplet.CENTER);
        p.textSize(14);
        p.text("LEFT/RIGHT ARROWS to move | SPACE to shoot | M to menu", p.width / 2f, 10);

        drawLevelIntro(p); //overlay do nivel
        drawHUD(p); //corações

        if (isFading) {
            p.pushStyle();
            p.noStroke();
            p.fill(0, fadeAlpha);
            p.rect(0, 0, p.width, p.height);
            p.popStyle();
        }
    }

    //metodo para desenhar o overlay inicial dos niveis
    private void drawLevelIntro(PApplet p) {
        if (levelIntroTimer <= 0f) return;

        //tempo t: 0..1 (1 no inicio, 0 no fim)
        float t = PApplet.constrain(levelIntroTimer / levelIntroDuration, 0f, 1f);

        float fade = (float) Math.pow(t, 2.2);  //fade nao linear para ficar forte no inicio e desaparecer suave no fim
        float a = 255f * fade;

        p.pushStyle();

        //overlay escuro atras do texto
        p.noStroke();
        p.fill(0, 140 * fade);
        p.rect(0, 0, p.width, p.height);

        //font do texto
        if (AssetManager.get().font("ui") != null) {
            p.textFont(AssetManager.get().font("ui"));
        }

        p.textAlign(PApplet.CENTER, PApplet.CENTER);

        //glow verde
        p.fill(0, 255, 0, a * 0.35f);
        p.textSize(82);
        p.text(levelIntroText, p.width / 2f, p.height / 2f);

        //texto principal
        p.fill(200, 255, 200, a);
        p.textSize(72);
        p.text(levelIntroText, p.width / 2f, p.height / 2f);

        p.popStyle();
    }

    private void drawHUD(PApplet p) {
        p.imageMode(PApplet.CORNER);
        p.pushStyle();

        //vidas (canto superior esquerdo)
        float pad = 14;
        float size = 26;   //tamanho do coração
        float gap = 8;     //distancia entre corações

        p.imageMode(PApplet.CORNER);

        for (int i = 0; i < 3; i++) {
            PImage img = (i < lives) ? heartFull : heartEmpty;
            if (img != null) {
                p.image(img, pad + i * (size + gap), pad, size, size);
            }
        }

        //score (canto superior direito)
        p.fill(200, 255, 200);
        if (AssetManager.get().font("ui") != null) p.textFont(AssetManager.get().font("ui"));
        p.textAlign(PApplet.RIGHT, PApplet.TOP);
        p.textSize(18);
        p.text("SCORE: " + score, p.width - pad, pad + 2);

        p.popStyle();
    }


    //método para ganhar pontuação após matar inimigos
    private void addScore(int pts) {
        score += pts;
    }

    @Override
    public void keyPressed(PApplet p) {
        //movimento do player
        if (p.keyCode == PApplet.LEFT) {
            player.setLeft(true);
        }
        if (p.keyCode == PApplet.RIGHT) {
            player.setRight(true);
        }

        //disparar
        if (p.key == ' ') {
            shootHeld = true;
        }

        //shortcut para voltar ao menu
        if (p.key == 'm' || p.key == 'M') {
            app.settings().lastScore = score;
            app.setState(new MenuState(app, score), p);
        }
    }

    @Override
    public void keyReleased(PApplet p) {
        if (p.keyCode == PApplet.LEFT) player.setLeft(false);
        if (p.keyCode == PApplet.RIGHT) player.setRight(false);

        if (p.key == ' ') shootHeld = false;
    }

    @Override
    public void mousePressed(PApplet p) {}

    @Override
    public void mouseReleased(PApplet p) {}

    @Override
    public void mouseDragged(PApplet p) {}
}