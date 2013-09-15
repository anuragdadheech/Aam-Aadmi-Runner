package com.greedygame.apprunner;

import android.content.Context;
import android.os.Handler;

import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.greedygame.AnalyticsEngine;

public class AnalyticsEngineAndroid implements AnalyticsEngine{
	Handler uiThread;
    Context appContext;
    
	private Tracker mGaTracker;
	private GoogleAnalytics mGaInstance;
	
	public AnalyticsEngineAndroid(Context appContext) {
		uiThread = new Handler();
		this.appContext = appContext;
	}
	
	@Override
	public void initialize() {
		// Get the GoogleAnalytics singleton. Note that the SDK uses
        // the application context to avoid leaking the current context.
        mGaInstance = GoogleAnalytics.getInstance(appContext);
        // Enable debug mode.
        mGaInstance.setDebug(true);
        // Set dispatch period to 30 seconds.
        GAServiceManager.getInstance().setDispatchPeriod(30);
        // Use the GoogleAnalytics singleton to get a Tracker.
        mGaTracker = mGaInstance.getTracker("UA-43098061-1"); // Placeholder tracking ID.
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendView(String name) {
		mGaTracker.sendView(name);
	}

	@Override
	public void setCustomVar(int slot, String label, String val, int scope) {
		//mGaTracker.set
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvent(String category, String action, String label,
			long value) {
		mGaTracker.sendEvent(category, action, label, value);
	}

	@Override
	public void close() {
		mGaTracker.close();
	}

}
