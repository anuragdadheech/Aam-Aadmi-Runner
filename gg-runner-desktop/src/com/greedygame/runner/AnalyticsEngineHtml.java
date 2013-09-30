package com.greedygame.runner;

import com.greedygame.AnalyticsEngine;

public class AnalyticsEngineHtml implements AnalyticsEngine{
	
	public AnalyticsEngineHtml() {         
          
          
          }

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendView(String name) {
		System.out.println("view : "+name);
		
	}

	@Override
	public void setCustomVar(int slot, String label, String val, int scope) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvent(String category, String subCategory, String label,
			long value) {
		System.out.println("event : "+category+"/"+subCategory+"/"+label+"/"+value);
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	


}
