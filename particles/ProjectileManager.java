package particles;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe responsável pela gestão dos projéteis do jogo.
 * 
 * O ProjectileManager centraliza toda a lógica associada aos tiros,
 * nomeadamente:
 *  - Criação de novos projéteis do jogador e dos inimigos
 *  - Atualização do movimento e efeitos visuais dos projéteis
 *  - Remoção automática de projéteis que saem do ecrã
 *  - Desenho de todos os projéteis ativos
 * 
 * Esta classe atua como intermediário entre o gameplay
 * (Player/PlayState) e os objetos Projectile,
 * evitando que a lógica de gestão de listas fique espalhada
 * pelo código
 */

public class ProjectileManager {

    private final List<Projectile> playerShots = new ArrayList<>();
    private final List<Projectile> enemyShots  = new ArrayList<>();

    //player
    private float playerShotSpeed = 650f;
    private float playerShotRadius = 3f;
    private int   playerShotDamage = 1;

    //enemy
    private float enemyShotSpeed = 320f;
    private float enemyShotRadius = 4f;
    private int   enemyShotDamage = 1;

    public void spawnPlayerShot(PVector origin) {
        //dispara para cima
    	PVector vel = new PVector(0, -playerShotSpeed);
        playerShots.add(new Projectile(origin, vel, playerShotRadius, playerShotDamage));
    }
    
    public void spawnEnemyShot(PVector origin) {
    	//dispara para baixo
        PVector vel = new PVector(0, enemyShotSpeed);
        enemyShots.add(new Projectile(origin, vel, enemyShotRadius, enemyShotDamage));
    }

    public void update(PApplet p, float dt) {
    	//player shots
        for (int i = playerShots.size() - 1; i >= 0; i--) {
            Projectile pr = playerShots.get(i);
            pr.update(dt);
            if (pr.isOffscreen(p)) { playerShots.remove(i);} //remover particula quando está fora do ecrã
        }

        //enemy shots
        for (int i = enemyShots.size() - 1; i >= 0; i--) {
            Projectile pr = enemyShots.get(i);
            pr.update(dt);
            if (pr.isOffscreen(p)) { enemyShots.remove(i);} //remover particula quando está fora do ecrã
        }
    }

    public void display(PApplet p) {
    	for (Projectile pr : playerShots) { pr.display(p);}
        for (Projectile pr : enemyShots) {  pr.display(p);}
    }

    public List<Projectile> getPlayerShots() {
        return playerShots;
    }
    
    public List<Projectile> getEnemyShots()  { 
    	return enemyShots; 
    }

    public void clear() {
        playerShots.clear();
        enemyShots.clear();
    }
}
