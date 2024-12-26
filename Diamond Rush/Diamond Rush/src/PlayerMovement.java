import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerMovement extends KeyAdapter {
    private JLabel player;
    private JPanel panel;
    private int worldWidth, worldHeight;
    private int speed = 3;
    private Set<Integer> activeKeys;
    private List<GameObjectWrapper> obstacles;
    private Timer movementTimer;
    private boolean isMovementPaused = false;

    public PlayerMovement(JLabel player, JPanel panel, int worldWidth, int worldHeight, List<GameObjectWrapper> obstacles) {
        this.player = player;
        this.panel = panel;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.obstacles = obstacles;
        activeKeys = new HashSet<>();

        movementTimer = new Timer(10, e -> {
            // Eğer duraklatılmadıysa movePlayer() çalışsın
            if (!isMovementPaused) {
                movePlayer();
            }
        });
        movementTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        activeKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        activeKeys.remove(e.getKeyCode());
    }

    private void movePlayer() {
        int currentX = player.getX();
        int currentY = player.getY();

        int nextX = currentX;
        int nextY = currentY;

        // Yönlere göre yeni pozisyonları belirle
        if (activeKeys.contains(KeyEvent.VK_UP) || activeKeys.contains(KeyEvent.VK_W)) {
            nextY -= speed; // Yukarı hareket
        }
        if (activeKeys.contains(KeyEvent.VK_DOWN) || activeKeys.contains(KeyEvent.VK_S)) {
            nextY += speed; // Aşağı hareket
        }
        if (activeKeys.contains(KeyEvent.VK_LEFT) || activeKeys.contains(KeyEvent.VK_A)) {
            nextX -= speed; // Sola hareket
        }
        if (activeKeys.contains(KeyEvent.VK_RIGHT) || activeKeys.contains(KeyEvent.VK_D)) {
            nextX += speed; // Sağa hareket
        }

        Rectangle nextBounds = new Rectangle(nextX, nextY, player.getWidth(), player.getHeight());
        for (GameObjectWrapper obstacleWrapper : obstacles) {
            if (obstacleWrapper.object.getType() != null && !obstacleWrapper.object.getType().equals("key") && !obstacleWrapper.object.getType().equals("door") && nextBounds.intersects(obstacleWrapper.object.getBounds())) {
                // Çarpışma varsa yönleri sıfırla
                if (activeKeys.contains(KeyEvent.VK_UP) || activeKeys.contains(KeyEvent.VK_W)) {
                    nextY = currentY;
                }
                if (activeKeys.contains(KeyEvent.VK_DOWN) || activeKeys.contains(KeyEvent.VK_S)) {
                    nextY = currentY;
                }
                if (activeKeys.contains(KeyEvent.VK_LEFT) || activeKeys.contains(KeyEvent.VK_A)) {
                    nextX = currentX;
                }
                if (activeKeys.contains(KeyEvent.VK_RIGHT) || activeKeys.contains(KeyEvent.VK_D)) {
                    nextX = currentX;
                }
            }
        }


        // Pozisyonun dünya sınırlarının dışına çıkmasını engelle
        if (nextX < 0) nextX = 0;
        if (nextY < 0) nextY = 0;
        if (nextX + player.getWidth() > worldWidth) nextX = worldWidth - player.getWidth();
        if (nextY + player.getHeight() > worldHeight) nextY = worldHeight - player.getHeight();

        // Yeni pozisyonu ayarla
        player.setLocation(nextX, nextY);
    }

    public void pauseMovement() {
        isMovementPaused = true;
    }

    public void resumeMovement() {
        isMovementPaused = false;
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }

}