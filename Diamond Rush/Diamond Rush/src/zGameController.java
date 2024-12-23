public class zGameController {
    private int score;
    private boolean isPaused;
    private SoundManager soundManager; // SoundManager'ı ekledik

    public zGameController(SoundManager soundManager) { // SoundManager'ı constructor'a ekledik
        this.soundManager = soundManager;
    }


    public void startGame() {
        score = 0;
        isPaused = false;
        System.out.println("Game started!");
        new World(soundManager); // Oyunu başlatmak için World sınıfını kullan
    }

    public void pauseGame() {
        isPaused = true;
        System.out.println("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        System.out.println("Game resumed.");
    }


    public static void main(String[] args) {
        SoundManager soundManager = new SoundManager();
        zGameController gameController = new zGameController(soundManager); // SoundManager'ı GameController'a yolla
        gameController.startGame();
    }
}