import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Properties;



/*

Ulaş Şahin — 220610027

Volkan Yılmaz — 220610006

Göktürk Can — 230611501

Emirhan Keven — 220610037

*/

/*
yapılacaklar
------------------------------------------------------------
Create player movement and controls.                        ok
Implement level designs.                                    ok
Develop checkpoint system.                                  ok
Add enemy AI and interactions.                              ok
Design menu system (start game, load game, credits).        ok
Implement save and load functionality.                      ok
Create visual assets and animations.                        ok
Add sound effects and background music.                     ok
Optimize game performance.                                  ok
Test gameplay mechanics.                                    ok
Perform bug fixes.                                          ok
------------------------------------------------------------

 son revize de tamamlandı


 */
public class MainMenu {



    String CONTINUE_BUTON_YERI = "resources/ContinueButton.jpg"; // KOYDUGUN BUTONUN RESMI BU YOLA YAPISTIR. TIRNAĞIN İÇİNE KOY
    String NEW_GAME_BUTON_YERI = "resources/PlayButton.jpg";




    private JFrame frame;
    private JPanel panel;
    private SoundManager soundManager;
    private static final String SETTINGS_FILE = "settings.txt";

    private String selectedResolution = "1920x1080";
    private boolean isFullscreen = true;
    public int lastLevel;

    public MainMenu() {
        soundManager = new SoundManager();
        soundManager.loadSound("menuMusic", "resources/menumusic.wav");
        soundManager.playSound("menuMusic", true);

        loadSettings();

        frame = new JFrame("Diamond Rush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        applyResolution(selectedResolution);
        applyFullscreen(isFullscreen);

        panel = new BackgroundPanel("resources/MainMenuBackground.jpg");
        panel.setLayout(new GridBagLayout());
        frame.add(panel);

        addButtons();

        frame.setVisible(true);
    }

    private void addButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // CONTINUE Butonu
        JButton continueButton = createButton("Continue", CONTINUE_BUTON_YERI);
        // "Başlatılacak seviye" yazısını gösterecek etiket
        JLabel levelLabel = new JLabel("Current Level: " + lastLevel);

        if (lastLevel == 1) {
            // Eğer level 1 ise, devam butonu ve etiketi görünmesin
            continueButton.setVisible(false);
            levelLabel.setVisible(false);
        } else {
            continueButton.setVisible(true);
            levelLabel.setVisible(true);
        }

        // Continue butonu: lastLevel’dan devam
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameFromLastLevel();
            }
        });

        // Continue butonunu grid'in ilk satırına ekleyelim
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(continueButton, gbc);

        // Yanına da seviye etiketini ekleyelim (aynı satır, x = 1)
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(levelLabel, gbc);

        // PLAY Butonu: her zaman level 1’den başlar
        JButton playButton = createButton("Play", NEW_GAME_BUTON_YERI);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameFromLevel1();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(playButton, gbc);

        // OPTIONS Butonu
        JButton optionsButton = createButton("Options", "resources/OptionsButton.jpg");
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettings();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(optionsButton, gbc);

        // QUIT Butonu
        JButton quitButton = createButton("Quit", "resources/QuitButton.jpg");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(quitButton, gbc);
    }

    /**
     * Play ve Continue butonlarını aynı boyutta yapmak için
     * ortak bir 'createButton' metodu kullanıyoruz.
     */
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
        button.setBackground(new Color(255, 0, 0));
        button.setForeground(new Color(220, 220, 220));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        return button;
    }

    /**
     * Continue butonuna tıklayınca, kaydedilmiş level (lastLevel) ile oyuna başlar
     */
    private void startGameFromLastLevel() {
        frame.dispose();
        soundManager.stopSound("menuMusic");
        new GameFrame(selectedResolution, isFullscreen, soundManager, lastLevel);
    }

    /**
     * Play butonuna tıklayınca, her zaman level 1’den başlatır
     */
    private void startGameFromLevel1() {
        frame.dispose();
        soundManager.stopSound("menuMusic");
        new GameFrame(selectedResolution, isFullscreen, soundManager, 1);
    }

    private void openSettings() {
        new GameSettings(soundManager);
    }

    private void loadSettings() {
        Properties props = new Properties();
        try (FileReader reader = new FileReader(SETTINGS_FILE)) {
            props.load(reader);
            selectedResolution = props.getProperty("resolution", "1920x1080");
            isFullscreen = Boolean.parseBoolean(props.getProperty("fullscreen", "true"));
            lastLevel = Integer.parseInt(props.getProperty("level", "1"));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ayarlar dosyası okunurken hata oluştu veya dosya yok. Varsayılan ayarlar kullanılacak.");
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
            gd.setFullScreenWindow(frame);
        } else {
            gd.setFullScreenWindow(null);
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
