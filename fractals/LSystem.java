package fractals;

/**
 * Implementa um L-System (Lindenmayer System) para geração procedural de sequências.
 *
 * Um LSystem é definido por:
 *  - um axioma (sequência inicial)
 *  - um conjunto de regras de substituição (produções)
 *  - um número de gerações aplicadas
 *
 * A cada geração, todos os símbolos da sequência atual são substituídos
 * em paralelo segundo as regras definidas, produzindo uma nova sequência.
 *
 * Esta classe é usada como base para a geração de fractais (árvores),
 * sendo posteriormente interpretada por uma Turtle para renderização.
 */

import java.util.HashMap;

public class LSystem {
	
	//sequência atual, regras de substituição e contador de gerações
    private String sequencia;
    private HashMap<Character, String> conjuntoRegras;
    private int generation;

    //construtor: inicializa com o axioma base e prepara o conjunto de regras
    public LSystem(String axioma) {
    	sequencia = axioma;
    	conjuntoRegras = new HashMap<>();
        generation = 0;
    }

    //retorna a sequência atual do l-system
    public String getSequence() {
        return sequencia;
    }
    
    //retorna o número de gerações aplicadas
    public int getGeneration() {
        return generation;
    }

    //adiciona uma regra de substituição para um símbolo
    public void regra(char simbolo, String substituto) {
    	conjuntoRegras.put(simbolo, substituto);
    }

    //avança para a próxima geração aplicando todas as regras
    public void proxGen() {
        generation++;

        //stringBuilder para a concatenação de strings
        StringBuilder proxGen = new StringBuilder();
        
        for (int i = 0; i < sequencia.length(); i++) {
            char c = sequencia.charAt(i);
            proxGen.append(conjuntoRegras.getOrDefault(c, "" + c));  //se existir regra, substitui, caso contrario, mantem o simbolo
        }
        
        this.sequencia = proxGen.toString(); //atualiza a sequencia para a proxima geraçao
    }
}
