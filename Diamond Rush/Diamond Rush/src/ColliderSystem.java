import java.util.ArrayList;
import java.util.List;

public class ColliderSystem {
    private List<GameObject> colliders; // Çarpışma kontrolü yapılacak nesneler

    public ColliderSystem() {
        colliders = new ArrayList<>();
    } 

    // Çarpışma kontrolüne nesne ekle
    public void addCollider(GameObject object) {
        colliders.add(object);
    }

    // Çarpışma kontrolünden nesne kaldır
    public void removeCollider(GameObject object) {
        colliders.remove(object);
    }

    // Tüm nesneler arasında çarpışmaları kontrol et
    public void checkCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                GameObject obj1 = colliders.get(i);
                GameObject obj2 = colliders.get(j);

                if (obj1.getBounds().intersects(obj2.getBounds())) {
                    // Çarpışma algılandı, tepkiyi işleyin
                    onCollision(obj1, obj2);
                }
            }
        }
    }

    // Çarpışma olduğunda yapılacak işlem
    public void onCollision(GameObject obj1, GameObject obj2) {
        System.out.println("Collision detected between " + obj1.getName() + " and " + obj2.getName());
        // Çarpışma tepkisini burada tanımlayın
    }
}
