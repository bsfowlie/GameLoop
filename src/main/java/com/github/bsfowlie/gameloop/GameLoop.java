package com.github.bsfowlie.gameloop;

public class GameLoop<T> {

    private final Game<T> game;

    private final InputHandler<T> inputHandler;

    public GameLoop(final Game<T> game, final InputHandler<T> inputHandler) {

        this.game = game;
        this.inputHandler = inputHandler;
    }

    public void run() {

        while (game.isRunning()) {
            T input = inputHandler.getInput();
            game.update(input);
            game.render();
        }
    }

}
