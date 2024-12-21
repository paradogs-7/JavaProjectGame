import javax.swing.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class PlayerMovement extends KeyAdapter {
    private JLabel player; // Oyuncunun JLabel temsilcisi
    private JPanel panel; // Oyun paneli
    private int worldWidth, worldHeight; // Oyun dünyasının boyutları
    private int speed = 3; // Karakterin varsayılan hızı
    private Set<Integer> activeKeys; // Aktif tuşları takip etmek için

    public PlayerMovement(JLabel player, JPanel panel, int worldWidth, int worldHeight) {
        this.player = player;
        this.panel = panel;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Aktif tuşları takip etmek için Set
        activeKeys = new HashSet<>();

        // Zamanlayıcı: Tuşlar basılıyken hareketi sürekli gerçekleştirir
        Timer movementTimer = new Timer(10, e -> movePlayer());
        movementTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Basılan tuşu aktif tuşlar setine ekle
        activeKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Serbest bırakılan tuşu aktif tuşlar setinden çıkar
        activeKeys.remove(e.getKeyCode());
    }

    private void movePlayer() {
        int currentX = player.getX();
        int currentY = player.getY();

        // Aktif tuşları kontrol ederek hareketi uygula
        if (activeKeys.contains(KeyEvent.VK_UP) || activeKeys.contains(KeyEvent.VK_W)) {
            currentY -= speed; // Yukarı hareket
        }
        if (activeKeys.contains(KeyEvent.VK_DOWN) || activeKeys.contains(KeyEvent.VK_S)) {
            currentY += speed; // Aşağı hareket
        }
        if (activeKeys.contains(KeyEvent.VK_LEFT) || activeKeys.contains(KeyEvent.VK_A)) {
            currentX -= speed; // Sola hareket
        }
        if (activeKeys.contains(KeyEvent.VK_RIGHT) || activeKeys.contains(KeyEvent.VK_D)) {
            currentX += speed; // Sağa hareket
        }

        // Pozisyonun sınırların dışına çıkmamasını sağla
        if (currentX < 0) currentX = 0; // Sol sınır
        if (currentY < 0) currentY = 0; // Üst sınır
        if (currentX + player.getWidth() > worldWidth) currentX = worldWidth - player.getWidth(); // Sağ sınır
        if (currentY + player.getHeight() > worldHeight) currentY = worldHeight - player.getHeight(); // Alt sınır

        // Oyuncunun pozisyonunu güncelle
        player.setLocation(currentX, currentY);
    }

    // Karakterin hızını ayarlamak için metod
    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    // Karakterin mevcut hızını almak için metod
    public int getSpeed() {
        return speed;
    }
}
