package fractals;

import processing.core.PApplet;

/**
 * Classe utilitária que centraliza a criação de fractais baseados em L-Systems.
 *
 * Fornece "presets" de árvores, definidos por:
 *  - axioma (string inicial)
 *  - regras de escrita (produção) para cada símbolo
 *  - número de gerações a expandir
 *
 * Inclui também métodos para Turtle, com parâmetros ajustados
 * (comprimento do segmento e ângulo) para renderizar cada árvore de forma consistente.
 *
 * Esta classe não desenha diretamente: apenas devolve estruturas (LSystem e Turtle)
 * prontas a serem renderizadas noutras partes do jogo.
 */

public class Fractals {
	
	//arvore com ramificacao simetrica e alongamento
    public static LSystem tree1(int generations) {
        LSystem ls = new LSystem("X");
        ls.regra('X', "F[+X][-X]FX");
        ls.regra('F', "FF");
        for (int i = 0; i < generations; i++) ls.proxGen();
        return ls;
    }
    
    //variacao com ramificacao e assimetria ligeira
    public static LSystem tree2(int generations) {
        LSystem ls = new LSystem("X");
        ls.regra('X', "F[+X]F[-X]+X");
        ls.regra('F', "FF");
        for (int i = 0; i < generations; i++) ls.proxGen();
        return ls;
    }
    
    //estrutura mais simples, focada em ramificacao
    public static LSystem tree3(int generations) {
        LSystem ls = new LSystem("X");
        ls.regra('X', "F[+X][-X]");
        ls.regra('F', "FF");
        for (int i = 0; i < generations; i++) ls.proxGen();
        return ls;
    }
    
   //arvore baseada apenas em f, com varias ramificacoes por segmento
    public static LSystem tree4(int generations) {
        LSystem ls = new LSystem("F");
        ls.regra('F', "F[+F]F[-F][F]");
        for (int i = 0; i < generations; i++) ls.proxGen();
        return ls;
    }
    
    //turtles com parametros ajustados a cada preset (step e angulo)
    public static Turtle turtleTree1() {return new Turtle(4f, PApplet.radians(25.7f));}
    public static Turtle turtleTree2() {return new Turtle(5f, PApplet.radians(25f));}
    public static Turtle turtleTree3() {return new Turtle(5f, PApplet.radians(22f));}
    public static Turtle turtleTree4() {return new Turtle(4f, PApplet.radians(20f));}
}
