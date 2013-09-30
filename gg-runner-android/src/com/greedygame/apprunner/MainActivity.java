package com.greedygame.apprunner;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.greedygame.AnalyticsEngine;
import com.greedygame.ScoreoidConstants;
import com.greedygame.WebViewInterface;
import com.greedygame.aap.Record;
import com.greedygame.aap.RunnerGame;
import com.greedygame.apprunner.ScoreoidWebService.AsyncResponse;
import com.greedygame.facebook.FacebookInterface;
import com.greedygame.scoreoid.FacebookUserCallback;
import com.greedygame.scoreoid.ScoreInterface;
import com.greedygame.scoreoid.UserCallBack;


public class MainActivity extends AndroidApplication implements WebViewInterface, ScoreInterface, FacebookInterface, ScoreoidWebService.AsyncResponse {
	

	private AnalyticsEngine analyticsEngine;
    private GraphUser user;
    
    private ProgressDialog progress;
    public Activity activity;
    public AsyncResponse asyncResponse;
    private int people_to_invite = 20;
	public MainActivity(){
		activity = this;
		asyncResponse = this;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analyticsEngine = new AnalyticsEngineAndroid(this);           
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new RunnerGame(analyticsEngine, this, this, this), cfg);
        
    }
       
    public void onBackPressed() {
    	finish();      
    }

	@Override
	public void login() {
		if(Session.openActiveSession(this, false, callback) == null){
			Log.i("facebook", "need to login");
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {
			    @Override
			    public void run() {
			    	Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
            		startActivity(intent);
			    }
			});
		}
	}
	
	@Override
	public void commitToVote() {
		// TODO: check internet connection, add toast if not connected
	    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
	    intent.putExtra("mode", "COMMIT_TO_VOTE");
	    startActivity(intent);
	}

	@Override
	public void donate() {
		// TODO: check internet connection, add toast if not connected
	    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
	    intent.putExtra("mode", "DONATE");
	    startActivity(intent);		
	}
	
    private Session.StatusCallback callback = new Session.StatusCallback() { 
        // callback when session changes state
        @SuppressWarnings("deprecation")
		@Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                // make request to the /me API
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    // callback after Graph API response with user object
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            MainActivity.this.user = user;
                            Toast.makeText(getApplicationContext(), "Welcome "+MainActivity.this.user.getFirstName()+" "+MainActivity.this.user.getLastName(),
        							Toast.LENGTH_SHORT).show();
                            
                            UserCallBack callback = new FacebookUserCallback();
                            register(callback);

                            //sendRequestDialog();
                        }
                    }
                });
            }
        }
    };
    
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public void register(UserCallBack callback) {        
		callback.setUsername(MainActivity.this.user.getUsername(), MainActivity.this.user.getFirstName(), MainActivity.this.user.getLastName());
    }

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void OpenHighScoresAround(final Record me) {
        if (!isNetworkAvailable())
        {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                	Toast.makeText(getApplicationContext(), "Ah! you need internet connection for leaderboard.",
							Toast.LENGTH_SHORT).show();
                }
            });
			

        } else{
        	if(RunnerGame.score.isConnected()){
				Runnable r=new Runnable() {			
					public void run() {		
						progress = ProgressDialog.show(activity, "Leaderboard", "Fetching records", true);
				        Bundle scoreBundle=new Bundle();
			        
				        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.GETPLAYERRANK);
				        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, me.getUsername());
				        scoreBundle.putLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE, (long)me.getScore());
				        scoreBundle.putString(ScoreoidConstants.SCOREOID_FIRST_NAME, me.getFirstName());
				        scoreBundle.putString(ScoreoidConstants.SCOREOID_LAST_NAME, me.getLasttName());
				        
				    	ScoreoidWebService wb = new ScoreoidWebService(asyncResponse);
				    	wb.execute(scoreBundle);
				    }
				};
				handler.post(r);
        	}else{
	        	login();
	        }
	}
	}
	

	@Override
	public void CreatePlayer(String Username, String First, String Last) {
		Log.i("MainActivity create",Username +","+First+" "+Last);		
        Bundle scoreBundle=new Bundle();
        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.CREATEPLAYER);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, Username);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_FIRST_NAME, First);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_LAST_NAME, Last);
    	ScoreoidWebService wb = new ScoreoidWebService(this);
    	wb.execute(scoreBundle);
	}

	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	

	
	@Override
	public void GetPlayer(String username) {
		Log.i("MainActivity getPlayer",username);		
        Bundle scoreBundle=new Bundle();
        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.GETPLAYER);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, username);
    	ScoreoidWebService wb = new ScoreoidWebService(this);
    	wb.execute(scoreBundle);
	}

	

	@Override
	public void CreateScore(final String username, final long score) {
		Log.i("MainActivity createScore",username+" "+score);	
		Runnable r=new Runnable() {			
			public void run() {
		        Bundle scoreBundle=new Bundle();
		        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.CREATESCORE);
		        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, username);
		        scoreBundle.putLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE, score);
		    	ScoreoidWebService wb = new ScoreoidWebService(asyncResponse);
		    	wb.execute(scoreBundle);
			}
		};
		handler.post(r);		
	}


	@Override
	public void onUserRecived() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setusername(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void responseReceived(final Bundle output) {
		String function = output.getString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION);
		String response = output.getString("response");
		boolean error = false;
		String errMsg = "Connection lost with leaderboard. Please try again.";
		
		System.out.println("Scoreoid "+function+" response >"+ response);
		if(function.equals(ScoreoidConstants.CREATEPLAYER)){
			if(response!=null && 
					(response.contains("success") || 
					response.contains("already exists")) ){
				RunnerGame.setting.setString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, output.getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME));
				RunnerGame.setting.setLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE, 0);
				RunnerGame.setting.saveSettings();
				progress.dismiss();
			}else{
				error = true;
				errMsg = "Connection lost while creating your account. Please try again.";
			}
		}else if(function.equals(ScoreoidConstants.GETPLAYER)){
			if(response!=null){ 
				if(response.contains("Player not found")==true) {
					//So create new player here
					output.remove(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION);
					output.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.CREATEPLAYER);        
					ScoreoidWebService wb = new ScoreoidWebService((AsyncResponse) activity);
	           		wb.execute(output);

	        		progress.setMessage("Hi! You seems to be new gamer here. Creating your account");
				}else{
					try {
						JSONArray jsonarray =new JSONArray(response);
						JSONObject jsonPlayer = jsonarray.getJSONObject(0).getJSONObject("Player");
						long score = jsonPlayer.getLong("best_score");
						RunnerGame.setting.setString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, output.getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME));
						RunnerGame.setting.setLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE, score);
						RunnerGame.setting.saveSettings();
						progress.dismiss();
					} catch (JSONException e) {
						e.printStackTrace();
						error = true;
						errMsg = "Improper user data recevied. Please try again.";
					}		
				}
			}else{
				error = true;
				errMsg = "Connection lost while fetching your record. Please try again.";
			}
			
		}else if(function.equals(ScoreoidConstants.CREATESCORE)){
			if(response!=null && response.contains("success")) {
				RunnerGame.setting.setString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, output.getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME));
				RunnerGame.setting.setLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE, output.getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE));
				RunnerGame.setting.saveSettings();
			}else{
				error = true;
				errMsg = "Connection lost while updating your score. Please try again.";
			}
			
		}else if(function.equals(ScoreoidConstants.GETBESTSCORES)){
			if(response!=null && response!=""){
				
        		progress.dismiss();        		
        		long rank = output.getLong(ScoreoidConstants.SCOREOID_BUNDLE_RANK);
        		String username = output.getString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME);
        		long score = output.getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE);
        		String firstname = output.getString(ScoreoidConstants.SCOREOID_FIRST_NAME);
        		String lastname = output.getString(ScoreoidConstants.SCOREOID_LAST_NAME);
				Intent intent = new Intent(getApplicationContext(), ScoresActivity.class);
				intent.putExtra("respone", response);
				intent.putExtra("rank", rank);
				intent.putExtra("username", username);
				intent.putExtra("score", score);
				intent.putExtra("name", firstname+" "+lastname);

				startActivity(intent);
			}else{
				error = true;
				errMsg = "Connection lost while fetching leaderboard. Please try again.";
			}			
		}else if(function.equals(ScoreoidConstants.GETPLAYERRANK)){
			if(response!=null && response!=""){		
				try {
					JSONObject jsonobject =new JSONObject(response);					
					long rank = jsonobject.getLong("rank");
					
					output.putLong(ScoreoidConstants.SCOREOID_BUNDLE_RANK, rank);					
					output.remove(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION);
					output.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.GETBESTSCORES);
					output.putLong(ScoreoidConstants.SCOREOID_BUNDLE_LIMIT, 20);
					output.putLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORESTART, 0);			        
					ScoreoidWebService wb = new ScoreoidWebService((AsyncResponse) activity);
	           		wb.execute(output);

				} catch (JSONException e) {
					e.printStackTrace();
					error = true;
					errMsg = "Improper user data recevied. Please try again.";
				}	
				
				/*
        		progress.dismiss();
				Intent intent = new Intent(getApplicationContext(), ScoresActivity.class);
				intent.putExtra("respone", response);
				startActivity(intent);*/
			}else{
				error = true;
				errMsg = "Connection lost while fetching leaderboard. Please try again.";
			}
			
		}
		
		if(error == true){
			output.remove("response");
	
	        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	        builder.setTitle("Leaderboard")
	        		.setMessage(errMsg)
	               .setPositiveButton(R.string.Retry, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {	
	               		ScoreoidWebService wb = new ScoreoidWebService((AsyncResponse) activity);
	               		wb.execute(output);
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   dialog.dismiss();
	                   }
	               });
	        builder.create();
	        builder.show();
		}
		
	}

	@Override
	public void GetorCreatePlayer(String Username, String First, String Last) {
		progress = ProgressDialog.show(activity, "Leaderboard", "Fetching your record", true);
		Log.i("GetorCreatePlayer",Username +","+First+" "+Last);		
        Bundle scoreBundle=new Bundle();
        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_FUNCTION, ScoreoidConstants.GETPLAYER);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_BUNDLE_USERNAME, Username);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_FIRST_NAME, First);
        scoreBundle.putString(ScoreoidConstants.SCOREOID_LAST_NAME, Last);
    	ScoreoidWebService wb = new ScoreoidWebService(this);
    	wb.execute(scoreBundle);		
	}
	
	
	private void sendRequestDialog() {
	    Bundle params = new Bundle();
	    params.putString("message", "Clean your city, with AAM AADMI PARTY");
	    /*params.putString("data",
	            "{\"badge_of_awesomeness\":\"1\"," +
	            "\"social_karma\":\"5\"}");*/

	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(activity,
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                	              
                
                		
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(activity.getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(activity.getApplicationContext(), 
	                                "Network Error", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        System.out.print(requestId);
	                        if (requestId != null) {	    	                    
	                    		Set<String> v = values.keySet();	                    		
	                    		int l = v.size()-1;
	    	                	if(l<people_to_invite){
	    	                		Toast.makeText(activity.getApplicationContext(), 
	                                    "You have to invite "+(people_to_invite-l)+" friends more.", 
	                                    Toast.LENGTH_LONG).show();
	    	                		people_to_invite -= l;
	    	                	}else{
	    	        				RunnerGame.score.setLevel(1);
	    	    					RunnerGame.setting.setInt("level", 1);
	    	    					RunnerGame.setting.saveSettings();
	    	                		Toast.makeText(activity.getApplicationContext(), 
	    	                                "You have earned, entry to level 2!", 
	    	                                Toast.LENGTH_SHORT).show();
	    	                		
	    	                		UserCallBack callback = new FacebookUserCallback();
	                                callback.unlockLevel2();
	    	                		
	    	                	}
	                
	                        } else {
	                            Toast.makeText(activity.getApplicationContext(), 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                }

			

	            })
	            .build();
	    requestsDialog.show();
	}

	@Override
	public void inviteDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	if(RunnerGame.score.isConnected()){
            	    Bundle params = new Bundle();
            	    params.putString("message", "Clean your city, with AAM AADMI PARTY");
            	    /*params.putString("data",
            	            "{\"badge_of_awesomeness\":\"1\"," +
            	            "\"social_karma\":\"5\"}");*/

            	    WebDialog requestsDialog = (
            	        new WebDialog.RequestsDialogBuilder(activity,
            	            Session.getActiveSession(),
            	            params))
            	            .setOnCompleteListener(new OnCompleteListener() {

            	                @Override
            	                public void onComplete(Bundle values,
            	                    FacebookException error) {
            	                	              
                            
                            		
            	                    if (error != null) {
            	                        if (error instanceof FacebookOperationCanceledException) {
            	                            Toast.makeText(activity.getApplicationContext(), 
            	                                "Request cancelled", 
            	                                Toast.LENGTH_SHORT).show();
            	                        } else {
            	                            Toast.makeText(activity.getApplicationContext(), 
            	                                "Network Error", 
            	                                Toast.LENGTH_SHORT).show();
            	                        }
            	                    } else {
            	                        final String requestId = values.getString("request");
            	                        if (requestId != null) {	    	                    
            	                        	Toast.makeText(activity.getApplicationContext(), 
                	                                "Request sent", 
                	                                Toast.LENGTH_SHORT).show();
            	                
            	                        } else {
            	                            Toast.makeText(activity.getApplicationContext(), 
            	                                "Request cancelled", 
            	                                Toast.LENGTH_SHORT).show();
            	                        }
            	                    }   
            	                }           			

            	            })
            	            .build();
            	    requestsDialog.show();
	        	}else{
		        	login();
		        }            
            }
        });

		
	}

	@Override
	public void inviteDialogLevel2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("A Revolution is coming")
					.setMessage("To fight for \"Swaraj\", clear \"Evolution\" level or invite 20 FB friends")
					.setPositiveButton(R.string.Invite, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {	
							if(RunnerGame.score.isConnected()){
								sendRequestDialog();
				        	}else{
					        	login();
					        }
						}
					 })
					   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					       public void onClick(DialogInterface dialog, int id) {
					    	   dialog.dismiss();
					    }
					   });
                builder.create();
                builder.show();
            
            }
        });
	}

	

	@Override
	public void public_action(FB_Action action) {
		String url = null;
		if(action == FB_Action.LEVEL_1) {
			url = "http://samples.ogp.me/1441903689369051";
		}else if(action == FB_Action.LEVEL_2) {
			url = "http://samples.ogp.me/1441903399369080";
		}else if(action == FB_Action.LEVEL_1_COMPLETE) {
			url = "http://samples.ogp.me/1443148499244570";
		}else if(action == FB_Action.START) {
			
		}else if(action == FB_Action.SUPPORT) {
			
		}else if(action == FB_Action.HIGH_SCORE) {
			
		}
	
		if (url == null){
			return;
		}
		
		Bundle params = new Bundle();
		params.putString("level", url);

		Request request = new Request(
		    Session.getActiveSession(),
		    "me/aaprunner:play",
		    params,
		    HttpMethod.POST
		);
		Response response = request.executeAndWait();
		// handle the response
		
	}
	
}
