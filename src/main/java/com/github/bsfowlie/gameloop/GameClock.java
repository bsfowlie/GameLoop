package com.github.bsfowlie.gameloop;

public interface GameClock {

    int MILLISECONDS_PER_SECOND = 1000;

    int FRAMES_PER_SECOND = 60;

    int FRAME_DURATION = MILLISECONDS_PER_SECOND / FRAMES_PER_SECOND;

    int getCurrentTick();

}
