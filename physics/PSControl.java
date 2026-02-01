package physics;

import processing.core.PVector;

/**
 * Classe responsável por controlar os parâmetros de emissão de um sistema de partículas.
 *
 * Um PSControl define intervalos e regras para gerar valores aleatórios usados
 * na criação de partículas, nomeadamente:
 *  - direção média e dispersão angular do vetor velocidade
 *  - velocidades mínima e máxima
 *  - raio mínimo e máximo
 *  - tempo de vida mínimo e máximo
 *  - fluxo de emissão (partículas/segundo)
 *  - cor base das partículas
 *
 * Esta classe não gere partículas diretamente: apenas fornece amostras aleatórias
 * para serem usadas por um  ParticleSystem.
 */

public class PSControl {
    private float averageAngle, dispersionAngle, minVelocity, maxVelocity,
            minLifetime, maxLifetime, minRadius, maxRadius, flow;
    private int color;

    public PSControl(float[] velControl, float[] lifetime, float[] radius, float flow, int color) {
        setVelParams(velControl);
        setLifetimeParams(lifetime);
        setRadiusParams(radius);
        setFlow(flow);
        setColor(color);
    }

    public float getFlow() {
        return flow;
    }

    public void setFlow(float flow) {
        this.flow = flow;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    //definir intervalo de raios
    public void setRadiusParams(float[] radius) {
        minRadius = radius[0];
        maxRadius = radius[1];
    }

    //definir intervalo de tempos de vida
    public void setLifetimeParams(float[] lifetime) {
        minLifetime = lifetime[0];
        maxLifetime = lifetime[1];
    }

    //definir direção média, dispersão angular e velocidades min/máx
    public void setVelParams(float[] velControl) {
        averageAngle = velControl[0];
        dispersionAngle = velControl[1];
        minVelocity = velControl[2];
        maxVelocity = velControl[3];
    }

    public float getAverageAngle() {
        return averageAngle;
    }

    public float getDispersionAngle() {
        return dispersionAngle;
    }

    public float getMinVelocity() {
        return minVelocity;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }
    
    //gerar raio aleatório
    public float getRndRadius() {
        return getRnd(minRadius, maxRadius);
    }
    
    //gerar tempo de vida aleatório
    public float getRndLifetime() {
        return getRnd(minLifetime, maxLifetime);
    }
    
    //gerar vetor velocidade aleatório
    public PVector getRndVel() {
    	//angulo dentro do intervalo
        float angle = getRnd(averageAngle - dispersionAngle / 2, averageAngle + dispersionAngle / 2);
        
        //vetor unitario com esse angulo, escalado por uma velocidade aleatoria
        PVector v = PVector.fromAngle(angle);
        return v.mult(getRnd(minVelocity, maxVelocity));
    }
    
    //gerar valor aleatório entre min e max
    public float getRnd(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }

}
