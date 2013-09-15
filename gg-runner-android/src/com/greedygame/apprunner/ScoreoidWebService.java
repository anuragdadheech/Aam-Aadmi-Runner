package com.greedygame.apprunner;

import android.os.AsyncTask;
import android.os.Bundle;

import com.greedygame.scoreoid.ScoreoidConstants;


public class ScoreoidWebService	extends AsyncTask<Bundle, String, Bundle> {
	public interface AsyncResponse {
	    void responseReceived(Bundle output);
	}
	
	private AsyncResponse delegate=null;
	private ScoreoidWrapper scoreoidObject;
	
	public ScoreoidWebService(AsyncResponse d){
		scoreoidObject = new ScoreoidWrapper();
		delegate = d;
	}
	
	@Override
	protected Bundle doInBackground(Bundle... params) {
		
		int count = params.length;
		Bundle rent = null;
		String response = null;
		if(count>0){
			int i =0;
			String function = params[i].getString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION);
			rent = params[i];
			if(function.equals(ScoreoidConstants.CREATEPLAYER)){				

				String username = params[i].getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME);
				String firstname = params[i].getString(ScoreoidConstants.SCOREOID_FIRST_NAME);
				String lastname = params[i].getString(ScoreoidConstants.SCOREOID_LAST_NAME);
				response=scoreoidObject.CreatePlayer(username, firstname, lastname);				
			}else if(function.equals(ScoreoidConstants.GETPLAYER)){				

				String username = params[i].getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME);
				response=scoreoidObject.GetPlayer(username);				
			}else if(function.equals(ScoreoidConstants.CREATESCORE)){				

				String username = params[i].getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME);
				long score =  params[i].getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE);
				response=scoreoidObject.CreateScore(username, (int)score);				
			}else if(function.equals(ScoreoidConstants.GETBESTSCORES)){

				long limit = params[i].getLong(ScoreoidConstants.SCOREOID_BUNDLE_LIMIT);
				long start = params[i].getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORESTART);
				response=scoreoidObject.GetBestScores(start, limit);
			}else if(function.equals(ScoreoidConstants.GETPLAYERRANK)){
				
				String username = params[i].getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME);
				response=scoreoidObject.GetPlayerRank(username);
			}
		}
		
		rent.putString("response", response);			
		return rent;
	}
	
	@Override
	protected void onPostExecute(Bundle resBundle) {
		//super.onPostExecute(resBundle);
		delegate.responseReceived(resBundle);
	}

}