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
import com.greedygame.facebook.FacebookInterface;

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
        init();
        
        game.analyticsEngine.sendView("MenuScreen");
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
        //Do not dispose MenuScreen
    }

	@Override
	public void onButtonClicked(String name) {
		Gdx.app.log("MenuScreen", "onButtonClicked "+name);
		if(name == "play"){
			//play
			game.setScreen(new SelectScreen(game)); 
		}else if(name == "support"){
			//support
			menuWindow.hide();
			supportWindow.show();	
			RunnerGame.facebook.public_action(FacebookInterface.FB_Action.SUPPORT);
			game.analyticsEngine.sendEvent("support", "mainmenu", "support", 0);
		}else if(name == "instruction"){
			//instructions
			//game.setScreen(new InstructionScreen(game));
			game.setScreen(new LoadingScreen(game, Constant.SCREEN_INSTRUCTION));
		}else if(name == "donate"){
			//donate
			RunnerGame.webView.donate();
			game.analyticsEngine.sendEvent("donate", "mainmenu", "donate", 0);
		}else if(name == "vote"){
			//vote
			RunnerGame.webView.commitToVote();
			game.analyticsEngine.sendEvent("vote", "mainmenu", "vote", 0);
		}else if(name == "share"){
			//share
			//Gdx.net.openURI(Constant.LINK_FB_SHARE);
			RunnerGame.facebook.inviteDialog();
			game.analyticsEngine.sendEvent("invite", "mainmenu", "invite", 0);
		}		
	}

	public void activity() {
		// TODO Auto-generated method stub
		
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
