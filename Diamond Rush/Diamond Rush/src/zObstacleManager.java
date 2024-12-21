import java.util.ArrayList;
import java.util.List;

public class zObstacleManager {
    private List<GameObject> obstacles; // Tüm engellerin listesi

    public zObstacleManager() {
        obstacles = new ArrayList<>();
    }

    // Yeni bir engel ekle
    public void addObstacle(GameObject obstacle) {
        obstacles.add(obstacle);
    }

    // Bir engeli kaldır
    public void removeObstacle(GameObject obstacle) {
        obstacles.remove(obstacle);
    }

    // Tüm engelleri al
    public List<GameObject> getObstacles() {
        return obstacles;
    }

    // Belirli bir engelin çarpışma alanını kontrol etmek için kullanılabilir
    public GameObject getObstacleAt(int x, int y) {
        for (GameObject obstacle : obstacles) {
            if (obstacle.getBounds().contains(x, y)) {
                return obstacle;
            }
        }
        return null;
    }
}
