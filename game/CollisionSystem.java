package game;

import java.util.List;

import particles.Projectile;
import processing.core.PVector;

public class CollisionSystem {

    //circulo vs circulo
    public static boolean circles(PVector a, float ra, PVector b, float rb) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        float r = ra + rb;
        return (dx * dx + dy * dy) <= (r * r);
    }

    //playerShots vs enemies
    //retorna quantos inimigos morreram (para score)
    public static int shotsVsEnemies(List<Projectile> shots, List<Enemy> enemies) {
        int kills = 0;

        for (int si = shots.size() - 1; si >= 0; si--) {
            Projectile s = shots.get(si);

            boolean hit = false;
            for (int ei = enemies.size() - 1; ei >= 0; ei--) {
                Enemy e = enemies.get(ei);

                if (circles(s.getPos(), s.getRadius(), e.getPos(), e.getRadius())) {
                    //remove ambos
                    enemies.remove(ei);
                    hit = true;
                    kills++;
                    break;
                }
            }

            if (hit) {shots.remove(si);}
        }

        return kills;
    }
    
    //enemyShots vs player
    public static boolean enemyShotsVsPlayer(List<particles.Projectile> shots, Player player) {
        for (int i = shots.size() - 1; i >= 0; i--) {
            particles.Projectile s = shots.get(i);
            if (circles(s.getPos(), s.getRadius(), player.getPos(), player.getRadius())) {
                shots.remove(i);
                return true;
            }
        }
        return false;
    }

}
