package com.greedygame.facebook;

public interface FacebookInterface {
    public enum FB_Action {
        LEVEL_1,
        LEVEL_2,
        LEVEL_1_COMPLETE,
        START,
        SUPPORT,
        HIGH_SCORE;
    }
	
	public void login();
	
	public boolean isActive();
	
	public void onUserRecived();
	
	public String getUsername();
	
	public void inviteDialog();
	public void inviteDialogLevel2();
	
	public void public_action(FB_Action action);
}
