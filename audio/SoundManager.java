package audio;

import javax.sound.sampled.*;

import java.net.URL;

/**
 * Classe responsável pela gestão de áudio do jogo (música de fundo).
 *
 * O SoundManager trata exclusivamente da música ambiente,
 * garantindo que:
 *  - apenas uma faixa toca de cada vez
 *  - a música não é recarregada se já estiver ativa
 *  - o volume pode ser ajustado dinamicamente em tempo real
 *  - o estado de mute é aplicado globalmente
 *
 * O volume é controlado através de decibéis (MASTER_GAIN),
 * convertendo valores normalizados no intervalo [0,1] para dB,
 * assegurando uma transição sonora suave e consistente.
 *
 * Esta classe é utilizada pelos diferentes estados do jogo
 * (menu, jogo, níveis), permitindo que cada estado defina a sua
 * própria música de fundo.
 */

public class SoundManager {

    private Clip music;
    private String currentTrack;
    
    public void playMusic(String resourcePath, float volume01, boolean muted) {
        if (resourcePath == null) return;
        
        //se a mesma musica ja estiver a tocar, apenas atualiza volume/mute
        if (currentTrack != null && currentTrack.equals(resourcePath) && music != null) {
            applyVolume(volume01, muted);
            return;
        }

        stopMusic(); //para a musica anterior antes de carregar uma nova

        try {
            URL url = getClass().getResource(resourcePath);  //carrega audio a partir dos recursos do projeto
            if (url == null) {
                System.out.println("Audio not found: " + resourcePath);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);

            music = clip;
            currentTrack = resourcePath;

            applyVolume(volume01, muted);

            music.loop(Clip.LOOP_CONTINUOUSLY);  //loop infinito para musica de fundo
            music.start();

        } catch (Exception e) {
            System.out.println("Audio error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (music != null) {
            music.stop();
            music.close();
            music = null;
        }
        currentTrack = null;
    }

    public void applyVolume(float volume01, boolean muted) {
        if (music == null) return;
        
        //mute ignora volume definido
        float v;
        if (muted) {
            v = 0f;
        } else {
            v = clamp01(volume01);
        }
        
       //conversao de volume linear [0,1] para escala logaritmica (dB)
        float dB;
        if (v <= 0.0001f) dB = -80f;  //silencio efetivo
        else dB = (float)(20.0 * Math.log10(v));

        try {
            FloatControl gain = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB)); //garante que o valor esta dentro dos limites suportados pelo sistema
            gain.setValue(dB);
        } catch (IllegalArgumentException ignored) {
        }
    }
    
    //metodo utilizado para normalizar o volume (limita valor ao intervalo [0,1])
    private float clamp01(float x) {
        return Math.max(0f, Math.min(1f, x));
    }
}
