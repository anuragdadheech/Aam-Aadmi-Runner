package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerGame;
import com.greedygame.assets.AssetsCommon;
import com.greedygame.dialog.MainDialog;
import com.greedygame.dialog.SupportDialog;
import com.greedygame.dialog.WindowDialogListener;

public class MenuScreen extends AbstractScreen implements WindowDialogListener {
   
	private MainDialog menuWindow;
	private SupportDialog supportWindow;
	private Stage stage;
    private Image screenBg;

    boolean keyDown = false;
    public MenuScreen(RunnerGame myGame) {
        super(myGame);
    }

    @Override
    public void show() {
    	stage = new Stage();
    	screenBg = new Image(AssetsCommon.BACK_DROP);  
        stage.addActor(screenBg);        
        menuWindow = new MainDialog(this, stage);
        supportWindow = new SupportDialog(this, stage);            
        menuWindow.setPosition((stage.getWidth()-menuWindow.getWidth())/2 + 250, (stage.getHeight()-menuWindow.getHeight())/2);
        supportWindow.setPosition((stage.getWidth()-menuWindow.getWidth())/2, (stage.getHeight()-menuWindow.getHeight())/2);
        Gdx.input.setCatchBackKey(true);
        menuWindow.show();
        game.analyticsEngine.sendView("loadingScreen");
        
        init();
    }

    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;       
		stage.setViewport(width , height, true);
		menuWindow.setPosition((stage.getWidth()-menuWindow.getWidth())/2, (stage.getHeight()-menuWindow.getHeight())/2);
        supportWindow.setPosition((stage.getWidth()-menuWindow.getWidth())/2, (stage.getHeight()-menuWindow.getHeight())/2);
    }
    
	@Override
	public void init() {
		keyDown = false;		
	}
	
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();    
        if( (Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
        	if(keyDown == false){
	        	keyDown = true;
	        	if(supportWindow.isVisible()){
	               	supportWindow.hide();
	               	menuWindow.show();
	        	}else if(menuWindow.isVisible()){
	        		 Gdx.app.exit();
	        	}
        	}
        }else{
        	keyDown = false;
        }
    }

    @Override
    public void hide() {
        // Dispose the loading AssetsCommon as we no longer need them
    }

	@Override
	public void onButtonClicked(String name) {
		if(name == "play"){
			//play
			//game.setScreen(new LoadingScreen(game, 1)); 
			game.setScreen(new SelectScreen(game)); 
		}else if(name == "support"){
			//support
			menuWindow.hide();
			supportWindow.show();				

		}else if(name == "instruction"){
			//instructions
			//game.setScreen(new InstructionScreen(game));
			game.setScreen(new LoadingScreen(game, Constant.SCREEN_INSTRUCTION));
		}else if(name == "donate"){
			//donate
			Gdx.net.openURI(Constant.LINK_AAP_DONATION);
		}else if(name == "vote"){
			//vote
			Gdx.net.openURI(Constant.LINK_GG_ENGAGEMENT);
		}else if(name == "share"){
			//share
			//Gdx.net.openURI(Constant.LINK_FB_SHARE);
			RunnerGame.facebook.inviteDialog();
		}		
	}




	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelClicked(String name) {
		// TODO Auto-generated method stub
		
	}


}
