package com.greedygame.aap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.greedygame.Assets;
import com.greedygame.aap.screens.LoadingScreen;

public class RunnerGame extends Game {
	
	//Aspect Ratio maintenance
	public static final float VIRTUAL_WIDTH = 960;
	public static final float VIRTUAL_HEIGHT = 720;
    public AssetManager manager = new AssetManager();
    
    public static boolean DEBUG = false;
    
    	
 	public RunnerGame() {

	}

 	/*
	public RunnerGame(AnalyticsEngine analyticsEngine2 ) {
		this.analyticsEngine = new AnalyticsEngineHtml();
		googlePlay = new GoogleInterface();
	}*/


	@Override
	public void create() {
		setScreen(new LoadingScreen(this));

	}

	@Override
	public void dispose() {
		Assets.unload();
	}
}