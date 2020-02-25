package com.github.bsfowlie.gameloop;

public class GameLoop<T> {

    private final Game<T> game;

    private final InputHandler<T> inputHandler;

    private final GameClock gameClock;

    public GameLoop(final Game<T> game,
                    final InputHandler<T> inputHandler,
                    final GameClock gameClock
    )
    {

        this.game = game;
        this.inputHandler = inputHandler;
        this.gameClock = gameClock;
    }

    public void run() {

        int previousTick = gameClock.getCurrentTick();
        int lag = 0;
        while (game.isRunning()) {
            final int currentTick = gameClock.getCurrentTick();
            lag += currentTick - previousTick;
            T input = inputHandler.getInput();
            while (lag >= GameClock.FRAME_DURATION) {
                game.update(input);
                input = null;
                lag -= GameClock.FRAME_DURATION;
            }
            game.render();
            previousTick = currentTick;
        }
    }

}
