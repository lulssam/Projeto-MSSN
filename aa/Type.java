package aa;

//Enum Type define diferentes categorias de entidades, cada uma com parâmetros específicos
//inclui atributos de movimento e visão que influenciam o comportamento dos boids

public enum Type {
    NEUTRAL(5, 0, 0, 0, 0),
    OBSTACLE(0, 0, 0, 0, 0),
    FOOD(0, 0, 0, 0, 0),
    PREDATOR(5, 5, 5, .1f, (float) Math.PI * 0.3f),
    PREY(9, 9, 20, .5f, (float) Math.PI * 0.75f),

    ;
    public final float maxSpeed;
    public final float maxForce;
    public final float visionDistance;
    public final float visionSafeDistance;
    public final float visionAngle;

    Type(float maxSpeed, float maxForce, float visionDistance, float visionSafeDistance, float visionAngle) {
        this.maxSpeed = maxSpeed;
        this.maxForce = maxForce;
        this.visionDistance = visionDistance;
        this.visionSafeDistance = visionSafeDistance;
        this.visionAngle = visionAngle;
    }
}
