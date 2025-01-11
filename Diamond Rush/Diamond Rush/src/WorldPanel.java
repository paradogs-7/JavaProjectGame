import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/
public class WorldPanel extends JPanel {
    private int worldWidth = 10800;
    private int worldHeight = 1080;
    private int currentLevel = 1; // Sadece bir kez tanımlandı
    private JLabel player;
    private Camera camera;
    private ArrayList<GameObjectWrapper> objects;
    private ArrayList<Enemy> enemies;
    private JLabel pausedLabel;
    private boolean doorOpen = false;
    private int keysAcquired = 0;
    private SoundManager soundManager;
    private PlayerMovement playerMovement;
    private boolean resetFlag = false;

    // Kapı ve anahtar referansları
    private GameObjectWrapper door;
    private GameObjectWrapper key;

    private static final String SETTINGS_FILE = "settings.txt";

    // Yeni eklenen SpawnPointManager referansı
    private SpawnPointManager spawnPointManager;

    public WorldPanel(SoundManager soundManager, int lastLevel, SpawnPointManager spawnPointManager) {
        this.soundManager = soundManager;
        this.spawnPointManager = spawnPointManager;  // constructor'dan aldığımız manager'ı saklıyoruz
        this.currentLevel = lastLevel;               // Mevcut level'i alıyoruz

        setLayout(null);
        setPreferredSize(new Dimension(worldWidth, worldHeight));
        setBounds(0, 0, worldWidth, worldHeight);

        objects = new ArrayList<>();
        enemies = new ArrayList<>();

        // Oyuncuyu ekle
        addPlayer();

        // Kamera ayarla (ekran boyutu için)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        camera = new Camera(screenSize.width, screenSize.height, worldWidth, worldHeight);

        // PAUSED yazısı
        pausedLabel = new JLabel("PAUSED");
        pausedLabel.setForeground(Color.RED);
        pausedLabel.setFont(new Font("Arial", Font.BOLD, 48));
        pausedLabel.setVisible(false);
        add(pausedLabel);

        // Level'ı yükle
        addLevel(currentLevel);

        // Oyuncunun konumunu SpawnPointManager'a göre ayarla
        Point startPoint = spawnPointManager.getSpawnPointForLevel(currentLevel);
        if (startPoint != null) {
            player.setLocation(startPoint.x, startPoint.y);
        }

        // PlayerMovement
        playerMovement = new PlayerMovement(player, this, worldWidth, worldHeight, objects);
        addKeyListener(playerMovement);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Arkaplan rengini boyama
        g.setColor(new Color(255, 153, 0));
        g.fillRect(0, 0, worldWidth, worldHeight);
    }

    /**
     * Oyun her karede güncellendiğinde bu metot çağrılacak.
     */
    public void updateWorld() {
        updateCamera();
        checkKeyCollision();
        checkDoorCollision();
        updateEnemies();
        checkEnemyCollisions();

        if (resetFlag) {
            resetLevel();
            resetFlag = false;
        }
    }

    /**
     * Kamerayı oyuncuya göre güncelle
     */
    private void updateCamera() {
        camera.update(player.getX() + player.getWidth() / 2,
                player.getY() + player.getHeight() / 2);

        // Sabit kamera görünüm boyutu
        int cameraWidth = 1920;
        int cameraHeight = 1080;

        // Kamera sınırlarını aşma
        int x = Math.max(0, Math.min(camera.getX(), worldWidth - cameraWidth));
        int y = Math.max(0, Math.min(camera.getY(), worldHeight - cameraHeight));

        // Paneli kamera ofsetine göre kaydır
        setLocation(-x, -y);
    }

    /**
     * Belirli bir seviye yükleme
     */
    private void addLevel(int level) {
        removeAll();       // Paneldeki tüm bileşenleri sil
        objects.clear();   // Oyun nesneleri listesi temizle
        enemies.clear();   // Düşman listesi temizle

        add(player);       // Oyuncuyu yeniden ekle
        add(pausedLabel);  // Paused yazısını yeniden ekle

        switch (level) {
            case 1:
                addLevel1();
                break;
            case 2:
                addLevel2();
                break;
            case 3:
                addLevel3();
                break;
            case 4:
                addLevel4();
                break;
            default:
                System.out.println("Geçersiz level.");
        }

        // Seviyeye düşmanlar ekle
        addEnemies();

        // KeyListener'ı tekrar ekleyin (removeAll() bunu silmişti)
        addKeyListener(playerMovement);

        revalidate();
        repaint();
    }

