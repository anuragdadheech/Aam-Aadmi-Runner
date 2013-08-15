package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Assets;
import com.greedygame.DialogBox.DialogBox;
import com.greedygame.DialogBox.DialogBoxListener;
import com.greedygame.aap.RunnerGame;

public class MenuScreen extends AbstractScreen implements DialogBoxListener {
	private Stage stage;
	private Image screenBg;
	
	private DialogBox menuDialog, supportDialog;
	

	
	public MenuScreen(RunnerGame myGame){
		super(myGame);		
        Assets.init();
	}
	
    @Override
    public void show() {    	
        stage = new Stage();
        screenBg = new Image(Assets.BACK_DROP);          
		menuDialog =  new DialogBox(this, Assets.MENUBOX, "Main Menu"); 
		supportDialog = new DialogBox(this, Assets.SUPPORTBOX, "Support Menu"); 
        stage.addActor(screenBg);
        stage.addActor(menuDialog); 
        stage.addActor(supportDialog); 
        menuDialog.show();       
        Gdx.input.setCatchBackKey(true);        

    }

    
  
    
    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
		menuDialog.centerAlign(width, height);
		supportDialog.centerAlign(width, height);
        stage.setViewport(width , height, true);
    }

    boolean keyDown = false;
    
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // Show the loading screen
        stage.act();
        stage.draw();
        
        if( (Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
        	if(keyDown == false){
	        	keyDown = true;
	        	if(supportDialog.isVisible()){
	        		System.out.print("support");
	               	supportDialog.hide();
	               	menuDialog.show();
	        	}else if(menuDialog.isVisible()){
	        		System.out.print("menu");
	        		 Gdx.app.exit();
	        	}
        	}
        }else{
        	keyDown = false;
        }
        
    }

    @Override
    public void hide() {
    	Gdx.input.setInputProcessor(null);    	
    }

	@Override
	public void onButtonClicked(int id) {
		if(menuDialog.isVisible()){
			if(id == 0){
				//play
				game.setScreen(new GameScreen(game)); 
			}else if(id==1){
				//support
				menuDialog.hide();
				supportDialog.show();				

			}else if(id==2){
				//instructions
				game.setScreen(new InstructionScreen(game)); 
			}
		}else if(supportDialog.isVisible()){
			if(id == 0){
				//donate
				Gdx.net.openURI("https://donate.aamaadmiparty.org");
			}else if(id==1){
				//vote
				Gdx.net.openURI("http://greedygame.com/aaprunner");
			}else if(id==2){
				//share
				Gdx.net.openURI("http://m.facebook.com/sharer.php?u=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.greedygame.runner");
			}
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
