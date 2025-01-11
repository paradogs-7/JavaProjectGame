import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/
public class SoundManager {
    private Map<String, Clip> clips;
    private float volume = 0.5f; // Varsayılan ses seviyesi

    public SoundManager() {
        clips = new HashMap<>();
    }

    public void loadSound(String name, String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

            if (inputStream == null) {
                System.err.println("Müzik dosyası bulunamadı: " + path);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clips.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("Müzik yüklenirken hata oluştu: " + path);
        }
    }

    public void playSound(String name, boolean loop) {
        if (!clips.containsKey(name)) {
            System.err.println("Müzik dosyası bulunamadı: " + name);
            return;
        }

        Clip clip = clips.get(name);
        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0); // Başa sar
        setClipVolume(clip); // Ses seviyesini uygula

        clip.start();
        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopSound(String name) {
        if (clips.containsKey(name)) {
            Clip clip = clips.get(name);
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        // Tüm kliplerin ses seviyesini ayarla
        clips.values().forEach(this::setClipVolume);
    }

    private void setClipVolume(Clip clip) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (gainControl != null) {
                float dB = (float) (20f * Math.log10(volume));
                gainControl.setValue(Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum())));
            }
        }
    }

    public float getVolume() {
        return volume;
    }

}