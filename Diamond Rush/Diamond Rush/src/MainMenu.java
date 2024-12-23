import javax.swing.*;  // Swing kütüphanesi
import java.awt.*;     // Layout ve grafik öğeleri
import java.awt.event.*;  // Butonlar için olay dinleyiciler
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu {
    private JFrame frame;
    private JPanel panel;
    private SoundManager soundManager;

    // Constructor: Ana pencere ve menü elemanlarını oluşturur
    public MainMenu() {
        soundManager = new SoundManager();
        soundManager.loadSound("menuMusic", "resources/menumusic.wav");
        soundManager.playSound("menuMusic", true);

        // Ana pencere oluşturma
        frame = new JFrame("Diamond Rush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);

        // Ekran çözünürlüğünü al ve pencereyi tam ekran yap
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocation(0, 0);

        // Panel oluşturma ve arka plan görseli ekleme
        panel = new BackgroundPanel("resources/MainMenuBackground.jpg");
        panel.setLayout(new GridBagLayout());  // Düzen yerleşimi
        frame.add(panel);

        // Butonları ekle
        addButtons();

        // Pencereyi görünür yap
        frame.setVisible(true);
    }

    // Butonları oluştur ve panel üzerine ekle
    private void addButtons() {
        // GridBagLayout için konumlandırma
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Butonlar arası boşluk

        // Play Butonu
        JButton playButton = createButton("Play", "resources/PlayButton.jpg");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();  // Play tuşu ile oyunu başlat
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(playButton, gbc);

        // Options Butonu
        JButton optionsButton = createButton("Options", "resources/OptionsButton.jpg");
        optionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSettings();  // Options tuşu ile ayarları aç
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(optionsButton, gbc);

        // Quit Butonu
        JButton quitButton = createButton("Quit", "resources/QuitButton.jpg");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Quit tuşu ile çıkış yap
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(quitButton, gbc);
    }

    // Özel buton oluşturma metodu (ikon desteği ile)
    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);

        try {
            // İkon dosyasını yükle
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(210, 82, Image.SCALE_SMOOTH); // İkonu boyutlandır
            button.setIcon(new ImageIcon(scaledIcon)); // Butona ikonu ekle
            button.setText(""); // Metni kaldır (sadece ikon görünsün isterseniz)
        } catch (Exception e) {
            System.out.println("İkon yüklenemedi: " + iconPath);
        }

        button.setPreferredSize(new Dimension(210, 82)); // Buton boyutu
        button.setFocusPainted(false); // Odak çizgilerini kaldır
        button.setBackground(new Color(70, 70, 70)); // Koyu arka plan
        button.setForeground(new Color(220, 220, 220)); // Açık metin
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Metin fontu ve boyutu
        button.setHorizontalTextPosition(SwingConstants.CENTER); // Metni ikonun üzerine yerleştir
        button.setVerticalTextPosition(SwingConstants.CENTER); // Metni ikonun ortasına yerleştir
        return button;
    }

    private void startGame() {
        frame.dispose(); // Ana menü penceresini kapat
        soundManager.stopSound("menuMusic");
        new World(soundManager);   // World sınıfını başlat
    }

    private void openSettings() {
        new GameSettings(soundManager); // GameSettings'i başlat
    }

    // Programın giriş noktası
    public static void main(String[] args) {
        new MainMenu();  // Ana menüyü başlat
    }

    // Arka plan görseli için özel bir JPanel sınıfı
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            try {
                // Kaynak dosyasından arka plan görselini yükle
                backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Arka plan görseli yüklenemedi: " + filePath);
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Görsel dosyası bulunamadı: " + filePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}