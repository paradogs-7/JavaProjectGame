import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private WorldController worldController;
    private String selectedResolution;
    private boolean isFullscreen;
    private SoundManager soundManager;

    public GameFrame(String selectedResolution, boolean isFullscreen, SoundManager soundManager) {
        // Parametrelerden gelen değerleri saklayın
        this.selectedResolution = selectedResolution;
        this.isFullscreen = isFullscreen;
        this.soundManager = soundManager;

        // JFrame ayarları
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        // Çözünürlük ve tam ekran ayarlarını uygula
        applyResolution(selectedResolution);
        applyFullscreen(isFullscreen);

        // WorldController oluşturup ekleyin
        worldController = new WorldController(selectedResolution, isFullscreen, soundManager);
        add(worldController.getPanel()); // WorldPanel, WorldController içinden alınıyor

        pack();
        setVisible(true);
    }

    /**
     * Metin olarak gelen "1920x1080" gibi bir çözünürlüğü parse edip pencere boyutunu ayarlar.
     */
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
