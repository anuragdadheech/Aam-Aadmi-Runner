package com.greedygame.apprunner;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

import android.os.Handler;

import com.greedygame.scoreoid.ScoreoidConstants;

public class ScoreoidWrapper {
	
	DefaultHttpClient httpClient;
	HttpContext localContext;
	String webServiceUrl;
	
	Handler handler;
	
	HttpPost httpPost = null;
	HttpResponse response = null;
	
	public ScoreoidWrapper(){
		HttpParams myParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
		httpClient = new DefaultHttpClient(myParams);
		localContext = new BasicHttpContext();
		
		//Scoreoid API BASE URL
		webServiceUrl = ScoreoidConstants.SCOREOID_URL;
		handler=new Handler();
		
	}
	
	String webInvoke(String methodName,List<NameValuePair> params){
		httpPost = new HttpPost(webServiceUrl + methodName);
		
		String data = "";
		String serverResponsePhrase;
		int serverStatusCode;
		try {
			//Passing the parameters
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			//Getting HttpResponse
			response = httpClient.execute(httpPost);
			
			System.out.print("Scoreoid request"+httpPost);
			
			//Reading the content part of the response
			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			
			//Convert the reponse to string to parse it later
			String ret = new String(baf.toByteArray());

			// Response from the server
			serverResponsePhrase = response.getStatusLine().getReasonPhrase();
			serverStatusCode = response.getStatusLine().getStatusCode();
			
			System.out.println("response "+serverStatusCode+serverResponsePhrase);
			// Response from the server
			data = ret;
		} catch (Exception e) {
			// Exception handling
		}
		return data;
	}
	
	public String GetBestScores(long start,long count) {
		String function=ScoreoidConstants.GETBESTSCORES;
		System.out.println("ScoreoidWrapper "+function +":"+start+","+count);
		
//		currentService=ScoreoidAPIType.GETBESTSCORES;
		ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("api_key", ScoreoidConstants.SCOREOID_APIKEY));
		param.add(new BasicNameValuePair("game_id", ScoreoidConstants.SCOREOID_GAME_ID));
		param.add(new BasicNameValuePair("response", ScoreoidConstants.SCOREOID_RESPONSETYPE));
		param.add(new BasicNameValuePair("order_by", ScoreoidConstants.SCOREOID_SCORE_ORDERBY));
		param.add(new BasicNameValuePair("order", ScoreoidConstants.SCOREOID_SCORE_ORDER));
		param.add(new BasicNameValuePair("limit",String.valueOf(start)+","+String.valueOf(count)));
		
		return webInvoke(function,param);
	}
	
	public String CreatePlayer(String username, String first, String last) {
		String function=ScoreoidConstants.CREATEPLAYER;
		System.out.println("ScoreoidWrapper "+function +":"+username+","+first+" "+last);
		
		ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("api_key", ScoreoidConstants.SCOREOID_APIKEY));
		param.add(new BasicNameValuePair("game_id", ScoreoidConstants.SCOREOID_GAME_ID));
		param.add(new BasicNameValuePair("response", ScoreoidConstants.SCOREOID_RESPONSETYPE));
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("first_name", first));
		param.add(new BasicNameValuePair("last_name", last));
		param.add(new BasicNameValuePair("score", String.valueOf(0)));
		return webInvoke(function,param);
	}

	public String GetPlayer(String username) {
		String function=ScoreoidConstants.GETPLAYER;
		System.out.println("ScoreoidWrapper "+function +":"+username);
		ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("api_key", ScoreoidConstants.SCOREOID_APIKEY));
		param.add(new BasicNameValuePair("game_id", ScoreoidConstants.SCOREOID_GAME_ID));
		param.add(new BasicNameValuePair("response", ScoreoidConstants.SCOREOID_RESPONSETYPE));
		param.add(new BasicNameValuePair("username", username));		
		return webInvoke(function,param);
	}
	
	public String GetPlayerRank(String username) {
		String function=ScoreoidConstants.GETPLAYERRANK;
		System.out.println("ScoreoidWrapper "+function +":"+username);
		ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("api_key", ScoreoidConstants.SCOREOID_APIKEY));
		param.add(new BasicNameValuePair("game_id", ScoreoidConstants.SCOREOID_GAME_ID));
		param.add(new BasicNameValuePair("response", ScoreoidConstants.SCOREOID_RESPONSETYPE));
		param.add(new BasicNameValuePair("username", username));		
		return webInvoke(function,param);
	}
	
	public String CreateScore(String username, int score) {
		String function=ScoreoidConstants.CREATESCORE;
		ArrayList<NameValuePair> param=new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("api_key", ScoreoidConstants.SCOREOID_APIKEY));
		param.add(new BasicNameValuePair("game_id", ScoreoidConstants.SCOREOID_GAME_ID));
		param.add(new BasicNameValuePair("response", ScoreoidConstants.SCOREOID_RESPONSETYPE));
		param.add(new BasicNameValuePair("username", username));
		param.add(new BasicNameValuePair("score", String.valueOf(score)));		
		return webInvoke(function,param);
	}
	
}
