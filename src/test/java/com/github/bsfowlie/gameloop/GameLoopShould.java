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

        verifyNoMoreInteractions(testGame);
    }

    @Test
    @DisplayName("invoke update once if game is running")
    public void invokeUpdateOnceIfGameIsRunning() {

        given(testGame.isRunning()).willReturn(true);

        loop.run();

        then(testGame).should(atLeastOnce()).update();
    }

}

