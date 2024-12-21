public class zGameController {
    private int score;
    private boolean isPaused;

    public void startGame() {
        score = 0;
        isPaused = false;
        System.out.println("Game started!");
        new World(); // Oyunu başlatmak için World sınıfını kullan
    }

    public void pauseGame() {
        isPaused = true;
        System.out.println("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        System.out.println("Game resumed.");
    }
}
