package com.github.bsfowlie.gameloop;

public interface Game<T> {

    boolean isRunning();

    void update(T input);

    void render();

}
