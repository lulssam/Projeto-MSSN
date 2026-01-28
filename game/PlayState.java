package game;

import particles.ProjectileManager;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import ui.AssetManager;

/**
 * Estado de jogo responsável pela execução principal do gameplay.
 * <p>
 * Este estado é independente do nível concreto, utilizando a classe
 * abstrata Level para permitir a progressão entre vários níveis
 * sem alterar a lógica principal do jogo.
 * <p>
 * O PlayState recebe input do utilizador (teclado) e coordena
 * a atualização e desenho de todas as entidades ativas em jogo
 */

public class PlayState implements GameState {

    private final GameApp app;
    private Player player;
    private Level level;
    private int currentLevel;

    private EnemyManager enemies;

    private int lives = 3;  //vidas do player
    private int score = 0;

    private PImage heartFull;
    private PImage heartEmpty;

    //projeteis
    private ProjectileManager projectiles;
    private float shootCooldown = 0.18f; //segundos
    private float shootTimer = 0f;
    private boolean shootHeld = false;

    private float hitCooldown = 0.75f;
    private float hitTimer = 0f;

    //level intro
    private float levelIntroTimer = 0f;
    private float levelIntroDuration = 3.5f; //segundos
    private String levelIntroText = "LEVEL 1";

    public PlayState(GameApp app) {
        this.app = app;
    }

    @Override
    public void onEnter(PApplet p) {

        //corações das vidas
        heartFull = AssetManager.get().img("heart_full");
        heartEmpty = AssetManager.get().img("heart_empty");

        lives = 3; //vidas
        score = 0;

        //o play passa sempre para o nivel 1
        currentLevel = 1;
        level = new Level1();
        level.onEnter(p);

        //musica
        app.sound().playMusic(level.music(), app.settings().volume, app.settings().muted);

        //inicializa o player
        PImage playerSprite = AssetManager.get().img("player");
        player = new Player(new PVector(p.width / 2f, p.height * 0.85f), 32, playerSprite);

        projectiles = new ProjectileManager(); //inicializa os projeteis

        //inimigos
        enemies = new EnemyManager();
        int count = (int) p.random(5, 11);
        enemies.spawnWave(p, count);

        //overlay
        levelIntroText = "LEVEL 1"; //mudar para: "LEVEL " + levelNumber quando houver mais niveis
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
        if (levelIntroTimer > 0f) {
            levelIntroTimer -= dt;
        } //timer do overlay

        level.update(p, dt); // background continua a mexer
        player.update(dt, p); // jogador pode mexer se para preparar

        if (levelIntroTimer > 1.5f)  return;

        //cooldown de levar hit
        if (hitTimer > 0f) {
            hitTimer -= dt;
        }

        //tiros do player
        shootTimer -= dt;
        if (shootHeld && shootTimer <= 0f) {
            //spawn na ponta da nave
            projectiles.spawnPlayerShot(player.gunMuzzle());
            shootTimer = shootCooldown;
        }

        projectiles.update(p, dt); //update projeteis (player + enemy)

        //debug
        //if (shootHeld) System.out.println("shootHeld true");


        enemies.update(p, dt, projectiles); //update inimigos (faz os inimigos dispararem)

        //colisão: tiros do player vs inimigos
        int kills = CollisionSystem.shotsVsEnemies(projectiles.getPlayerShots(), enemies.getEnemies());

        if (kills > 0) {
            addScore(kills * 10);
            app.settings().lastScore = score;  //guarda valor para menu e options
        }

        //colisão: tiros dos inimigos vs player
        if (hitTimer <= 0f && CollisionSystem.enemyShotsVsPlayer(projectiles.getEnemyShots(), player)) {

            lives--;
            hitTimer = hitCooldown;

            //se ficar sem vidas passa para GameOver e guarda-se o score
            if (lives <= 0) {
                app.settings().lastScore = score;
                app.setState(new GameOverState(app, score), p);
                return;
            }
        }

        // quando inimigos forem todos mortos
        if (enemies.getEnemies().isEmpty()) {
            currentLevel ++; // aumentar nivel

            switch (currentLevel) {
                case 2:
                    level = new Level2();
                    level.onEnter(p);

                    levelIntroText = "LEVEL 2";
                    levelIntroTimer = levelIntroDuration;

                    //musica
                    app.sound().playMusic(level.music(), app.settings().volume, app.settings().muted);

                    enemies.spawnWave(p, 12);
                    break;
                case 3:
                    // TODO
                    break;
            }
        }

    }

    @Override
    public void display(PApplet p) {
        level.display(p);

        enemies.display(p);

        //debug linha
        //p.stroke(0, 255, 0, 60);
        //p.line(0, p.height * 0.85f + 32, p.width, p.height * 0.85f + 32);

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
    }

    //metodo para desenhar o overlay inicial dos niveis
    private void drawLevelIntro(PApplet p) {
        if (levelIntroTimer <= 0f) return;

        //tempo t: 0..1 (1 no inicio, 0 no fim)
        float t = PApplet.constrain(levelIntroTimer / levelIntroDuration, 0f, 1f);

        //forte no inicio e depois fade no fim
        //curva
        float fade = (float) Math.pow(t, 2.2);  //quanto maior, mais tempo forte e fade mais rapido no fim
        float a = 255f * fade;

        p.pushStyle();

        //overlay escuro atras do texto
        p.noStroke();
        p.fill(0, 140 * fade);
        p.rect(0, 0, p.width, p.height);

        //font do texto
        if (ui.AssetManager.get().font("ui") != null) {
            p.textFont(ui.AssetManager.get().font("ui"));
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
    public void mousePressed(PApplet p) {
    }

    @Override
    public void mouseReleased(PApplet p) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(PApplet p) {
        // TODO Auto-generated method stub

    }
}
