import javax.swing.*;

// GameObjectWrapper sınıfı: JLabel'i GameObject ile birlikte tutar
public class GameObjectWrapper {
    JLabel label;       // Görsel bileşen
    GameObject object;  // Mantıksal bileşen (çarpışma sınırları)

    public GameObjectWrapper(JLabel label, GameObject object) {
        this.label = label;
        this.object = object;
    }

}