    /**
     * Level 1'in engelleri, kapısı, anahtarı
     */
    private void addLevel1() {
        // Anahtar
        key = addKey(1000, 500);

        // Engeller / objeler
        addObject(0, 50, 50, 980, new Color(21, 64, 77), "Obstacle", false);
        addObject(0, 0, 2400, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(0, 1030, 2400, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(50, 630, 400, 400, new Color(50, 205, 50), "Obstacle", false);
        addObject(50, 50, 400, 400, new Color(50, 205, 50), "Obstacle", false);
        addObject(2000, 50, 400, 400, new Color(50, 205, 50), "Obstacle", false);
        addObject(2000, 630, 400, 400, new Color(50, 205, 50), "Obstacle", false);

        // Kapı
        door = addDoor(2300, 500);
    }

    /**
     * Level 2
     */
    private void addLevel2() {
        key = addKey(3000, 500);

        addObject(2900, 0, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(2900, 1030, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(2900, 730, 1300, 300, new Color(180, 0, 0), "Obstacle", false);
        addObject(2900, 50, 1300, 300, new Color(180, 0, 0), "Obstacle", false);

        door = addDoor(4700, 500);
    }

    /**
     * Level 3
     */
    private void addLevel3() {
        key = addKey(6000, 600);

        addObject(5320, 0, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(5320, 1030, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(5320, 50, 50, 780, new Color(21, 64, 77), "Obstacle", false);
        addObject(5370, 250, 450, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(6220, 250, 450, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(7190, 50, 50, 780, new Color(21, 64, 77), "Obstacle", false);
        addObject(6990, 250, 200, 580, new Color(21, 64, 77), "Obstacle", false);

        door = addDoor(7300, 500);
    }

    /**
     * Level 4
     */
    private void addLevel4() {
        key = addKey(8000, 200);

        addObject(7740, 0, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(7740, 1030, 1920, 50, new Color(21, 64, 77), "Obstacle", false);
        addObject(8124, 50, 384, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(8892, 50, 384, 580, new Color(21, 64, 77), "Obstacle", false);
        addObject(9610, 50, 50, 980, new Color(21, 64, 77), "Obstacle", false);
        addObject(7740, 50, 50, 780, new Color(21, 64, 77), "Obstacle", false);

        door = addDoor(9500, 500);
    }

    /**
     * Seviyeye göre düşmanları ekle
     */
    private void addEnemies() {
        switch (currentLevel) {
            case 1:
                addEnemiesLevel1();
                break;
            case 2:
                addEnemiesLevel2();
                break;
            case 3:
                addEnemiesLevel3();
                break;
            case 4:
                addEnemiesLevel4();
                break;
            default:
                System.out.println("Geçersiz level değeri.");
        }
    }

    /**
     * Level 1 düşmanları
     */
    private void addEnemiesLevel1() {
        addEnemy(650, 750, 50, 950, 2, 9, 700, 100, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(750, 850, 50, 950, 2, 9, 750, 900, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(850, 950, 50, 950, 2, 9, 850, 100, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(950, 1050, 50, 950, 2, 9, 950, 900, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1050, 1150, 50, 950, 2, 9, 1050, 100, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1150, 1250, 50, 950, 2, 9, 1150, 900, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1250, 1350, 50, 950, 2, 9, 1250, 100, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1350, 1450, 50, 950, 2, 9, 1350, 900, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1450, 1550, 50, 950, 2, 9, 1450, 100, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1550, 1650, 50, 950, 2, 9, 1550, 900, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1650, 1750, 50, 950, 2, 9, 1650, 100, 35, 35, new Color(120, 100, 100), 600, 500);
        addEnemy(1750, 1850, 50, 950, 2, 9, 1750, 900, 35, 35, new Color(120, 100, 100), 600, 500);
    }

    /**
     * Level 2 düşmanları
     */
    private void addEnemiesLevel2() {
        addEnemy(2900, 4150, 400, 500, 13, 5, 2900, 500, 35, 35, new Color(120, 100, 100), 3000, 500);
        addEnemy(2900, 4150, 500, 600, 13, 5, 4150, 600, 35, 35, new Color(120, 100, 100), 3000, 500);
        addEnemy(2900, 4150, 600, 700, 13, 5, 2900, 700, 35, 35, new Color(120, 100, 100), 3000, 500);

        addEnemy(4200, 4400, 50, 950, 5, 14, 1750, 900, 35, 35, new Color(120, 100, 100), 3000, 500);
        addEnemy(4500, 4700, 50, 950, 5, 14, 1750, 100, 35, 35, new Color(120, 100, 100), 3000, 500);
    }

    /**
     * Level 3 düşmanları
     */
    private void addEnemiesLevel3() {
        addEnemy(5900, 6150, 50, 950, 5, 14, 1750, 100, 35, 35, new Color(120, 100, 100), 6000, 500);
        addEnemy(5900, 6150, 50, 950, 5, 14, 1750, 900, 35, 35, new Color(120, 100, 100), 6000, 500);

        addEnemy(5400, 7000, 850, 1000, 13, 5, 2900, 800, 35, 35, new Color(120, 100, 100), 6000, 500);
        addEnemy(5400, 7000, 50, 225, 13, 5, 2900, 800, 35, 35, new Color(120, 100, 100), 6000, 500);

        addEnemy(6725, 6925, 50, 950, 5, 14, 1750, 100, 35, 35, new Color(120, 100, 100), 6000, 500);
        addEnemy(6725, 6925, 50, 950, 5, 14, 1750, 900, 35, 35, new Color(120, 100, 100), 6000, 500);
    }

    /**
     * Level 4 düşmanları
     */
    private void addEnemiesLevel4() {
        addEnemy(7800, 8000, 50, 950, 5, 16, 1750, 100, 35, 35, new Color(120, 100, 100), 8000, 500);
        addEnemy(7800, 8000, 50, 950, 5, 16, 1750, 900, 35, 35, new Color(120, 100, 100), 8000, 500);

        addEnemy(8600, 8800, 50, 950, 5, 16, 1750, 100, 35, 35, new Color(120, 100, 100), 8000, 500);
        addEnemy(8600, 8800, 50, 950, 5, 16, 1750, 900, 35, 35, new Color(120, 100, 100), 8000, 500);

        addEnemy(9350, 9550, 50, 950, 5, 16, 1750, 100, 35, 35, new Color(120, 100, 100), 8000, 500);
        addEnemy(9350, 9550, 50, 950, 5, 16, 1750, 900, 35, 35, new Color(120, 100, 100), 8000, 500);

        addEnemy(7800, 9500, 850, 1000, 18, 5, 7800, 800, 35, 35, new Color(120, 100, 100), 8000, 500);
        addEnemy(7800, 9500, 750, 900, 18, 5, 9500, 800, 35, 35, new Color(120, 100, 100), 8000, 500);
        addEnemy(7800, 9500, 650, 800, 18, 5, 7800, 800, 35, 35, new Color(120, 100, 100), 8000, 500);
    }

    /**
     * Düşman ekleme metodu
     */
    private void addEnemy(int startX, int endX, int startY, int endY,
                          int speedX, int speedY, int x, int y, int width, int height,
                          Color color, int playerStartX, int playerStartY) {

        Enemy enemy = new Enemy(startX, endX, startY, endY, speedX, speedY,
                x, y, width, height, color,
                playerStartX, playerStartY);
        enemies.add(enemy);
        add(enemy.getLabel());
    }

    /**
     * Oyuncunun anahtarla çarpışma kontrolü
     */
    private void checkKeyCollision() {
        Rectangle playerBounds = player.getBounds();

        // "key" tipli bir obje ile çarpışıyor mu?
        Iterator<GameObjectWrapper> iterator = objects.iterator();
        while (iterator.hasNext()) {
            GameObjectWrapper objectWrapper = iterator.next();
            if ("key".equals(objectWrapper.object.getType()) &&
                    playerBounds.intersects(objectWrapper.object.getBounds())) {

                keysAcquired++;
                System.out.println("Key acquired!");
                // Panelden ve objects listesinden sil
                remove(objectWrapper.label);
                iterator.remove();

                if (keysAcquired > 0) {
                    doorOpen = true; // Kapı artık açılabilir
                }
                break;
            }
        }
    }

    /**
     * Kapıyla çarpışma ve seviye geçişi kontrolü
     */
    private void checkDoorCollision() {
        if (!doorOpen) return;

        Rectangle playerBounds = player.getBounds();
        for (GameObjectWrapper objectWrapper : objects) {
            if ("door".equals(objectWrapper.object.getType()) &&
                    playerBounds.intersects(objectWrapper.object.getBounds())) {

                if (currentLevel < 4) {
                    currentLevel++;
                    doorOpen = false;
                    keysAcquired = 0;
                    addLevel(currentLevel);

                    System.out.println("Level geçildi, yeni level: " + currentLevel);

                    // Level bilgisini settings.txt'ye kaydet
                    saveLevelToFile(currentLevel);

                    // Müzik tekrar çalınabilir
                    soundManager.playSound("gameMusic", true);
                } else {
                    // 4. seviyedeyiz ve kapıya ulaştık, oyunu bitir
                    showGameOver();
                }
                break;
            }
        }
    }

    /**
     * Son level kapıya ulaştığında oyunu bitirir ve ana menüye döner.
     */
    private void showGameOver() {
        // Son level kapıya değince
        JOptionPane.showMessageDialog(this, "Congratulations, Game Over!");

        // Ayar dosyasına level=1 yazarak oyunu ilk seviyeye sıfırlar
        saveLevelToFile(1);

        // Ana menüye dön
        SwingUtilities.getWindowAncestor(this).dispose(); // Mevcut GameFrame'i kapat
        soundManager.stopSound("gameMusic");
        new MainMenu();  // Yeni Ana Menü aç
    }

    /**
     * Düşmanların hareketini güncelle
     */
    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    /**
     * Düşmanla çarpışma kontrolü
     */
    private void checkEnemyCollisions() {
        Rectangle playerBounds = player.getBounds();
        boolean collisionDetected = false;

        for (Enemy enemy : enemies) {
            if (enemy.checkCollision(playerBounds)) {
                collisionDetected = true;
                break;
            }
        }
        if (collisionDetected) {
            resetFlag = true;
            System.out.println("Enemy collision detected!");
        }
    }

    /**
     * Seviye sıfırlama (oyuncuyu başa almak, anahtarları, kapıyı resetlemek)
     */
    private void resetLevel() {
        keysAcquired = 0;
        doorOpen = false;

        addLevel(currentLevel);

        // Yeni level yüklendikten sonra spawn noktası:
        Point startPoint = spawnPointManager.getSpawnPointForLevel(currentLevel);
        if (startPoint != null) {
            player.setLocation(startPoint.x, startPoint.y);
        }
    }

    /**
     * Oyuncu etiketini oluşturup panele ekleme
     */
    private void addPlayer() {
        player = new JLabel();
        player.setOpaque(true);
        player.setBackground(new Color(176, 76, 106));
        player.setBounds(600, 500, 35, 35);
        add(player);
    }

    /**
     * Kapı ekleme
     */
    private GameObjectWrapper addDoor(int x, int y) {
        JLabel doorLabel = new JLabel();
        doorLabel.setOpaque(true);
        doorLabel.setBackground(new Color(0, 150, 200)); // Mavi
        doorLabel.setBounds(x, y, 35, 100);

        GameObject doorObject = new GameObject("door", x, y, 35, 100, false, "door");
        GameObjectWrapper doorWrapper = new GameObjectWrapper(doorLabel, doorObject);

        objects.add(doorWrapper);
        add(doorLabel);
        return doorWrapper;
    }

    /**
     * Anahtar ekleme
     */
    private GameObjectWrapper addKey(int x, int y) {
        JLabel keyLabel = new JLabel();
        keyLabel.setOpaque(true);
        keyLabel.setBackground(Color.YELLOW);
        keyLabel.setBounds(x, y, 35, 35);

        GameObject keyObject = new GameObject("key", x, y, 35, 35, true, "key");
        GameObjectWrapper keyWrapper = new GameObjectWrapper(keyLabel, keyObject);

        objects.add(keyWrapper);
        add(keyLabel);
        return keyWrapper;
    }

    /**
     * Engeller veya farklı tipte nesneler eklemek için
     */
    private void addObject(int x, int y, int width, int height,
                           Color color, String name, boolean isKey) {
        JLabel newLabel = new JLabel();
        newLabel.setOpaque(true);
        newLabel.setBackground(color);
        newLabel.setBounds(x, y, width, height);

        GameObject newObject = new GameObject(name, x, y, width, height, isKey, "obstacle");
        GameObjectWrapper wrapper = new GameObjectWrapper(newLabel, newObject);

        objects.add(wrapper);
        add(newLabel);
    }

    /**
     * Duraklatma yazısını göstermek veya gizlemek için
     */
    public void showPausedLabel(boolean show) {
        pausedLabel.setVisible(show);
    }

    /**
     * Level bilgisini settings.txt'ye kaydeden yardımcı metot
     */
    private void saveLevelToFile(int level) {
        try {
            // Önce mevcut ayarları okuyalım, satır satır “level=” olanı değiştireceğiz
            java.util.Properties props = new java.util.Properties();
            try (FileReader fr = new FileReader(SETTINGS_FILE)) {
                props.load(fr);
            } catch (IOException e) {
                // Dosya yoksa sorun değil, yeniden oluşturacağız
            }

            // level güncelle
            props.setProperty("level", String.valueOf(level));

            // Tekrar yaz
            try (FileWriter fw = new FileWriter(SETTINGS_FILE)) {
                props.store(fw, "Updated game settings");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
