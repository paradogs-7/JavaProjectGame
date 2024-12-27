import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class zGameController {
    private int score;
    private boolean isPaused;
    private SoundManager soundManager;
    private static final String SETTINGS_FILE = "settings.txt";
    private String selectedResolution = "1920x1080";
    private boolean isFullscreen = true;

    public zGameController(SoundManager soundManager) {
        this.soundManager = soundManager;
        loadSettings();
    }


    public void startGame() {
        score = 0;
        isPaused = false;
        System.out.println("Game started!");
        new GameFrame(selectedResolution, isFullscreen, soundManager);
    }

    public void pauseGame() {
        isPaused = true;
        System.out.println("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        System.out.println("Game resumed.");
    }

    private void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("resolution=")) {
                    selectedResolution = line.substring(11);
                } else if (line.startsWith("fullscreen=")) {
                    isFullscreen = Boolean.parseBoolean(line.substring(11));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ayarlar dosyası okunurken hata oluştu veya dosya bulunamadı. Varsayılan ayarlar kullanılacak.");
        }
    }


    public static void main(String[] args) {
        SoundManager soundManager = new SoundManager();
        zGameController gameController = new zGameController(soundManager);
        gameController.startGame();
    }

}