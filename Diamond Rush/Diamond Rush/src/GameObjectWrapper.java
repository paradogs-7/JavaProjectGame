import javax.swing.*;

/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/
// GameObjectWrapper sınıfı: JLabel'i GameObject ile birlikte tutar
public class GameObjectWrapper {
    JLabel label;       // Görsel bileşen
    GameObject object;  // Mantıksal bileşen (çarpışma sınırları)

    public GameObjectWrapper(JLabel label, GameObject object) {
        this.label = label;
        this.object = object;
    }

}