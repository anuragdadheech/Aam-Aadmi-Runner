package com.greedygame;

import com.badlogic.gdx.Screen;
import com.greedygame.aap.RunnerGame;

/**
 * @author Mats Svensson
 */
public abstract class AbstractScreen implements Screen {

    public RunnerGame game;

    public AbstractScreen(RunnerGame myGame) {
        this.game = myGame;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
