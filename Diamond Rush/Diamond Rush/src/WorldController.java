import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/
public class WorldController {
    private WorldPanel worldPanel;
    private Timer updateTimer;
    private boolean isPaused = false;

    private SoundManager soundManager;
    private String selectedResolution;
    private boolean isFullscreen;

    // Yeni eklenen değişken
    private int lastLevel;

    public WorldController(String selectedResolution, boolean isFullscreen, SoundManager soundManager, int lastLevel) {
        this.selectedResolution = selectedResolution;
        this.isFullscreen = isFullscreen;
        this.soundManager = soundManager;
        this.lastLevel = lastLevel;

        soundManager.loadSound("gameMusic", "resources/gamemusic.wav");
        soundManager.playSound("gameMusic", true);

        // SpawnPointManager örneği oluşturulur ve varsayılan noktaları ekler
        SpawnPointManager spawnPointManager = new SpawnPointManager();
        spawnPointManager.initializeDefaultSpawnPoints();

        // WorldPanel’i lastLevel ve spawnPointManager ile başlatıyoruz
        worldPanel = new WorldPanel(soundManager, lastLevel, spawnPointManager);

        updateTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    worldPanel.updateWorld();
                }
            }
        });
        updateTimer.start();

        // Klavye olayları (Pause tuşu P)
        worldPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    togglePause();
                }
            }
        });

        worldPanel.setFocusable(true);
        worldPanel.requestFocusInWindow();
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            soundManager.stopSound("gameMusic");
            worldPanel.showPausedLabel(true);
        } else {
            soundManager.playSound("gameMusic", true);
            worldPanel.showPausedLabel(false);
        }
    }

    public JPanel getPanel() {
        return worldPanel;
    }
}
