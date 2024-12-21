import javax.swing.*;
import java.awt.*;

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