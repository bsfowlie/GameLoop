package com.github.bsfowlie.gameloop;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameLoop should")
public final class GameLoopShould {

    @Mock private Game testGame;
    @InjectMocks private GameLoop loop;

    @Test
    @DisplayName("do nothing if game is not running")
    public void doNothingIfGameIsNotRunning() {

        given(testGame.isRunning()).willReturn(false);

        loop.run();

        then(testGame).should(times(0)).update();
    }

    @Test
    @DisplayName("invoke update once if game is running")
    public void invokeUpdateOnceIfGameIsRunning() {

        given(testGame.isRunning()).willReturn(true, false);

        loop.run();

        then(testGame).should(atLeastOnce()).update();
    }

    @Test
    @DisplayName("invoke update as long as game is running")
    public void invokeUpdateAsLongAsGameIsRunning() {

        given(testGame.isRunning()).willReturn(true, true, true, false);

        loop.run();

        then(testGame).should(times(3)).update();
    }

}

