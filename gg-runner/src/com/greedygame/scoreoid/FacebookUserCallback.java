package com.greedygame.scoreoid;

import com.badlogic.gdx.Gdx;
import com.greedygame.aap.Record;
import com.greedygame.aap.RunnerGame;

public class FacebookUserCallback implements UserCallBack {
	@Override
	public void setUsername(String username,String First,String Last) {
		String storedusername = RunnerGame.setting.getString("username");
		RunnerGame.score.setConnected(true);
		Gdx.app.log("Stored username", storedusername);
		if(storedusername.equals(username) == false){
			RunnerGame.score.setUsername(username);
			RunnerGame.score.setName(First, Last);
			RunnerGame.scoreoid.GetorCreatePlayer(username, First, Last);
		}else{
			RunnerGame.score.setUsername(username);
			RunnerGame.score.setName(First, Last);
		}
	}

	@Override
	public void unlockLevel2() {
		
		
	}
}
