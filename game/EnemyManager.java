package game;

import aa.Alignment;
import aa.Cohesion;
import aa.Separation;
import particles.ProjectileManager;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import ui.AssetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de inimigos e ondas (waves) por nível.
 *
 * O EnemyManager é responsável por:
 *  - criar ondas de inimigos para cada nível (spawnWaveLevel1/2/3)
 *  - configurar comportamentos por grupo (wander, pursuit, flocking)
 *  - atualizar inimigos (aplicar behaviors) e gerir disparos inimigos
 *  - desenhar todos os inimigos ativos
 *
 * A seleção de inimigos especiais (pursuers/chasers) é feita por índices aleatórios,
 * permitindo variedade entre partidas sem alterar a estrutura das waves.
 */

public class EnemyManager {
    private final List<Enemy> enemies = new ArrayList<>(); //lista de inimigos ativos

    private float shootTimer = 0f; //timer do disparo inimigo
    private float shootInterval = 1.2f; //1 tiro a cada ~1.2s


    //método para poder criar o grupo de inimigos (wave) do nivel 1
    public void spawnWaveLevel1(PApplet p, int count) {
        enemies.clear(); //limpa inimigos anteriores

        float r = 26f; //raio base/colisao
        float topY = p.height * 0.12f; //zona superior de spawn

        String[] enemyKeys = {"enemy1", "enemy2", "enemy3"}; //existem 3 tipos de imigos que podem spawnar

        //adicionar inimigos
        for (int i = 0; i < count; i++) {
            float x = p.random(r, p.width - r);
            float y = topY + p.random(0, 80);

            String pick = enemyKeys[(int) p.random(enemyKeys.length)];  //escolher aletoriamente qual o tipo de inimigo que irá spawnar
            PImage sprite = AssetManager.get().img(pick);

            enemies.add(new EnemyLevel1(new PVector(x, y), r, sprite, p));
        }
    }


    //método para criar inimigos do nivel 2
    public void spawnWaveLevel2(PApplet p, int count, Player player) {
        enemies.clear(); //limpa inimigos anteriores

        float r = 26f;  //raio base/colisao
        float topY = p.height * 0.12f; //zona superior de spawn

        String[] enemyKeys = {"enemy1", "enemy2", "enemy3"}; //existem 3 tipos de imigos que podem spawnar

        //cria lista com o player para o sistema de visao (eye)
        List<Body> targetList = new ArrayList<>();
        targetList.add(player);

        //escolher os "pursuers":

        int pursuers = Math.min(4, count); //número fixo de inimigos a dar pursuit (menor valor entre 4 e o total de inimigos)

        ArrayList<Integer> idx = new ArrayList<>();   //lista que vai guardar os indices dos inimigos

        for (int j = 0; j < count; j++) {
            idx.add(j);
        }  //preenche a lista com os indices de todos os inimigos

        java.util.Collections.shuffle(idx);          //baralha a lista de indices de forma aleatoria

        //cria um conjunto (set) com os primeiros "pursuers" indices da lista baralhada
        java.util.HashSet<Integer> chaseSet = new java.util.HashSet<>(idx.subList(0, pursuers));

        for (int i = 0; i < count; i++) {
            float x = p.random(r, p.width - r);
            float y = topY + p.random(0, 80);

            String pick = enemyKeys[(int) p.random(enemyKeys.length)];  //escolher aletoriamente qual o tipo de inimigo que irá spawnar
            PImage sprite = AssetManager.get().img(pick);

            EnemyLevel2 enemyLevel2 = new EnemyLevel2(new PVector(x, y), r, sprite, p);

            //só os inimigos cujo indice esta no conjunto recebem o comportamento pursuit
            if (chaseSet.contains(i)) {
                enemyLevel2.setupTarget(player, targetList);
            }

            enemies.add(enemyLevel2);

        }

        //configurar só os pursuers (pursuit + separation)
        float sepDist = 85f;
        float sepWeight = 1.2f;

        for (int i = 0; i < enemies.size(); i++) {
            if (!chaseSet.contains(i)) continue;

            EnemyLevel2 e = (EnemyLevel2) enemies.get(i);
            e.addBehavior(new Separation(enemies, sepDist, sepWeight)); //evita pursuers colados
        }

    }


