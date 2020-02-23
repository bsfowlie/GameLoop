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

    @Mock private Game<AnyTestInput> testGame;
    @Mock private InputHandler<AnyTestInput> testInputHandler;
    private GameLoop<AnyTestInput> loop;

    @BeforeEach
    public void setUpGameLoop() {
        loop = new GameLoop<>(testGame, testInputHandler);
    }

    @Test
    @DisplayName("do nothing if game is not running")
    public void doNothingIfGameIsNotRunning() {

        given(testGame.isRunning()).willReturn(false);

        loop.run();

        then(testGame).should(times(0)).update(any());
    }

    @Test
    @DisplayName("invoke update once if game is running")
    public void invokeUpdateOnceIfGameIsRunning() {

        given(testGame.isRunning()).willReturn(true, false);

        loop.run();

        then(testGame).should(atLeastOnce()).update(any());
    }

    @Test
    @DisplayName("invoke update as long as game is running")
    public void invokeUpdateAsLongAsGameIsRunning() {

        given(testGame.isRunning()).willReturn(true, true, true, false);

        loop.run();

        then(testGame).should(times(3)).update(any());
    }

    @Test
    @DisplayName("invoke render after update")
    public void invokeRenderAfterUpdate() {

        given(testGame.isRunning()).willReturn(true, false);

        loop.run();

        InOrder inOrder = inOrder(testGame);
        then(testGame).should(inOrder, atLeastOnce()).update(any());
        then(testGame).should(inOrder, atLeastOnce()).render();
    }

    @Test
    @DisplayName("pass user input to update")
    public void passUserInputToUpdate() {

        final AnyTestInput input = new AnyTestInput();

        given(testGame.isRunning()).willReturn(true, false);
        given(testInputHandler.getInput()).willReturn(input);

        loop.run();

        then(testGame).should(times(1)).update(same(input));
    }

    private static class AnyTestInput {

    }
}

