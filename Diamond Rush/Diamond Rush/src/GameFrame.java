import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private WorldController worldController;
    private String selectedResolution;
    private boolean isFullscreen;
    private SoundManager soundManager;

    // Yeni eklenen değişken
    private int lastLevel;

    // Yapıcı metot artık lastLevel'ı da alıyor
    public GameFrame(String selectedResolution, boolean isFullscreen, SoundManager soundManager, int lastLevel) {
        // Parametrelerden gelen değerleri saklayın
        this.selectedResolution = selectedResolution;
        this.isFullscreen = isFullscreen;
        this.soundManager = soundManager;
        this.lastLevel = lastLevel;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        applyResolution(selectedResolution);
        applyFullscreen(isFullscreen);

        // WorldController oluşturup ekleyin (lastLevel'ı da veriyoruz)
        worldController = new WorldController(selectedResolution, isFullscreen, soundManager, lastLevel);
        add(worldController.getPanel());

        pack();
        setVisible(true);
    }

    private void applyResolution(String resolution) {
        String[] parts = resolution.split("x");
        if (parts.length == 2) {
            try {
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                setSize(width, height);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz çözünürlük formatı: " + resolution);
            }
        }
    }

    /**
     * Pencereyi tam ekran veya pencere moduna geçirir.
     */
    private void applyFullscreen(boolean fullscreen) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (fullscreen) {
            gd.setFullScreenWindow(this); // Tam ekran moduna al
        } else {
            // Pencere moduna dön
            gd.setFullScreenWindow(null);
            // Mevcut çözünürlük metnini uygula (örneğin "1920x1080")
            applyResolution(selectedResolution);
        }
    }
}
