package com.greedygame.apprunner;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.greedygame.aap.RunnerGame;


public class MainActivity extends AndroidApplication {
	
	public MainActivity(){

	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;     
        initialize(new RunnerGame(), cfg);
    }
    
  
    
    public void onBackPressed() {
    	finish();      
    }

	

}
