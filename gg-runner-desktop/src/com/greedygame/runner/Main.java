package com.greedygame.runner;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.greedygame.AnalyticsEngine;
import com.greedygame.aap.RunnerGame;

public class Main{
	
	static AnalyticsEngine analyticsEngine;
	static FbInterfaceHtml fbhtml =  new FbInterfaceHtml();
	static WebViewHtml webviewhtml;

	static ScoreInterfaceHtml scorehtml =  new ScoreInterfaceHtml();
	public static void main(String[] args) {
		analyticsEngine = new AnalyticsEngineHtml();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Aam Aadmi Runner";
		cfg.useGL20 = false;

		cfg.width = 960;
		cfg.height = 720;
		
		new LwjglApplication(new RunnerGame(analyticsEngine, fbhtml, scorehtml, webviewhtml), cfg);
	}
}
