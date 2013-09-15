package com.greedygame.facebook;

public interface FacebookInterface {
	public void login();
	
	public boolean isActive();
	
	public void onUserRecived();
	
	public String getUsername();
	
	public void inviteDialog();
	public void inviteDialogLevel2();
}
