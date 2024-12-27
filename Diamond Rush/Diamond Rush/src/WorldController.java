import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class WorldController {

    private WorldPanel worldPanel;      // Oyun sahnesini çizen panel
    private Timer updateTimer;
    private boolean isPaused = false;

    private SoundManager soundManager;
    private String selectedResolution;
    private boolean isFullscreen;

    public WorldController(String selectedResolution, boolean isFullscreen, SoundManager soundManager) {
        this.selectedResolution = selectedResolution;
        this.isFullscreen = isFullscreen;
        this.soundManager = soundManager;

        // Oyun müziğini yükle ve çal
        soundManager.loadSound("gameMusic", "resources/gamemusic.wav");
        soundManager.playSound("gameMusic", true);

        // WorldPanel (oyun sahnesi) oluştur
        worldPanel = new WorldPanel(soundManager);

        // --- Swing Timer ile 16 ms arayla updateWorld() çağrısı ---
        updateTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    worldPanel.updateWorld();
                }
            }
        });
        updateTimer.start();

        // --- 'P' tuşu ile duraklatma/başlatma için KeyListener ---
        worldPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    togglePause();
                }
            }
        });

        // Odaklanabilmesi için
        worldPanel.setFocusable(true);
        worldPanel.requestFocusInWindow();
    }

    /**
     * Oyunu duraklatıp başlatmayı togglar.
     */
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            // Müzik durduruluyor
            soundManager.stopSound("gameMusic");
            worldPanel.showPausedLabel(true);
        } else {
            // Müzik yeniden çalıyor
            soundManager.playSound("gameMusic", true);
            worldPanel.showPausedLabel(false);
        }
    }

    /**
     * Dışarıdan panel erişimi: Ana pencere (GameFrame) bu paneli ekler.
     */
    public JPanel getPanel() {
        return worldPanel;
    }
}
