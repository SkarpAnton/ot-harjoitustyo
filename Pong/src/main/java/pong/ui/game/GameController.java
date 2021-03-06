package pong.ui.game;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pong.game.Field;
import pong.game.Pong;
import pong.rating.RatingHandler;
import pong.ui.menu.endgame.GameEndController;
import pong.ui.menu.endgame.GameEndElements;
import pong.ui.menu.pause.PauseController;
import pong.ui.menu.pause.PauseElements;

/**
 *
 */
public class GameController {

    private final Pong pong;
    private final GameElements elements;
    private final Pane root;
    private final Scene scene;
    private final Stage stage;
    private AnimationTimer timer;

    /**
     * initializes elements and object needed for the game
     */
    public GameController(Boolean twoPlayerGame, int ballSpeed, RatingHandler ranked, Stage stage) {
        this.stage = stage;
        pong = new Pong(ballSpeed, twoPlayerGame, ranked);
        elements = new GameElements();
        root = elements.createGameRoot();
        scene = new Scene(root, Field.getWIDTH(), Field.getHEIGHT());
        scene.getStylesheets().add("css/pane_style.css");
        setKeyEventsToScene();
        createTimer();
    }

    public Scene getScene() {
        return scene;
    }

    private void setKeyEventsToScene() {
        scene.setOnKeyPressed((KeyEvent e) -> {
            pong.getPlayerOne().move(e.getCode());
            pong.getPlayerTwo().move(e.getCode());
            openMenu(e.getCode());
        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            pong.getPlayerOne().stop(e.getCode());
            pong.getPlayerTwo().stop(e.getCode());
        });

    }

    private void openMenu(KeyCode key) {
        if (KeyCode.ESCAPE == key) {
            PauseController pauseController = new PauseController(new PauseElements(), this);
            pauseController.run(stage);
        }
    }

    private void createTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (pong.hasGameEnded()) {
                    gameEnd();
                } else {
                    updateGame();
                }
            }
        };
    }

    /**
     * starts the game
     */
    public void start() {
        timer.start();
    }

    /**
     * stops the game
     */
    public void stop() {
        timer.stop();
    }

    private void gameEnd() {
        stop();
        pong.updateRating();
        if (pong.isPlayerOneWinner()) {
            runEndGameScreen("Player one");
        } else {
            runEndGameScreen("Player two");
        }
    }

    private void runEndGameScreen(String winner) {
        GameEndController gameEndController = new GameEndController(
                new GameEndElements(pong.getPlayerOneScore(),
                        pong.getPlayerTwoScore(), winner));
        gameEndController.run(stage);
    }

    private void updateGame() {
        pong.move();
        elements.getPaddleP1().setY(pong.getPlayerOne().getUpdatedHeight());
        if (pong.isTwoPlayerGame()) {
            elements.getPaddleP2().setY(pong.getPlayerTwo().getUpdatedHeight());
        } else {
            elements.getPaddleP2().setY(pong.getAi().getY());
        }
        updateBall();
        updateScore();
    }

    private void updateBall() {
        elements.getBallElement().setCenterX(pong.getBall().getX());
        elements.getBallElement().setCenterY(pong.getBall().getY());
    }

    private void updateScore() {
        pong.scored();
        elements.getP1ScoreText().setText("P1: " + pong.getPlayerOneScore());
        elements.getP2ScoreText().setText("P2: " + pong.getPlayerTwoScore());
    }
}
