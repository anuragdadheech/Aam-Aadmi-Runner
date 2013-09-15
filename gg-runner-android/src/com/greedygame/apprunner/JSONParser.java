package com.greedygame.apprunner;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.greedygame.aap.Record;


public class JSONParser {
	List<Record> scores;
	
	public JSONParser(){
		scores=new ArrayList<Record>();
	}
	

	
	public List<Record> ParseScores(String response){
		scores.clear();
		if(response==null)
			return scores;
		try {
			//For parsing the data just look at the content in the Scoreoid Console 
			//and then we can understand the structure of the return data
			//We receive a json array of player scores
			JSONArray json=new JSONArray(response);
			
			for (int i = 0; i < json.length(); i++) {
				//To get each player score object from the array
		        JSONObject jsonObject = json.getJSONObject(i);
		        
		        //Inside this object we have two objects of Player and Score
		        //Get Player Object's json string
		        String player=jsonObject.getString("Player");
		        //Create json object from player String
		        JSONObject playerobj=new JSONObject(player);
		        
		        //Retrieve the player first name
		        String player_username=playerobj.getString("username");
		        String player_first=playerobj.getString("first_name");
		        String player_last=playerobj.getString("last_name");
		        
		        
		        //Get Score object's json string
		        String scoreStr=jsonObject.getString("Score");
		        //Create json object from score string
		        JSONObject scoreobj=new JSONObject(scoreStr);
		        //Retrieve score 
		        String score=scoreobj.getString("score");
		        
		        long s = Long.parseLong(score, 10);
		        
		        //Create a Score object and add it in the arraylist 
		        scores.add(new Record(player_username,player_first,player_last, (int)s));
		        
		        
		      }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return scores;
	}
}
