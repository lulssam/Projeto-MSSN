package game;

/**
 * Classe que armazena as definições globais do jogo
 * 
 * As Settings têm  parâmetros configuráveis que são
 * partilhados entre vários estados e sistemas, como:
 *  - Volume global do som
 *  - Dimensões da janela do jogo
 *  - Estado de mute
 *  - Pontuação do jogador
 * 
 * Esta classe funciona como um contentor simples de dados,
 * permitindo acesso centralizado e fácil às configurações
 * atuais do jogo.
 */

public class Settings {
    public float volume = 0.4f;    //volume do som 
    public int width = 800;        //largura da janela
    public int height = 600;       //altura da janela
    public boolean muted = false;  //se o som está mutado
    public int lastScore = 0;      //score atual/ultimo obtido
}
