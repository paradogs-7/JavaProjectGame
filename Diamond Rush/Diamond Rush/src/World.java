import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class World {
    private JFrame frame;
    private JPanel levelPanel;
    private JLabel player;

    private Camera camera;
    private int worldWidth = 10800; // Oyun dünyası genişliği
    private int worldHeight = 1080; // Oyun dünyası yüksekliği

    private ArrayList<GameObjectWrapper> objects; // Objeleri tutan liste

    public World() {
        frame = new JFrame("Diamond Rush - Level 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        objects = new ArrayList<>(); // Objeler listesi

        levelPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(213, 231, 181));
                g.fillRect(0, 0, worldWidth, worldHeight); // Tüm dünya boyutunu doldur
            }
        };

        levelPanel.setLayout(null);
        levelPanel.setPreferredSize(new Dimension(worldWidth, worldHeight));
        levelPanel.setBounds(0, 0, worldWidth, worldHeight);
        frame.add(levelPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        camera = new Camera(screenSize.width, screenSize.height, worldWidth, worldHeight);

        addPlayer();
        addObject(0, 50, 50, 764, new Color(21, 64, 77), "Obstacle");
        addObject(0, 0, 10800, 50, new Color(21, 64, 77), "Obstacle");
        addObject(0, 814, 10800, 50, new Color(21, 64, 77), "Obstacle");
        addObject(400, 500, 99, 50, new Color(21, 64, 77), "Obstacle");

        PlayerMovement playerMovement = new PlayerMovement(player, levelPanel, worldWidth, worldHeight, objects);
        levelPanel.addKeyListener(playerMovement);

        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();

        Timer timer = new Timer(16, e -> {
            updateCamera();
        });
        timer.start();

        frame.pack();
        frame.setVisible(true);
    }

    private void addPlayer() {
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(new Color(176, 76, 106)); // Gül kurusu
        player.setBounds(750, 400, 35, 35);
        levelPanel.add(player);
    }

    private void addObject(int x, int y, int width, int height, Color color, String name) {
        JLabel newLabel = new JLabel();
        newLabel.setOpaque(true);
        newLabel.setBackground(color);
        newLabel.setBounds(x, y, width, height);

        GameObject newObject = new GameObject(name, x, y, width, height);
        objects.add(new GameObjectWrapper(newLabel, newObject));

        levelPanel.add(newLabel);
    }

    private void updateCamera() {
        camera.update(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);

        int cameraWidth = 1920;
        int cameraHeight = 1080;

        int x = Math.max(0, Math.min(camera.getX(), worldWidth - cameraWidth));
        int y = Math.max(0, Math.min(camera.getY(), worldHeight - cameraHeight));

        levelPanel.setLocation(-x, -y);
    }

    public static void main(String[] args) {
        new World();
    }
}
