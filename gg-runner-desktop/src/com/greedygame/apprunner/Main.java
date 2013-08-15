package com.greedygame.apprunner;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.greedygame.aap.RunnerGame;

public class Main{
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "swipe-race-tutorial";
		cfg.useGL20 = false;
		/*
		cfg.width = 800;
		cfg.height = 480;*/
		cfg.width = 960;
		cfg.height = 720;
		
		new LwjglApplication(new RunnerGame(), cfg);
	}
}
