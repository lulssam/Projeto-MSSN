package audio;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Classe responsável pela gestão de audio do jogo
 *
 * O SoundManager trata exclusivamente da música de fundo,
 * garantindo que:
 *  - Apenas uma faixa toca de cada vez
 *  - A música não é recarregada desnecessariamente se já estiver a tocar
 *  - O volume pode ser ajustado dinamicamente em tempo real
 *  - O estado de mute é utilizado globalmente
 *
 * O volume é controlado através de decibéis (MASTER_GAIN),
 * convertendo valores normalizados [0,1] para dB,
 * assegurando uma transição sonora suave e consistente.
 *
 * Esta classe é usada pelos GameStates (Menu, Play, Levels),
 * permitindo que cada estado ou nível defina a sua própria música.
 */

public class SoundManager {

    private Clip music;
    private String currentTrack;

    public void playMusic(String resourcePath, float volume01, boolean muted) {
        if (resourcePath == null) return;

        if (currentTrack != null && currentTrack.equals(resourcePath) && music != null) {
            applyVolume(volume01, muted);
            return;
        }

        stopMusic();

        try {
            URL url = getClass().getResource(resourcePath);
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

            music.loop(Clip.LOOP_CONTINUOUSLY);
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

        float v = muted ? 0f : clamp01(volume01);

        float dB;
        if (v <= 0.0001f) dB = -80f;
        else dB = (float)(20.0 * Math.log10(v));

        try {
            FloatControl gain = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
            gain.setValue(dB);
        } catch (IllegalArgumentException ignored) {
        }
    }
    
    //metodo utilizado para normalizar o volume
    private float clamp01(float x) {
        return Math.max(0f, Math.min(1f, x));
    }
}
