import javax.swing.*;
import java.awt.*;
/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/
public class zPlayer {
    private JLabel playerLabel;

    public zPlayer(int x, int y, int width, int height, Color color) {
        playerLabel = new JLabel();
        playerLabel.setOpaque(true);
        playerLabel.setBackground(color);
        playerLabel.setBounds(x, y, width, height); // Oyuncunun başlangıç pozisyonu ve boyutu
    }

    public JLabel getLabel() {
        return playerLabel;
    }

}