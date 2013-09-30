package com.greedygame.apprunner;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
 
	private WebView webView;
	
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.webview);
						
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
			    String value = extras.getString("mode");
			    
			    if (value.equals("COMMIT_TO_VOTE")){
					webView = (WebView) findViewById(R.id.webview);
					webView.getSettings().setJavaScriptEnabled(true);
			    	webView.loadUrl("http://www.donate4india.org/web/ipledgeforaap.html?mode=game");
			
			    } else if (value.equals("DONATE")){
					webView = (WebView) findViewById(R.id.webview);
					webView.getSettings().setJavaScriptEnabled(true);
			    	webView.loadUrl("https://donate.aamaadmiparty.org/?utm_source=aap&utm_medium=android&utm_term=t1&utm_content=d1&utm_campaign=game");
			    }
			    
			}
		}
}