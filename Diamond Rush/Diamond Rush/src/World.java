import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class World {
    private JFrame frame;
    private JPanel levelPanel;
    private JLabel player;
    private Camera camera;
    private int worldWidth = 10800;
    private int worldHeight = 1080;
    private ArrayList<GameObjectWrapper> objects;
    private boolean keyAcquired = false; // Anahtarın alınıp alınmadığını tutar

    public World() {
        frame = new JFrame("Diamond Rush - Level 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        objects = new ArrayList<>(); // Obje listesi

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
        addKey(1000, 500); // Anahtarı ekle
        addObject(0, 50, 50, 764, new Color(21, 64, 77), "Obstacle", false);
        addObject(0, 0, 10800, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(0, 814, 10800, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(400, 500, 99, 50, new Color(21, 64, 77), "Obstacle", false);

        PlayerMovement playerMovement = new PlayerMovement(player, levelPanel, worldWidth, worldHeight, objects);
        levelPanel.addKeyListener(playerMovement);

        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();

        Timer timer = new Timer(16, e -> {
            updateCamera();
            checkKeyCollision(); // Anahtar çarpışmasını kontrol et
        });
        timer.start();

        frame.pack();
        frame.setVisible(true);
    }


    private void addKey(int x, int y) {
        JLabel keyLabel = new JLabel(); // Label oluştur

        keyLabel.setOpaque(true);
        keyLabel.setBackground(Color.YELLOW); // Anahtarı sarı yap

        keyLabel.setBounds(x, y, 35, 35);
        GameObject keyObject = new GameObject("key", x, y, 35, 35, true); // Yeni anahtar objesi
        objects.add(new GameObjectWrapper(keyLabel,keyObject));
        levelPanel.add(keyLabel);
    }

    private void addPlayer() {
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(new Color(176, 76, 106)); // Gül kurusu
        player.setBounds(750, 400, 35, 35);
        levelPanel.add(player);
    }

    private void addObject(int x, int y, int width, int height, Color color, String name, boolean isKey) {
        JLabel newLabel = new JLabel();
        newLabel.setOpaque(true);
        newLabel.setBackground(color);
        newLabel.setBounds(x, y, width, height);

        GameObject newObject = new GameObject(name, x, y, width, height, isKey);
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

    private void checkKeyCollision() {
        if (keyAcquired) return; // Anahtar zaten alındıysa kontrol yapma

        Rectangle playerBounds = player.getBounds();

        for (GameObjectWrapper objectWrapper : objects) {
            if (objectWrapper.object.isKey() && playerBounds.intersects(objectWrapper.object.getBounds())) {
                keyAcquired = true; // Anahtarı aldık
                levelPanel.removeAll(); // Tüm bileşenleri temizle
                levelPanel.add(player);  // Oyuncuyu ekle
                levelPanel.revalidate();
                levelPanel.repaint();
                System.out.println("Key acquired!");
                break; // Çarpışmayı bulduktan sonra döngüyü bitir
            }
        }
    }

    public static void main(String[] args) {
        new World();
    }
}