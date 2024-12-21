import java.awt.*;

public class GameObject {
    private String name; // Nesnenin adı veya türü
    private Rectangle bounds; // Nesnenin çarpışma sınırları

    public GameObject(String name, int x, int y, int width, int height) {
        this.name = name;
        this.bounds = new Rectangle(x, y, width, height);
    }

    // Nesnenin çarpışma sınırlarını döndürür
    public Rectangle getBounds() {
        return bounds;
    }

    // Nesnenin adını döndürür
    public String getName() {
        return name;
    }

    // Nesnenin pozisyonunu ayarla
    public void setPosition(int x, int y) {
        bounds.setLocation(x, y);
    }

    // Nesnenin X ve Y konumlarını döndürür
    public int getX() {
        return bounds.x;
    }

    public int getY() {
        return bounds.y;
    }

    // Nesnenin genişlik ve yüksekliğini döndürür
    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }
}
