public class Camera {
    private int x, y; // Kamera pozisyonu
    private int viewportWidth, viewportHeight; // Görünür alan (kamera genişliği ve yüksekliği)
    private int worldWidth, worldHeight; // Oyun dünyasının boyutları

    public Camera(int viewportWidth, int viewportHeight, int worldWidth, int worldHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.x = 0;
        this.y = 0;
    }

    // Kamera pozisyonunu güncelle
    public void update(int targetX, int targetY) {
        // Hedefin (örneğin oyuncu) merkezini ortalamak için hesaplama
        x = targetX - viewportWidth / 2;
        y = targetY - viewportHeight / 2;

        // Kamera sınırlarının dünya boyutlarını aşmasını engelle
        x = Math.max(0, Math.min(x, worldWidth - viewportWidth));
        y = Math.max(0, Math.min(y, worldHeight - viewportHeight));
    }

    // Kamera'nın X pozisyonunu döndür
    public int getX() {
        return x;
    }

    // Kamera'nın Y pozisyonunu döndür
    public int getY() {
        return y;
    }

    // Kamera'nın genişlik ve yükseklik ayarını güncelle
    public void setViewportSize(int width, int height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

}