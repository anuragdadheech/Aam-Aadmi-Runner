package com.greedygame.aap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.greedygame.AnalyticsEngine;
import com.greedygame.Constant;
import com.greedygame.Settings;
import com.greedygame.aap.screens.LoadingScreen;
import com.greedygame.facebook.FacebookInterface;
import com.greedygame.scoreoid.ScoreInterface;

public class RunnerGame extends Game {
	
	public static final float VIRTUAL_WIDTH = 960;
	public static final float VIRTUAL_HEIGHT = 720;
    public AssetManager manager = new AssetManager();
    public AnalyticsEngine analyticsEngine;
    public static FacebookInterface facebook;
    
    public static boolean DEBUG = true;  
    
    public static Record score;
    
    public static ScoreInterface scoreoid;
	public static Settings setting;
	public static boolean isConnected;
    
 	public RunnerGame(AnalyticsEngine analyticsEngine, FacebookInterface _fb, ScoreInterface _scoreoid) {
		this.analyticsEngine = analyticsEngine;
		this.analyticsEngine.initialize();		
		facebook = _fb;
		scoreoid = _scoreoid;
		score = new Record();
		setting = new Settings();
		isConnected = false;
	}

	@Override
	public void create() {
		setting.loadSettings();
		score.setLevel(setting.getInt("level"));
		setScreen(new LoadingScreen(this, Constant.SCREEN_MENU));
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public void pause () { 
	   super.pause();
	}

	@Override
	public void resume () {
		super.resume();
	}
}