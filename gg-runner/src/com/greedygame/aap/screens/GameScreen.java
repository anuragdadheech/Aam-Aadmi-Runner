package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.greedygame.AbstractScreen;
import com.greedygame.Assets;
import com.greedygame.aap.RunnerGame;
import com.greedygame.aap.RunnerTable;
import com.greedygame.app.dialog.RetryDialog;
import com.greedygame.app.dialog.SupportDialog;
import com.greedygame.app.dialog.WindowDialogListener;

public class GameScreen extends AbstractScreen implements GestureListener, WindowDialogListener{
	private Stage stage;
	private RunnerTable runnerTable;

	
	private RetryDialog retryWindow;
	private SupportDialog supportWindow;
	
	public float SCORE = 0;
	

	public GameScreen(RunnerGame game) {
		super(game);			
    }
	

	@Override
	public void show() {

		stage = new Stage(RunnerGame.VIRTUAL_WIDTH, RunnerGame.VIRTUAL_HEIGHT,true);
		
		runnerTable = new RunnerTable(this, RunnerGame.VIRTUAL_WIDTH, RunnerGame.VIRTUAL_HEIGHT);

		stage.addActor(runnerTable);
		

        supportWindow = new SupportDialog(this, stage);
        retryWindow = new RetryDialog(this, stage);
		 
		Gdx.input.setInputProcessor(new GestureDetector(this));	      
        Gdx.input.setCatchBackKey(true);        
        
        Assets.BACKGROUND.loop(1f);

	}

	
	@Override
	public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
		runnerTable.setSize(width, height);
        retryWindow.setPosition((width-retryWindow.getWidth())/2, (height-retryWindow.getHeight())/2);
        supportWindow.setPosition((width-supportWindow.getWidth())/2, (height-supportWindow.getHeight())/2);

        stage.setViewport(width , height, true);
	}

	boolean showRetryDialog = false;
	boolean keyDown = false;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);       
        stage.act(delta);
		stage.draw();
		
        if( (Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
        	if(keyDown == false){
	        	keyDown = true;
	        	if(supportWindow.isVisible()){
	               	supportWindow.hide();
	               	retryWindow.show();
	        	}else if(retryWindow.isVisible()){
	    			game.setScreen(new MenuScreen(game)); 
	        	}
        	}
        }else{
        	keyDown = false;
        }
	}


	@Override 
    public void hide() {
    	Gdx.input.setInputProcessor(null);
    	stage.clear();
    	stage = null;    	
    }
	
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	
	@Override 
	public boolean touchDown(float x, float y, int pointer, int button) {
		if(x > Gdx.graphics.getWidth()/2){
			runnerTable.jumpMan();
		}else {
			runnerTable.duckMan();
		}
		return false;
	}

	@Override public boolean longPress(float x, float y) {
		return false;
	}
	
	
	public void showRetry(){
		if(!retryWindow.isVisible()){
			retryWindow.show();
		}
		
		if(supportWindow.isVisible()){
			retryWindow.hide();
		}
	}
	
	@Override public void resume() {}
	@Override public void pause() {
				
	}
	@Override public void dispose() {}	
	@Override public boolean tap(float x, float y, int count, int button) {	return false;}

	@Override public boolean pan(float x, float y, float deltaX, float deltaY) {return false;}
	@Override public boolean zoom(float initialDistance, float distance) {return false;}
	@Override public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {return false;}

	
	
	@Override
	public void onButtonClicked(String name) {
			if(name == "retry"){
				//play_again
				retryWindow.hide();
				Gdx.input.setInputProcessor(new GestureDetector(this));
				runnerTable.restart();
				//Assets.BACKGROUND.loop(1f);
			}else if(name == "support"){
				//support
				retryWindow.hide();
				supportWindow.show();
			}else if(name == "score"){
				//leaderboard
				//TODO: with google play service
			}else if(name == "donate"){
				//donate
				Gdx.net.openURI("https://donate.aamaadmiparty.org");
			}else if(name == "vote"){
				//vote
				Gdx.net.openURI("http://greedygame.com/aaprunner");
			}else if(name == "share"){
				//share
				Gdx.net.openURI("http://m.facebook.com/sharer.php?u=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.greedygame.runner");
			}
		
	}


	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}


}