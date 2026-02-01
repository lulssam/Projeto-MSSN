package aa;

/**
 * Enum que define os diferentes tipos lógicos de entidades no sistema.
 *
 * Cada Type agrupa parâmetros de movimento e visão que influenciam
 * o comportamento dos boids, permitindo diferenciar papéis como:
 *  - entidades neutras
 *  - predadores
 *  - presas
 *
 * Os parâmetros associados a cada tipo podem ser usados para:
 *  - ajustar limites de velocidade e força
 *  - definir alcance e ângulo de visão
 *  - modular comportamentos como fuga, perseguição ou atração
 */

public enum Type {
	NEUTRAL(5, 0, 0, 0, 0),
	PREDATOR(5, 5, 5, .1f, (float) Math.PI * 0.3f),
	PREY(9, 9, 20, .5f, (float) Math.PI * 0.75f),
	;
	
	//limites de movimento
	public final float maxSpeed;
	public final float maxForce;
	
	//parametros de visao
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
