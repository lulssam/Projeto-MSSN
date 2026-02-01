package fractals;

import processing.core.PApplet;

/**
 * Classe responsável por interpretar e desenhar uma sequência de um L-System usando turtle graphics.
 *
 * A Turtle mantém dois parâmetros base:
 *  - len: comprimento do segmento desenhado para símbolos de avanço (F/G)
 *  - angle: ângulo de rotação aplicado para '+' e '-'
 *
 * A sequência do LSystem é interpretada segundo a convenção típica:
 *  - 'F' ou 'G' : desenha um segmento e avança
 *  - '+' / '-'  : roda a orientação da turtle
 *  - '[' / ']'  : guarda/restaura a matriz de transformação (ramificação)
 *
 * Existem dois modos de renderização:
 *  - render(LSystem, PApplet, double, double, double, double): converte len para píxeis a partir de limites do mundo
 *  - renderPixels(LSystem, PApplet): desenha diretamente em coordenadas locais/píxeis
 */
public class Turtle {
	private float len, angle;

	public Turtle(float len, float angle) {
		this.len = len;
		this.angle = angle;
	}
	
	//define a posição inicial e orientação da turtle na janela
	public void setPos(double[] position, float orientation, PApplet p, double minX, double maxX, double minY, double maxY) {
		
		//conversao coordenadas mundo -> pixeis (y invertido porque o ecrã cresce para baixo)
	    float px = (float) ((position[0] - minX) / (maxX - minX) * p.width);
	    float py = (float) ((1 - (position[1] - minY) / (maxY - minY)) * p.height);
	    p.translate(px, py);
	    p.rotate(-orientation); //sinal invertido para alinhar orientacao com o referencial do processing
	}
	
	//aplica escala do comprimento dos segmentos
	public void scaling(float s) {
		len *= s;
	}
	
	//desenha a sequência do L-System usando as regras F/G, +, -, [ e ]
	public void render(LSystem lsys, PApplet p, double minX, double maxX, double minY, double maxY) {
		
	    float lenPix = (float) (len / (maxX - minX) * p.width); //converte len do mundo para pixels com base na largura da janela
	    
	    for (int i = 0; i < lsys.getSequence().length(); i++) {
	        char c = lsys.getSequence().charAt(i);
	        if (c == 'F' || c == 'G') {
	            p.line(0, 0, lenPix, 0);
	            p.translate(lenPix, 0);
	        } else if (c == '+') {
	            p.rotate(angle);
	        } else if (c == '-') {
	            p.rotate(-angle);
	        } else if (c == '[') {
	            p.pushMatrix();   //inicia ramo
	        } else if (c == ']') {
	            p.popMatrix();  //volta ao ponto de ramificacao anterior
	        }
	    }
	}
	
	//desenha em coordenadas locais
	public void renderPixels(LSystem lsys, PApplet p) {
	    for (int i = 0; i < lsys.getSequence().length(); i++) {
	        char c = lsys.getSequence().charAt(i);

	        if (c == 'F' || c == 'G') {
	            p.line(0, 0, len, 0);
	            p.translate(len, 0);
	        } else if (c == '+') {
	            p.rotate(angle);
	        } else if (c == '-') {
	            p.rotate(-angle);
	        } else if (c == '[') {
	            p.pushMatrix();
	        } else if (c == ']') {
	            p.popMatrix();
	        }
	    }
	}
}