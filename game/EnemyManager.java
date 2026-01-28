package game;

import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {
    private final List<Enemy> enemies = new ArrayList<>(); //lista de inimigos

    private float shootTimer = 0f;
    private float shootInterval = 1.2f; //1 tiro a cada ~1.2s

    /**
     * método para poder criar o grupo de inimigos (wave) do nivel 1
     *
     * @param p     Instancia PApplet
     * @param count quantidade de inimigos
     */
    public void spawnWaveLevel1(PApplet p, int count) {
        enemies.clear();  //eliminar anteriores caso haja problemas

        float r = 26f;   //raio de colisão
        float topY = p.height * 0.12f;

        String[] enemyKeys = {"enemy1", "enemy2", "enemy3"}; //existem 3 tipos de imigos que podem spawnar

        //adicionar inimigos
        for (int i = 0; i < count; i++) {
            float x = p.random(r, p.width - r);
            float y = topY + p.random(0, 80);

            String pick = enemyKeys[(int) p.random(enemyKeys.length)];  //escolher aletoriamente qual o tipo de inimigo que irá spawnar
            PImage sprite = ui.AssetManager.get().img(pick);

            enemies.add(new EnemyLevel1(new PVector(x, y), r, sprite, p));
        }
    }

    /**
     * Método para criar inimigos do nivel 2
     *
     * @param p      Instancia PApplet
     * @param count  quantidade de inimigos
     * @param player Instancia player para associar o eye
     */
    public void spawnWaveLevel2(PApplet p, int count, Player player) {
        enemies.clear();  //eliminar anteriores caso haja problemas

        float r = 26f;   //raio de colisão
        float topY = p.height * 0.12f;

        String[] enemyKeys = {"enemy1", "enemy2", "enemy3"}; //existem 3 tipos de imigos que podem spawnar

        // lista com o jogador com alvo para o eye
        List<Body> targetList = new ArrayList<>();
        targetList.add(player);

        for (int i = 0; i < count; i++) {
            float x = p.random(r, p.width - r);
            float y = topY + p.random(0, 80);

            String pick = enemyKeys[(int) p.random(enemyKeys.length)];  //escolher aletoriamente qual o tipo de inimigo que irá spawnar
            PImage sprite = ui.AssetManager.get().img(pick);

            EnemyLevel2 enemyLevel2 = new EnemyLevel2(new PVector(x, y), r, sprite, p);
            enemyLevel2.setupTarget(player, targetList);

            enemies.add(enemyLevel2);
        }

    }

    public void update(PApplet p, float dt) {
        //sem movimento (debug)
    }

    public void update(PApplet p, float dt, particles.ProjectileManager proj) {
        shootTimer -= dt;
        if (shootTimer <= 0f && !enemies.isEmpty()) {

            //escolhe um inimigo random para disparar
            Enemy shooter = enemies.get((int) p.random(enemies.size()));

            //origem do tiro (de baixo do sprite)
            PVector origin = new PVector(shooter.getPos().x, shooter.getPos().y + shooter.getRadius());
            proj.spawnEnemyShot(origin);

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
}
