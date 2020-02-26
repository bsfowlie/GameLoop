package com.github.bsfowlie.gameloop;

import static com.github.bsfowlie.gameloop.GameClock.FRAME_DURATION;

public class GameLoop<T> {

    private final Game<T> game;

    private final InputHandler<T> inputHandler;

    private final GameClock gameClock;

    private int previousTick;

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

        previousTick = tick();
        int durationRemaining = 0;

        while (game.isRunning()) {

            final int currentDuration = getDurationBetween(previousTick, tick()) + durationRemaining;
            durationRemaining = updateGameAsNeededFor(currentDuration);
            renderGame();
        }
    }

    private int tick() {

        return gameClock.getCurrentTick();
    }

    private int getDurationBetween(final int lastTick, final int nextTick) {

        try {

            return nextTick - lastTick;
        } finally {

            previousTick = nextTick;
        }
    }

    private int updateGameAsNeededFor(final int duration) {

        int currentDuration = duration;
        if (currentDuration >= FRAME_DURATION) {

            game.update(withCurrentInput());
            currentDuration -= FRAME_DURATION;
            while (currentDuration >= FRAME_DURATION) {

                game.update(withNoInput());
                currentDuration -= FRAME_DURATION;
            }
        }
        return currentDuration;
    }

    private void renderGame() {

        game.render();
    }

    private T withCurrentInput() {

        return inputHandler.getInput();
    }

    private T withNoInput() {

        return null;
    }

}
