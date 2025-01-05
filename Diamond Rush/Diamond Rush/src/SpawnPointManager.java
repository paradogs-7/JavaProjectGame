import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class SpawnPointManager {

    // Her seviyenin spawn noktalarını tutmak için bir Map kullanıyoruz
    private Map<Integer, Point> spawnPoints;

    public SpawnPointManager() {
        spawnPoints = new HashMap<>();
    }

    /**
     * İlgili level için spawn (x, y) değerini ayarlar.
     * @param level Seviye numarası
     * @param x Spawn noktasının X koordinatı
     * @param y Spawn noktasının Y koordinatı
     */
    public void setSpawnPointForLevel(int level, int x, int y) {
        spawnPoints.put(level, new Point(x, y));
    }

    /**
     * İlgili level için atanmış spawn (Point) değerini döndürür.
     * @param level Seviye numarası
     * @return Spawn noktası Point nesnesi veya ayarlanmamışsa null
     */
    public Point getSpawnPointForLevel(int level) {
        return spawnPoints.get(level);
    }

    /**
     * İlgili level için atanmış spawn X koordinatını döndürür.
     * @param level Seviye numarası
     * @return X koordinatı veya ayarlanmamışsa -1
     */
    public int getSpawnXForLevel(int level) {
        Point p = spawnPoints.get(level);
        if (p != null) {
            return p.x;
        }
        return -1; // ya da istediğiniz bir varsayılan değer
    }

    /**
     * İlgili level için atanmış spawn Y koordinatını döndürür.
     * @param level Seviye numarası
     * @return Y koordinatı veya ayarlanmamışsa -1
     */
    public int getSpawnYForLevel(int level) {
        Point p = spawnPoints.get(level);
        if (p != null) {
            return p.y;
        }
        return -1; // ya da istediğiniz bir varsayılan değer
    }

    /**
     * Örnek olarak, level değerine karşılık otomatik spawn noktalarını
     * belirlemek isterseniz bu metodu kullanabilirsiniz.
     * (İsteğe bağlı bir metot, manuel çağırarak level spawn'larını topluca set edebilirsiniz.)
     */
    public void initializeDefaultSpawnPoints() {
        // Örneğin 1. seviye için (600, 500),
        // 2. seviye için (3000, 500) vb. değerleri atayabilirsiniz.
        setSpawnPointForLevel(1, 600, 500);
        setSpawnPointForLevel(2, 3000, 500);
        setSpawnPointForLevel(3, 5000, 500);
        setSpawnPointForLevel(4, 7500, 500);
    }
}