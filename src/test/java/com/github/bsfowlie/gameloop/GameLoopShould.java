package com.github.bsfowlie.gameloop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameLoop should")
public class GameLoopShould {

    @Test
    @DisplayName("do nothing if game is not running")
    public void doNothingIfGameIsNotRunning(@Mock Game testGame) {

        GameLoop loop = new GameLoop(testGame);

        loop.run();

        Mockito.verifyNoInteractions(testGame);
    }

}

