package particles;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela gestão dos projéteis do jogo.
 *
 * O ProjectileManager centraliza a criação, atualização, remoção e desenho
 * dos projéteis ativos, separando tiros do jogador e tiros de inimigos.
 *
 * Responsabilidades principais:
 *  - criar projéteis (jogador e inimigos) com parâmetros definidos (velocidade, dano, raio, cor)
 *  - atualizar movimento e efeitos visuais dos projéteis
 *  - remover automaticamente projéteis que saem do ecrã
 *  - desenhar todos os projéteis ativos
 *
 * Esta classe reduz a duplicação de lógica e evita que a gestão de listas fique espalhada
 * pelo gameplay (ex: Player/PlayState).
 *
 * Colisões e aplicação de dano são tratadas externamente (CollisionSystem).
 */

public class ProjectileManager {

    private final List<Projectile> playerShots = new ArrayList<>();
    private final List<Projectile> enemyShots = new ArrayList<>();

    //player
    private float playerShotSpeed = 650f;
    private float playerShotRadius = 3f;
    private int playerShotDamage = 1;

    //enemy
    private float enemyShotSpeed = 320f;
    private float enemyShotRadius = 4f;
    private int enemyShotDamage = 1;
    
    //tiro simples com parametros para gameplay
    public void spawnPlayerShot(PApplet p, PVector origin) {
        //dispara para cima
        int color = p.color(255, 165, 0);
        PVector vel = new PVector(0, -playerShotSpeed);
        playerShots.add(new Projectile(origin, vel, playerShotRadius, playerShotDamage, color));

    }

    public void spawnEnemyShot(PApplet p, PVector origin) {
        //dispara para baixo
        int color = p.color(255, 0, 0);
        PVector vel = new PVector(0, enemyShotSpeed);
        enemyShots.add(new Projectile(origin, vel, enemyShotRadius, enemyShotDamage, color));
    }
      
    public void update(PApplet p, float dt) {
    	
    	//iteracao inversa para remover projeteis em seguranca
        //player shots
        for (int i = playerShots.size() - 1; i >= 0; i--) {
            Projectile pr = playerShots.get(i);
            pr.update(dt);
            if (pr.isOffscreen(p)) {
                playerShots.remove(i);
            } //remover particula quando está fora do ecrã
        }
        
        //iteracao inversa para remover projeteis em seguranca
        //enemy shots
        for (int i = enemyShots.size() - 1; i >= 0; i--) {
            Projectile pr = enemyShots.get(i);
            pr.update(dt);
            if (pr.isOffscreen(p)) {
                enemyShots.remove(i);
            } //remover particula quando está fora do ecrã
        }     
    }
    
 
    public void display(PApplet p) {
    	
    	//player shots
        for (Projectile pr : playerShots) {
            pr.display(p);
        }
        
        //enemy shots
        for (Projectile pr : enemyShots) {
            pr.display(p);
        }     
    }

    public List<Projectile> getPlayerShots() {
        return playerShots;
    }

    public List<Projectile> getEnemyShots() {
        return enemyShots;
    }
       
    public void clear() {
        playerShots.clear();
        enemyShots.clear(); 
    }


    //método para escolher direção, velocidade e cor do projetil dos inimigos
    public void spawnCustomEnemyShot(PVector origin, PVector velocity, int color) {
        enemyShots.add(new Projectile(origin, velocity, enemyShotRadius, enemyShotDamage, color));
    }
}