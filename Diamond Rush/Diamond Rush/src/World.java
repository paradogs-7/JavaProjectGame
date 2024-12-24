import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class World {
    private JFrame frame;
    private JPanel levelPanel;
    private JLabel player;
    private Camera camera;
    private int worldWidth = 10800;
    private int worldHeight = 1080;
    private ArrayList<GameObjectWrapper> objects;
    private boolean keyAcquired = false;
    private boolean isPaused = false;
    private Timer updateTimer;
    private PlayerMovement playerMovement;
    private JLabel pausedLabel;
    private SoundManager soundManager;
    private String selectedResolution;
    private boolean isFullscreen;
    private ArrayList<Enemy> enemies; // Enemy listesi eklendi


    public World(SoundManager soundManager, String selectedResolution, boolean isFullscreen) {
        this.selectedResolution = selectedResolution;
        this.isFullscreen = isFullscreen;
        this.soundManager = soundManager;
        soundManager.loadSound("gameMusic", "resources/gamemusic.wav");
        soundManager.playSound("gameMusic", true);

        // Pencere oluşturma ve ayarlara göre ayarlama
        frame = new JFrame("Diamond Rush - Level 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        applyResolution(selectedResolution);
        applyFullscreen(isFullscreen);


        objects = new ArrayList<>(); // Obje listesi
        enemies = new ArrayList<>(); // Enemy listesi

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

        // Oyuncu ve nesneleri ekleyelim
        addPlayer();
        addKey(1000, 500);
        addObject(0, 50, 50, 980, new Color(21, 64, 77), "Obstacle", false);
        addObject(0, 0, 2400, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(0, 1030, 2400, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(50, 630, 400, 400, new Color(180, 0, 0), "Obstacle", false);
        addObject(50, 50, 400, 400, new Color(180, 0, 0), "Obstacle", false);
        addObject(2000, 50, 400, 400, new Color(180, 0, 0), "Obstacle", false);
        addObject(2000, 630, 400, 400, new Color(180, 0, 0), "Obstacle", false);
        addObject(2900, 0, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(2900, 1030, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(2900, 730, 1300, 300, new Color(180, 0, 0), "Obstacle", false);
        addObject(2900, 50, 1300, 300, new Color(180, 0, 0), "Obstacle", false);
        addObject(5320, 0, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(5320, 1030, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(5320, 50, 50, 780, new Color(21, 64, 77), "Obstacle", false);
        addObject(5370, 250, 450, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(6220, 250, 450, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(7190, 50, 50, 780, new Color(21, 64, 77), "Obstacle", false);
        addObject(6990, 250, 200, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(7740, 0, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(7740, 1030, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(8124, 50, 384, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(8892, 50, 384, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(9610, 50, 50, 980, new Color(21, 64, 77), "Obstacle", false);
        addEnemies();

        // Oyuncu hareketi
        playerMovement = new PlayerMovement(player, levelPanel, worldWidth, worldHeight, objects);
        levelPanel.addKeyListener(playerMovement);

        // 2) P TUŞU İLE DURAKLATMA/DEVAM
        levelPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    isPaused = !isPaused; // Durumu tersine çevir

                    if (isPaused) {
                        // Oyunu durdur
                        playerMovement.pauseMovement();
                        pausedLabel.setVisible(true);   // "PAUSED" yazısını göster
                        soundManager.stopSound("gameMusic");
                    } else {
                        // Oyunu devam ettir
                        playerMovement.resumeMovement();
                        pausedLabel.setVisible(false);  // "PAUSED" yazısını gizle
                        soundManager.playSound("gameMusic", true);
                    }
                    levelPanel.repaint();
                }
            }
        });

        // 3) PAUSED YAZISINI HAZIRLA (ortaya alıyoruz)
        pausedLabel = new JLabel("PAUSED");
        pausedLabel.setForeground(Color.RED);
        pausedLabel.setFont(new Font("Arial", Font.BOLD, 48));

        // Label’in genişliği ve yüksekliği (tahmini)
        int pausedLabelWidth = 300;
        int pausedLabelHeight = 60;

        // Ekran merkezini hesaplayarak Label’ı konumlandır
        int pausedLabelX = (screenSize.width - pausedLabelWidth) / 2;
        int pausedLabelY = (screenSize.height - pausedLabelHeight) / 2;

        pausedLabel.setBounds(pausedLabelX, pausedLabelY, pausedLabelWidth, pausedLabelHeight);
        pausedLabel.setVisible(false);  // Başlangıçta gizli
        levelPanel.add(pausedLabel);

        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();

        // Kamera ve anahtar kontrol Timer
        updateTimer = new Timer(16, e -> {
            // Oyunu duraklattığımızda da kamera vs. dursun isterseniz:
            if (!isPaused) {
                updateCamera();
                checkKeyCollision(); // Anahtar çarpışmasını kontrol et
                updateEnemies();
                checkEnemyCollisions();
            }
        });
        updateTimer.start();

        frame.pack();
        frame.setVisible(true);
    }

    private void addEnemies(){
        addEnemy(4000, 4500, 250, 350, 3, 2, 4000, 250, 35, 35, new Color(120, 100, 100), 8600, 450);
        addEnemy(5000, 5500, 500, 700, 2, 1, 5000, 600, 35, 35, new Color(120, 100, 100), 8600, 450);
        addEnemy(7000, 7500, 150, 300, 3, 1, 7000, 300, 35, 35, new Color(120, 100, 100), 8600, 450);
        // level 1
        addEnemy(650, 750, 50, 950, 2, 9, 700, 100, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(750, 850, 50, 950, 2, 9, 750, 900, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(850, 950, 50, 950, 2, 9, 850, 100, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(950, 1050, 50, 950, 2, 9, 950, 900, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1050, 1150, 50, 950, 2, 9, 1050, 100, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1150, 1250, 50, 950, 2, 9, 1150, 900, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1250, 1350, 50, 950, 2, 9, 1250, 100, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1350, 1450, 50, 950, 2, 9, 1350, 900, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1450, 1550, 50, 950, 2, 9, 1450, 100, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1550, 1650, 50, 950, 2, 9, 1550, 900, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1650, 1750, 50, 950, 2, 9, 1650, 100, 35, 35, new Color(120, 100, 100), 400, 500);
        addEnemy(1750, 1850, 50, 950, 2, 9, 1750, 900, 35, 35, new Color(120, 100, 100), 400, 500);

    }


    private void addEnemy(int startX, int endX, int startY, int endY, int speedX, int speedY,int x, int y, int width, int height, Color color, int playerStartX, int playerStartY) {
        Enemy enemy = new Enemy(startX, endX, startY, endY, speedX, speedY, x, y, width, height, color, playerStartX, playerStartY);
        enemies.add(enemy);
        levelPanel.add(enemy.getLabel());
    }


    private void addKey(int x, int y) {
        JLabel keyLabel = new JLabel();
        keyLabel.setOpaque(true);
        keyLabel.setBackground(Color.YELLOW);
        keyLabel.setBounds(x, y, 35, 35);

        GameObject keyObject = new GameObject("key", x, y, 35, 35, true);
        objects.add(new GameObjectWrapper(keyLabel, keyObject));
        levelPanel.add(keyLabel);
    }

    private void addPlayer() {
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(new Color(176, 76, 106));
        player.setBounds(400, 500, 35, 35);
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
        if (keyAcquired) return;

        Rectangle playerBounds = player.getBounds();

        for (GameObjectWrapper objectWrapper : objects) {
            if (objectWrapper.object.isKey() && playerBounds.intersects(objectWrapper.object.getBounds())) {
                keyAcquired = true;
                levelPanel.removeAll();
                levelPanel.add(player);
                levelPanel.revalidate();
                levelPanel.repaint();
                System.out.println("Key acquired!");
                soundManager.stopSound("gameMusic");
                break;
            }
        }
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    private void checkEnemyCollisions() {
        Rectangle playerBounds = player.getBounds();

        for (Enemy enemy : enemies) {
            if (enemy.checkCollision(playerBounds)) {
                enemy.resetPlayerPosition(player);
                System.out.println("Enemy collision detected!");
            }
        }
    }


    private void applyResolution(String resolution) {
        String[] parts = resolution.split("x");
        if (parts.length == 2) {
            try {
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                frame.setSize(width, height);
                if (camera != null) { // Kamera nesnesini kontrol et
                    camera.setViewportSize(width, height);
                }
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz çözünürlük formatı.");
            }
        }
    }

    private void applyFullscreen(boolean fullscreen) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (fullscreen) {
            gd.setFullScreenWindow(frame); // Tam ekran moduna al
        } else {
            gd.setFullScreenWindow(null); // Pencere moduna geri dön
            applyResolution(selectedResolution);
        }
    }

    public static void main(String[] args) {
        SoundManager soundManager = new SoundManager();
        new World(soundManager, "1920x1080", true);
    }
}