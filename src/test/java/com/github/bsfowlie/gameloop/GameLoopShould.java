package com.github.bsfowlie.gameloop;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameLoop should")
public final class GameLoopShould {

    @Mock Game<AnyTestInput> testGame;

    @Mock InputHandler<AnyTestInput> testInputHandler;

    @Mock GameClock testClock;

    private GameLoop<AnyTestInput> loop;

    @BeforeEach
    public void setUpGameLoop() {

        loop = new GameLoop<>(testGame, testInputHandler, testClock);
    }

    @Test
    @DisplayName("do nothing if game is not running")
    public void doNothingIfGameIsNotRunning() {

        given(testClock.getCurrentTick()).willReturn(0);
        given(testGame.isRunning()).willReturn(false);

        loop.run();

        then(testGame).should(times(0)).update(any());
    }

    @Test
    @DisplayName("invoke update once if game is running")
    public void invokeUpdateOnceIfGameIsRunning() {

        given(testClock.getCurrentTick()).willReturn(0, GameClock.FRAME_DURATION);
        given(testGame.isRunning()).willReturn(true, false);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        then(testGame).should(atLeastOnce()).update(any());
    }

    @Test
    @DisplayName("invoke update as long as game is running")
    public void invokeUpdateAsLongAsGameIsRunning() {

        given(testClock.getCurrentTick()).willReturn(0,
                GameClock.FRAME_DURATION,
                2 * GameClock.FRAME_DURATION,
                3 * GameClock.FRAME_DURATION);
        given(testGame.isRunning()).willReturn(true, true, true, false);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        then(testGame).should(times(3)).update(any());
    }

    @Test
    @DisplayName("invoke render after update")
    public void invokeRenderAfterUpdate() {

        given(testClock.getCurrentTick()).willReturn(0, GameClock.FRAME_DURATION);
        given(testGame.isRunning()).willReturn(true, false);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        InOrder inOrder = inOrder(testGame);
        then(testGame).should(inOrder, atLeastOnce()).update(any());
        then(testGame).should(inOrder, atLeastOnce()).render();
    }

    @Test
    @DisplayName("pass user input to update")
    public void passUserInputToUpdate() {

        given(testClock.getCurrentTick()).willReturn(0, GameClock.FRAME_DURATION);
        given(testGame.isRunning()).willReturn(true, false);

        final AnyTestInput input = new AnyTestInput();
        given(testInputHandler.getInput()).willReturn(input);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        then(testGame).should(times(1)).update(same(input));
    }

    @Test
    @DisplayName("skip update if loop is too fast")
    public void skipUpdateIfLoopIsTooFast() {

        given(testClock.getCurrentTick()).willReturn(0, 1);
        given(testGame.isRunning()).willReturn(true, false);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        then(testGame).should(times(0)).update(any());
        then(testGame).should(times(1)).render();
    }

    @Test
    @DisplayName("do additional updates if loop is too slow")
    public void doAdditionalUpdatesIfLoopIsTooSlow() {

        given(testClock.getCurrentTick()).willReturn(0, 2 * GameClock.FRAME_DURATION);
        given(testGame.isRunning()).willReturn(true, false);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        then(testInputHandler).should(times(1)).getInput();
        then(testGame).should(times(2)).update(any());
        then(testGame).should(times(1)).render();
    }

    @Test
    @DisplayName("catch up on updates if fast loop slows")
    public void catchUpOnUpdatesIfFastLoopSlowsDown() {

        given(testClock.getCurrentTick()).willReturn(0, 15, 2 * GameClock.FRAME_DURATION + 5);
        given(testGame.isRunning()).willReturn(true, true, false);

        GameLoop<AnyTestInput> loop = new GameLoop<>(testGame, testInputHandler, testClock);
        loop.run();

        then(testGame).should(times(2)).update(any());
        then(testGame).should(times(2)).render();
    }

    //------------------------------------------------------------------------------------------------------------------
    private static class AnyTestInput {

    }

}