    //método para criar inimigos do nivel 3
    public void spawnWaveLevel3(PApplet p, int count, Player player) {
        enemies.clear();

        float r = 26f;
        float topY = p.height * 0.12f;

        String[] enemyKeys = {"enemy1", "enemy2", "enemy3"};

        //cria lista com o player para o sistema de visao (eye)
        List<Body> targetList = new ArrayList<>();
        targetList.add(player);

        //escolher pursuers
        int pursuers = Math.min(5, count);

        //escolher indices aleatórios para os pursuers
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            idx.add(i);
        }
        java.util.Collections.shuffle(idx);
        java.util.HashSet<Integer> chaseSet = new java.util.HashSet<>(idx.subList(0, pursuers));

        //criar inimigos
        for (int i = 0; i < count; i++) {
            float x = p.random(r, p.width - r);
            float y = topY + p.random(0, 140);

            String pick = enemyKeys[(int) p.random(enemyKeys.length)];
            PImage sprite = AssetManager.get().img(pick);

            EnemyLevel3 e = new EnemyLevel3(new PVector(x, y), r, sprite, p);

            //se for pursuer, ganha pursuit
            if (chaseSet.contains(i)) {
                e.makeChaser(player, targetList);
            }

            enemies.add(e);
        }

        //separa inimigos em dois grupos para behaviors diferentes
        List<Enemy> flock = new ArrayList<>();
        List<Enemy> chasers = new ArrayList<>();

        for (Enemy e : enemies) {
            if (e instanceof EnemyLevel3 && ((EnemyLevel3) e).isChaser()) {
                chasers.add(e);
            } else {
                flock.add(e);
            }
        }

        //flocking
        float neighborRadius = 120f;  //raio da vizinhança
        float alignW = 0.45f;
        float cohesionW = 0.35f;
        float sepDist = 55f;
        float sepW = 0.85f;

        for (Enemy e : flock) {
            e.addBehavior(new Alignment(flock, neighborRadius, alignW));
            e.addBehavior(new Cohesion(flock, neighborRadius, cohesionW));
            e.addBehavior(new Separation(flock, sepDist, sepW));
            //Wander já vem do initBehaviors() do EnemyLevel3
        }

        //pursuit + sep entre pursuers
        float sepChaserDist = 95f;
        float sepChaserW = 1.6f;

        for (Enemy e : chasers) {
            //separation entre pursuers para não irem colados
            e.addBehavior(new Separation(chasers, sepChaserDist, sepChaserW));
        }
    }

    public void update(PApplet p, float dt, ProjectileManager proj) {
        shootTimer -= dt;
        if (shootTimer <= 0f && !enemies.isEmpty()) {

            //escolhe um inimigo aleatorio para disparar
            Enemy shooter = enemies.get((int) p.random(enemies.size()));

            //origem do tiro (de baixo do sprite)
            PVector origin = new PVector(shooter.getPos().x, shooter.getPos().y + shooter.getRadius());
    
            float prob = p.random(1f);  //probabilidade de ataque especial
            
            //nivel 3: ataque especial (triplo azul)
            if (shooter instanceof EnemyLevel3 && prob < 0.4f) {
                int azul = p.color(0, 0, 255);

                //3 tiros em leque
                proj.spawnCustomEnemyShot(origin, new PVector(-100, 300), azul);
                proj.spawnCustomEnemyShot(origin, new PVector(0, 300), azul);
                proj.spawnCustomEnemyShot(origin, new PVector(100, 300), azul);
                
            //nivel 2: ataque especial (duplo roxo)
            } else if (shooter instanceof EnemyLevel2 && prob < 0.3f) {
                
                int roxo = p.color(180, 50, 255);
                float vel = 320f;

                //tiro 1 -> esquerda
                PVector vLeft = new PVector(-80, vel);
                proj.spawnCustomEnemyShot(origin, vLeft, roxo);

                //tiro 2 -> direita
                PVector vRight = new PVector(80, vel);
                proj.spawnCustomEnemyShot(origin, vRight, roxo);
            } else {
            	//ataque normal (tiro reto)
                int encarnado = p.color(255, 0, 0);
                PVector vDown = new PVector(0, 320f);

                proj.spawnCustomEnemyShot(origin, vDown, encarnado);
            }
            shootTimer = shootInterval;

        }
        for (Enemy e : enemies) {
            e.applyBehaviors(dt);
        }
    }

    public void display(PApplet p) {
        for (Enemy e : enemies) {
            e.display(p);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

//    public void spawnBoss(PApplet p) {
//        enemies.clear();
//        PVector pos = new PVector(p.width /2f, p.height * 0.15f); // em cima no meio
//        float radius = 60f;
//        PImage bossSprite = AssetManager.get().img("boss");
//
//        enemies.add(new BossLevel4(pos, radius, bossSprite, p));
//    }
}