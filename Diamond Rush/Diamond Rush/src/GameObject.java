import java.awt.*;

public class GameObject {
    private String name;
    private Rectangle bounds;
    private boolean isKey; // Yeni özellik: Anahtar mı?

    public GameObject(String name, int x, int y, int width, int height, boolean isKey) {
        this.name = name;
        this.bounds = new Rectangle(x, y, width, height);
        this.isKey = isKey;
    }

    // Mevcut metotlar
    public Rectangle getBounds() {
        return bounds;
    }

    public String getName() {
        return name;
    }

    public void setPosition(int x, int y) {
        bounds.setLocation(x, y);
    }

    public int getX() {
        return bounds.x;
    }

    public int getY() {
        return bounds.y;
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key){
        this.isKey = key;
    }
}