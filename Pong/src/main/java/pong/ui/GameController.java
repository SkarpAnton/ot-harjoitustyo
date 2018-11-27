package pong.ui;

import pong.ui.FxElements;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import pong.Field;
import pong.Pong;

public class GameController {

    private final Pong pong = new Pong();
    private final FxElements elements;
    private final Pane root;
    private final Scene scene;

    public GameController() {
        elements = new FxElements();
        root = elements.createGameRoot();
        scene = new Scene(root, Field.getWIDTH(), Field.getHEIGHT());
        scene.getStylesheets().add("paneStyle.css");
        setKeyEventsToScene();
        startTimer();
    }

    public Scene getScene() {
        return scene;
    }

    private void setKeyEventsToScene() {
        scene.setOnKeyPressed((KeyEvent e) -> {
            pong.getPlayerOne().move(e.getCode());
            pong.getPlayerTwo().move(e.getCode());

        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            pong.getPlayerOne().stop(e.getCode());
            pong.getPlayerTwo().stop(e.getCode());
        });
    }

    private void startTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                elements.getPaddleP1().setY(pong.getPlayerOne().getUpdatedHeight());
                elements.getPaddleP2().setY(pong.getPlayerTwo().getUpdatedHeight());
                pong.getBall().move(pong.getPlayerOne().getUpdatedHeight(),
                        pong.getPlayerTwo().getUpdatedHeight());
                pong.getBall().scored();
                elements.getBallElement().setCenterX(pong.getBall().getX());
                elements.getBallElement().setCenterY(pong.getBall().getY());
                elements.getP1ScoreText().setText("P1: " + pong.getBall().getPlayerOneScore());
                elements.getP2ScoreText().setText("P2: " + pong.getBall().getPlayerTwoScore());

            }
        }.start();
    }

}
