import java.awt.*;

public class GameObject {
    private String name;
    private Rectangle bounds;
    private boolean isKey;
    private String type; // Nesne tipi: "key", "door", "obstacle" vb.

    public GameObject(String name, int x, int y, int width, int height, boolean isKey, String type) {
        this.name = name;
        this.bounds = new Rectangle(x, y, width, height);
        this.isKey = isKey;
        this.type = type;
    }

    public GameObject(String name, int x, int y, int width, int height, boolean isKey) {
        this.name = name;
        this.bounds = new Rectangle(x, y, width, height);
        this.isKey = isKey;
        this.type = null;
    }

    public String getType() {
        return type;
    }

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

    public void setKey(boolean key) {
        this.isKey = key;
    }

    public void setType(String type) {
        this.type = type;
    }

}