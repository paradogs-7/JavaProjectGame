import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class GameSettings {

    private JFrame frame;
    private JPanel settingsPanel;
    private JSlider volumeSlider;
    private JComboBox<String> resolutionDropdown;
    private JCheckBox fullscreenCheckbox;

    // Constructor: Ayarlar ekranını başlatır
    public GameSettings() {
        // Ayarlar penceresi oluşturma
        frame = new JFrame("Diamond Rush - Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sadece bu pencereyi kapat
        frame.setUndecorated(true); // Çerçeveyi kaldır
        frame.setResizable(false);

        // Tam ekran modu
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        // Ayarlar panelini oluştur
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        settingsPanel.setBackground(new Color(45, 45, 45)); // Koyu arka plan rengi
        frame.add(settingsPanel);

        // Ayar bileşenlerini ekle
        addSettingsComponents();

        // Pencereyi görünür yap
        frame.setVisible(true);
    }

    // Ayar bileşenlerini ekleme
    private void addSettingsComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Genel yazı tipi ve renk ayarları
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color foregroundColor = new Color(220, 220, 220); // Açık gri metin rengi

        // Ses Ayarı
        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setFont(labelFont);
        volumeLabel.setForeground(foregroundColor); // Metin rengi
        gbc.gridx = 0;
        gbc.gridy = 0;
        settingsPanel.add(volumeLabel, gbc);

        volumeSlider = new JSlider(0, 100, 50); // Min: 0, Max: 100, Varsayılan: 50
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setBackground(new Color(45, 45, 45)); // Koyu arka plan
        volumeSlider.setForeground(foregroundColor); // Açık gri metin
        gbc.gridx = 1;
        gbc.gridy = 0;
        settingsPanel.add(volumeSlider, gbc);

        // Çözünürlük Ayarı
        JLabel resolutionLabel = new JLabel("Resolution:");
        resolutionLabel.setFont(labelFont);
        resolutionLabel.setForeground(foregroundColor); // Metin rengi
        gbc.gridx = 0;
        gbc.gridy = 1;
        settingsPanel.add(resolutionLabel, gbc);

        String[] resolutions = {"800x600", "1024x768", "1280x720", "1920x1080"};
        resolutionDropdown = new JComboBox<>(resolutions);
        resolutionDropdown.setBackground(new Color(60, 60, 60)); // Koyu gri arka plan
        resolutionDropdown.setForeground(foregroundColor); // Açık gri metin
        gbc.gridx = 1;
        gbc.gridy = 1;
        settingsPanel.add(resolutionDropdown, gbc);

        // Tam Ekran Ayarı
        JLabel fullscreenLabel = new JLabel("Fullscreen:");
        fullscreenLabel.setFont(labelFont);
        fullscreenLabel.setForeground(foregroundColor); // Metin rengi
        gbc.gridx = 0;
        gbc.gridy = 2;
        settingsPanel.add(fullscreenLabel, gbc);

        fullscreenCheckbox = new JCheckBox();
        fullscreenCheckbox.setBackground(new Color(45, 45, 45)); // Koyu arka plan
        fullscreenCheckbox.setForeground(foregroundColor); // Açık metin
        gbc.gridx = 1;
        gbc.gridy = 2;
        settingsPanel.add(fullscreenCheckbox, gbc);

        // Kaydet Butonu
        JButton saveButton = new JButton("Save Settings");
        saveButton.setBackground(new Color(70, 70, 70)); // Koyu buton arka plan
        saveButton.setForeground(foregroundColor); // Açık metin
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
            // Görseli yükle
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/ExitButton.jpg");
            if (inputStream != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(inputStream));
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                exitButton.setIcon(new ImageIcon(scaledImage)); // Butona görseli ekle
            } else {
                System.out.println("Görsel yüklenemedi: resources/ExitButton.jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        exitButton.setBorderPainted(false); // Çerçeve kaldır
        exitButton.setContentAreaFilled(false); // Arka plan kaldır
        exitButton.setFocusPainted(false); // Odak çizgileri kaldır
        exitButton.setPreferredSize(new Dimension(50, 50)); // 1:1 ölçekte buton boyutu

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Ayarlar penceresini kapat
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4; // Exit butonu en alta eklenir
        gbc.gridwidth = 2; // Ortalamak için tüm sütunları kapsar
        settingsPanel.add(exitButton, gbc);
    }

    // Ayarları kaydetme işlemi
    private void saveSettings() {
        int volume = volumeSlider.getValue();
        String resolution = (String) resolutionDropdown.getSelectedItem();
        boolean fullscreen = fullscreenCheckbox.isSelected();

        // Seçilen ayarları konsola yazdır (test amaçlı)
        System.out.println("Settings Saved:");
        System.out.println("Volume: " + volume);
        System.out.println("Resolution: " + resolution);
        System.out.println("Fullscreen: " + fullscreen);

        // İleride ayarları dosyaya kaydetmek veya uygulamak için kod eklenebilir
        JOptionPane.showMessageDialog(frame, "Settings have been saved!");
    }

    // Main metodu (GameSettings çalıştırmak için)
    public static void main(String[] args) {
        new GameSettings();
    }
}
