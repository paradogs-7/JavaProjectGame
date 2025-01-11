import java.awt.*;
import javax.swing.*;

/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/
public class Enemy {
    private JLabel label;
    private GameObject object;
    private int startX;
    private int endX;
    private int startY;
    private int endY;
    private int speedX;
    private int speedY;
    private int directionX = 1; // 1: sağa, -1: sola
    private int directionY = 1; // 1: aşağı, -1: yukarı
    private int playerStartX;
    private int playerStartY;

    public Enemy(int startX, int endX, int startY, int endY, int speedX, int speedY, int x, int y, int width, int height, Color color, int playerStartX, int playerStartY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.playerStartX = playerStartX;
        this.playerStartY = playerStartY;

        this.label = new JLabel();
        label.setOpaque(true);
        label.setBackground(color);
        label.setBounds(x, y, width, height);
        this.object = new GameObject("Enemy", x, y, width, height, false);
    }

    public JLabel getLabel() {
        return label;
    }

    public GameObject getObject() {
        return object;
    }

    public void move() {
        int currentX = object.getX();
        int currentY = object.getY();

        // X ekseninde hareket
        currentX += speedX * directionX;
        if (currentX >= endX) {
            currentX = endX;
            directionX = -1; // Sola dön
        } else if (currentX <= startX) {
            currentX = startX;
            directionX = 1;  // Sağa dön
        }

        // Y ekseninde hareket
        currentY += speedY * directionY;
        if (currentY >= endY) {
            currentY = endY;
            directionY = -1;
        } else if (currentY <= startY) {
            currentY = startY;
            directionY = 1;
        }

        object.setPosition(currentX, currentY);
        label.setLocation(currentX, currentY);
    }

    public boolean checkCollision(Rectangle playerBounds) {
        return playerBounds.intersects(object.getBounds());
    }

    public void resetPlayerPosition(JLabel player) {
        player.setLocation(playerStartX, playerStartY);
    }

}