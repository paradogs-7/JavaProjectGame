import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MainMenu {
    private JFrame frame;
    private JPanel panel;
    private SoundManager soundManager;
    private static final String SETTINGS_FILE = "settings.txt";
    private String selectedResolution = "1920x1080";
    private boolean isFullscreen = true;

    public MainMenu() {
        soundManager = new SoundManager();
        soundManager.loadSound("menuMusic", "resources/menumusic.wav");
        soundManager.playSound("menuMusic", true);

        // Ayarları yükle
        loadSettings();

        // Ana pencere oluşturma ve ayarları uygulama
        frame = new JFrame("Diamond Rush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        applyResolution(selectedResolution);
        applyFullscreen(isFullscreen);

        // Panel oluşturma ve arka plan görseli ekleme
        panel = new BackgroundPanel("resources/MainMenuBackground.jpg");
        panel.setLayout(new GridBagLayout());
        frame.add(panel);

        // Butonları ekle
        addButtons();

        // Pencereyi görünür yap
        frame.setVisible(true);
    }

    private void addButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton playButton = createButton("Play", "resources/PlayButton.jpg");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(playButton, gbc);

        JButton optionsButton = createButton("Options", "resources/OptionsButton.jpg");
        optionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSettings();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(optionsButton, gbc);

        JButton quitButton = createButton("Quit", "resources/QuitButton.jpg");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(quitButton, gbc);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(210, 82, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setText("");
        } catch (Exception e) {
            System.out.println("İkon yüklenemedi: " + iconPath);
        }
        button.setPreferredSize(new Dimension(210, 82));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(new Color(220, 220, 220));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        return button;
    }

    private void startGame() {
        frame.dispose();
        soundManager.stopSound("menuMusic");
        new World(soundManager, selectedResolution, isFullscreen);
    }

    private void openSettings() {
        new GameSettings(soundManager);
    }

    private void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("resolution=")) {
                    selectedResolution = line.substring(11);
                } else if (line.startsWith("fullscreen=")) {
                    isFullscreen = Boolean.parseBoolean(line.substring(11));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ayarlar dosyası okunurken hata oluştu veya dosya bulunamadı. Varsayılan ayarlar kullanılacak.");
        }
    }

    private void applyResolution(String resolution) {
        String[] parts = resolution.split("x");
        if (parts.length == 2) {
            try {
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                frame.setSize(width, height);
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
        new MainMenu();
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String filePath) {
            try {
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