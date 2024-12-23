import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;

public class GameSettings {

    private JFrame frame;
    private JPanel settingsPanel;
    private JSlider volumeSlider;
    private JComboBox<String> resolutionDropdown;
    private JCheckBox fullscreenCheckbox;
    private SoundManager soundManager;
    private static final String SETTINGS_FILE = "settings.txt";
    private String selectedResolution = "1920x1080";
    private boolean isFullscreen = true;

    public GameSettings(SoundManager soundManager) {
        this.soundManager = soundManager;

        // Ayarlar penceresi oluşturma
        frame = new JFrame("Diamond Rush - Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);

        // Ayarları yükle
        loadSettings();

        // Tam ekran modu (varsayılan olarak tam ekran)
        applyFullscreen(isFullscreen);
        // Tam ekran modu(başlangıçta da tam ekran olması için constructor'a eklendi)

        // Ayarlar panelini oluştur
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        settingsPanel.setBackground(new Color(45, 45, 45));
        frame.add(settingsPanel);

        // Ayar bileşenlerini ekle
        addSettingsComponents();

        // Pencereyi görünür yap
        frame.setVisible(true);
    }

    private void addSettingsComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Genel yazı tipi ve renk ayarları
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color foregroundColor = new Color(220, 220, 220);

        // Ses Ayarı
        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setFont(labelFont);
        volumeLabel.setForeground(foregroundColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        settingsPanel.add(volumeLabel, gbc);

        volumeSlider = new JSlider(0, 100, (int) (soundManager.getVolume() * 100));
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setBackground(new Color(45, 45, 45));
        volumeSlider.setForeground(foregroundColor);
        gbc.gridx = 1;
        gbc.gridy = 0;
        settingsPanel.add(volumeSlider, gbc);

        // Çözünürlük Ayarı
        JLabel resolutionLabel = new JLabel("Resolution:");
        resolutionLabel.setFont(labelFont);
        resolutionLabel.setForeground(foregroundColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        settingsPanel.add(resolutionLabel, gbc);

        String[] resolutions = {"800x600", "1024x768", "1280x720", "1920x1080"};
        resolutionDropdown = new JComboBox<>(resolutions);
        resolutionDropdown.setSelectedItem(selectedResolution); // Kaydedilen çözünürlüğü seç
        resolutionDropdown.setBackground(new Color(60, 60, 60));
        resolutionDropdown.setForeground(foregroundColor);
        gbc.gridx = 1;
        gbc.gridy = 1;
        settingsPanel.add(resolutionDropdown, gbc);

        // Tam Ekran Ayarı
        JLabel fullscreenLabel = new JLabel("Fullscreen:");
        fullscreenLabel.setFont(labelFont);
        fullscreenLabel.setForeground(foregroundColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        settingsPanel.add(fullscreenLabel, gbc);

        fullscreenCheckbox = new JCheckBox();
        fullscreenCheckbox.setSelected(isFullscreen); // Kaydedilen tam ekran durumunu seç
        fullscreenCheckbox.setBackground(new Color(45, 45, 45));
        fullscreenCheckbox.setForeground(foregroundColor);
        gbc.gridx = 1;
        gbc.gridy = 2;
        settingsPanel.add(fullscreenCheckbox, gbc);

        // Kaydet Butonu
        JButton saveButton = new JButton("Save Settings");
        saveButton.setBackground(new Color(70, 70, 70));
        saveButton.setForeground(foregroundColor);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        settingsPanel.add(saveButton, gbc);

        // Exit Butonu
        JButton exitButton = new JButton();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/ExitButton.jpg");
            if (inputStream != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(inputStream));
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                exitButton.setIcon(new ImageIcon(scaledImage));
            } else {
                System.out.println("Görsel yüklenemedi: resources/ExitButton.jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(50, 50));

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        settingsPanel.add(exitButton, gbc);
    }

    private void saveSettings() {
        int volume = volumeSlider.getValue();
        selectedResolution = (String) resolutionDropdown.getSelectedItem();
        isFullscreen = fullscreenCheckbox.isSelected();

        // Ses seviyesini ayarla
        soundManager.setVolume(volume / 100f);

        // Çözünürlüğü ayarla
        applyResolution(selectedResolution);
        applyFullscreen(isFullscreen);

        saveSettingsToFile(); // Ayarları dosyaya kaydet

        // Test amaçlı çıktı
        System.out.println("Settings Saved:");
        System.out.println("Volume: " + volume);
        System.out.println("Resolution: " + selectedResolution);
        System.out.println("Fullscreen: " + isFullscreen);

        JOptionPane.showMessageDialog(frame, "Settings have been saved!");
    }
    private void saveSettingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
            writer.write("volume=" + (int) (soundManager.getVolume() * 100) + "\n");
            writer.write("resolution=" + selectedResolution + "\n");
            writer.write("fullscreen=" + isFullscreen + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("volume=")) {
                    int volume = Integer.parseInt(line.substring(7));
                    soundManager.setVolume(volume / 100f);
                } else if (line.startsWith("resolution=")) {
                    selectedResolution = line.substring(11);
                } else if (line.startsWith("fullscreen=")) {
                    isFullscreen = Boolean.parseBoolean(line.substring(11));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ayarlar dosyası okunurken hata oluştu veya dosya bulunamadı. Varsayılan ayarlar kullanılacak.");
        }
    }

    // Çözünürlüğü uygula
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
        SoundManager soundManager = new SoundManager();
        new GameSettings(soundManager);
    }
}