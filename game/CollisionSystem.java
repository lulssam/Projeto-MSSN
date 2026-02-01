package game;

import java.util.List;
import particles.Projectile;
import processing.core.PVector;

/**
 * Sistema de colisões do jogo.
 *
 * O CollisionSystem agrupa funções estáticas para detetar e resolver
 * colisões simples entre entidades, usando aproximação de círculo (circle vs circle).
 *
 * Responsabilidades:
 *  - detetar colisão entre dois círculos (circles)
 *  - gerir impacto de tiros do jogador em inimigos (aplicar dano, remover inimigos, contar kills)
 *  - detetar impacto de tiros inimigos no jogador (remover tiro e sinalizar hit)
 *
 * Esta classe não desenha nada e não mantém estado interno.
 */


public class CollisionSystem {

	//detecao de colisão simples: circulo vs circulo
    public static boolean circles(PVector a, float ra, PVector b, float rb) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float r = ra + rb;
        return (dx * dx + dy * dy) <= (r * r);
    }

    //tiros do player vs inimigos
    //retorna quantos inimigos morreram (para score)
    //percorre de tras para a frente para remover com seguranca
    public static int shotsVsEnemies(List<Projectile> shots, List<Enemy> enemies) {
        int kills = 0;
        int damagePerShot = 1; //dano base por tiro (hp varia por nivel)

        for (int si = shots.size() - 1; si >= 0; si--) {
            Projectile s = shots.get(si);

            boolean hit = false;
            
            for (int ei = enemies.size() - 1; ei >= 0; ei--) {
                Enemy e = enemies.get(ei);

                if (circles(s.getPos(), s.getRadius(), e.getPos(), e.getRadius())) {
                	e.takeDamage(damagePerShot); //leva dano
                	
                	//se o inimigo morrer, remove e conta para score
                    if (e.isDead()) {
                        enemies.remove(ei);
                        kills++;
                    }
                    
                    hit = true; //tiro desaparece quando acerta
                    break;            
                }
            }
 
            if (hit) { shots.remove(si);} //se acertar no jogador, remove o tiro e devolve true
        }

        return kills;
    }
    
    //tiros inimigos vs player
    public static boolean enemyShotsVsPlayer(List<Projectile> shots, Player player) {
        for (int i = shots.size() - 1; i >= 0; i--) {
        	
        	Projectile s = shots.get(i);
            
        	//se acertar no jogador, remove o tiro e sinaliza hit
            if (circles(s.getPos(), s.getRadius(), player.getPos(), player.getRadius())) {
                shots.remove(i);    
                return true;
            }
        }
        return false;
    }
  
}
