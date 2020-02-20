package com.github.bsfowlie.gameloop;

public class GameLoop {

    private final Game game;

    public GameLoop(final Game game) {

        this.game = game;
    }

    public void run() {

        if (game.isRunning()) {
            game.update();
        }
    }

}