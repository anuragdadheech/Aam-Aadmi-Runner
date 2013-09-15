package com.greedygame.scoreoid;

import com.greedygame.aap.Record;


public interface ScoreInterface {
	public void OpenHighScoresAround(Record me);
	public void GetorCreatePlayer(String Username, String First, String Last);
	public void CreatePlayer(String Username, String First, String Last);
	public void CreateScore(String username, long score);
	public void GetPlayer(String username);
	public void setusername(String username);
}
